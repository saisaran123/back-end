function _(selector){
    return document.querySelector(selector);
}
function getId(selector){
    return document.getElementById(selector);
}
let debug = true;
//function log(text) {
//    if (debug) console.log(text);
//}

function log(messages) {
    if (debug) {
        const stack = new Error().stack.split("\n");
        const callerLine = stack[2].trim(); // Get the line where log() was called
        console.log(`[DEBUG] ${callerLine}:`, messages);
    }
}
let userName;
let userId;
let chatUsers = {}
let currentreciever;
let currentrecieverId;
let serverUrl = "ws://localhost:8080/kunto/chat";
let audio = document.querySelector("#myAudio");
const messagesDiv = document.getElementById("chat-messages")
const container = document.getElementById('users-container');

let plusFriend = document.querySelector(".plus-friend");
const FRIEND_SUGGEST = document.querySelector(".agf-container");

let friends = {}


let myGroups = {};

let globalUsers = {};

let usersMap = {};

let currentCommunitySection = null;
let receiverHeader = document.querySelector(".receiver-header");
let sendMessageInput = document.getElementById('message-input');
let sendMessageBtn = document.querySelector(".send-button");


let sayHelloBtn = document.getElementById("say-hello-btn");
let sayHelloTap = document.getElementById("say-Hello-Tap");

const continueButton = document.getElementById('continueButton'); // continue create btn

const mainPage = document.querySelector('.main-page'); //create group main-page
const confirmationPage = document.getElementById('confirmationPage');
const confirmationUsersList = document.getElementById('confirmationUsersList');

let createGroup = getId('create-group');
const usersList = document.getElementById('agf-users-list');
let selectedUsersDiv = document.querySelector(".selected-users");

let successCreateGroupBtn = document.querySelector(".success-create-group");
let groupName = document.getElementById("group-name-input");

let communityNav = document.querySelector(".community-nav-buttons");

const postsContainer = document.getElementById('posts');
const faqsContainer = document.getElementById('faqs');


let currentPage = 0;
const postsPerPage = 10;
let currentFaqPage = 0;



let agfMode = "initial";

const searchButton = document.querySelector('.agf-search-button');
const backButton = document.querySelector('.agf-back-button');
const agfSearchInput = document.querySelector('.agf-search-input');
const appContainer = document.querySelector('.agf-container');
const headerTitle = document.querySelector('.agf-header-left h1');

let fetchUsers = false;
let fileBtn = document.getElementById('file-button');
let fileInput = document.getElementById('file-input');
const imgPreview = document.getElementById("extra-file-preview");
let imageUpload = false;

const comModal = document.getElementById('create-post-modal');
const createPostBtn = document.getElementById('create-post-btn');
const createFaqBtn = document.getElementById('create-faq-btn');
const comCloseBtn = document.querySelector('.close');
const postForm = document.getElementById('post-form');



let lastMessageDate = '';


//image upload

fileBtn.addEventListener('click', function () {
    if (!currentreciever) {
        dispPopup('choose you friend or group....')
        return;
    }
    fileInput.click();
});


document.getElementById('file-input').addEventListener('change', function () {


    console.log("file");
    const file = this.files[0];
    if (file) {

        const reader = new FileReader();
        reader.onload = function (e) {
            imgPreview.children[0].children[0].src = e.target.result;
            log(imgPreview);

            imgPreview.classList.add('dis_flex');
            imgPreview.classList.remove('dis_none');
            imageUpload = true;
            fileBtn.disabled = true;

        };
        reader.readAsDataURL(file);
    }
});
document.querySelector(".remove-img").addEventListener('click', () => {
    fileInput.value = "";
    imgPreview.classList.remove('dis_flex');
    imgPreview.classList.add('dis_none');
    imageUpload = false;
    fileBtn.disabled = false;
})


function sanitizeFileName(fileName) {
    return fileName.replace(/\s+/g, '_').replace(/[^a-zA-Z0-9_.-]/g, '');
}

async function uploadImagetoServer(input) {

    const file = input.files[0];

    if (!file) {
        log('No image selected');
        // dispPopup("No image selected");
        return null;
    }

    if (!file.type.startsWith('image/')) {
        dispPopup('Please select a valid image file.');
        return;
    }

    const formData = new FormData();
    formData.append('image', file, sanitizeFileName(file.name));

    try {
        const response = await fetch('upload-image', {
            method: 'POST',
            body: formData
        });
		if(!checkSession(response)) return;
        const data = await response.json();
        log(data);

        if (data && data.imageUrl) {
            return { imageUrl: data.imageUrl };
        } else {
            log('Image upload failed');
            return null;
        }
    } catch (error) {
        console.error('Error:', error);
        dispPopup('Failed to upload image.');
        return null;

    }
}

//
//create post,faq 



createPostBtn.onclick = () => {
    updateCreateModal(currentCommunitySection);
    showModel(createPostBtn)
};

createFaqBtn.onclick = () => {
    updateCreateModal(currentCommunitySection);
    showModel(createFaqBtn)
}

function showModel(btnReference) {
    btnReference.classList.toggle("rotate-plus-btn");
    comModal.classList.toggle("dis_flex");
    comModal.classList.toggle("dis_none");

}


comCloseBtn.onclick = () => {
    comModal.classList.add("dis_none");
    comModal.classList.remove("dis_flex");
    if (currentCommunitySection == 'faq') {
        createFaqBtn.classList.remove("rotate-plus-btn")
    } else {
        createPostBtn.classList.remove("rotate-plus-btn")
    }

};


window.onclick = (event) => {
    if (event.target === comModal) {
        comModal.classList.add("dis_none");
        comModal.classList.remove("dis_flex");
        if (currentCommunitySection == 'faq') {
            createFaqBtn.classList.remove("rotate-plus-btn")
        } else {
            createPostBtn.classList.remove("rotate-plus-btn")
        }

    }
};


function updateCreateModal(currentTab) {
    const modalTitle = document.querySelector("#create-post-modal h2");
    const postForm = document.getElementById("post-form");


    if (currentTab === "posts") {
        modalTitle.innerText = "Create New Post";

        postForm.innerHTML = `
        	<div class="form-group">
            <label for="post-title">Title</label>
            <input type="text" id="post-title" name="post-title" placeholder="Enter post title" required>
        </div>

        <div class="form-group">
            <label for="post-content">Content</label>
            <textarea id="post-content" name="post-content" placeholder="Write your post..." required></textarea>
        </div>

        <div class="form-group">
            <label>Image</label>
            <div class="custom-file-input">
                <label class="custom-file-label" for="post-image">Choose Image</label>
                <span class="file-name">No file chosen</span>
                <input type="file" id="post-image" name="post-image" accept="image/*">
            </div>
            <img id="post-image-preview" class="post-image-preview" alt="Image Preview">
        </div>

        <button type="submit" class="submit-btn">submit</button>
        `;
        postImage = document.getElementById('post-image');
        postImagePreview = document.getElementById('post-image-preview');
        postLabel = document.querySelector('.file-name');
        postImage.addEventListener('change', postImagePreviewDisplayed);


    } else {
        modalTitle.innerText = "Ask a Question";

        postForm.innerHTML = `
        	<div class="form-group">
            	<label for="faq-title">Title</label>
            	<input type="text" id="faq-title" name="faq-title" placeholder="Enter question title" required>
        	</div>
        	 <div class="form-group">
             	<label for="faq-question">Content</label>
             	<textarea id="faq-question" name="faq-question" placeholder="Write your question..." required></textarea>
         	</div>
           
         	 <button type="submit" class="submit-btn">submit</button>
        `;
    }
}



let postImage;
let postLabel;
let postImagePreview;



function postImagePreviewDisplayed() {
    log("kshuhjkahd");
    const file = postImage.files[0];
    if (file) {
        postLabel.textContent = file.name;

        // Preview the selected image
        const reader = new FileReader();
        reader.onload = function (e) {
            postImagePreview.src = e.target.result;
            postImagePreview.style.display = 'block';
        }
        reader.readAsDataURL(file);
    } else {
        postLabel.textContent = 'No file chosen';
        postImagePreview.style.display = 'none';
    }
}


postForm.onsubmit = async (event) => {
    event.preventDefault();

    const currentTab = currentCommunitySection;

    let json = {};

    if (currentTab === "posts") {
        const title = document.getElementById('post-title');
        const content = document.getElementById('post-content');
        const postImage = document.getElementById('post-image');
        let imageUrl = 'no-image';

        if (postImage.files.length > 0) {
            let image = await uploadImagetoServer(postImage);
            imageUrl = image.imageUrl;
        }

        if (!title.value.trim() || !content.value.trim()) {
            dispPopup('Please fill in all required fields.');
            return;
        }

        json = {
            title: title.value.trim(),
           
            content: content.value.trim(),
            imageUrl: imageUrl
        };

    } else if (currentTab === "faq") {

        const questionTitle = document.getElementById('faq-title').value.trim();
        const questionText = document.getElementById('faq-question').value.trim();

        if (!questionTitle || !questionText) {
            dispPopup('Please enter both a question title and details.');
            return;
        }

        json = {
            title: questionTitle,
            questionText: questionText,
          
        };
    }

    try {
        const endpoint = currentTab === "posts" ? "createPost" : "createFAQ";

        const response = await fetch(endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(json)
        });

        const data = await response.json();
        log(data);
        if (data.success) {
            dispPopup(`${currentTab === "posts" ? "Post" : "Question"} created successfully!`);
            console.log("postdata")
            console.log(data);
            json.id = data.id;
            json.username = userName;
            json.liked = false;

            if (currentTab === "posts") {
                document.getElementById('posts').prepend(await createPostUI(json));
            } else {
                document.getElementById('faqs').prepend(await createFAQUI(json));
            }

            comCloseBtn.onclick();
        } else {
            dispPopup(`Failed to create ${currentTab === "posts" ? "post" : "question"}.`);
        }
    } catch (error) {
        console.error(`Error creating ${currentTab === "posts" ? "post" : "question"}:`, error);
        dispPopup(`Failed to create ${currentTab === "posts" ? "post" : "question"}.`);
    }
};

//


let findFriends = getId("find-friends-btn");
findFriends.onclick = () => {
    _(".search-friend-input").focus();
}

function getId(selector) {
    return document.getElementById(selector);
}


let searchFriendsInput = document.querySelector(".search-friend-input");


searchFriendsInput.addEventListener('input', () => {
    container.innerHTML = "";
    let val = searchFriendsInput.value.trim().toLowerCase();
    let fragment = document.createDocumentFragment();

    const searchAndAppend = (collection) => {
        for (const id in collection) {
            if (collection[id].name.toLowerCase().includes(val)) {
                fragment.appendChild(createUserItem(collection[id]));
            }
        }
    };

    searchAndAppend(friends);
    searchAndAppend(myGroups);

    container.appendChild(fragment);
});




async function login() {


    // username = document.getElementById("username").value;
    if (userName) {

        websocket = new WebSocket(serverUrl);

        websocket.onopen = async () => {
            log("Connected to server");

            const message = {
                type: "LOGIN",
                username: userName
            };

            try {
                const groups = await fetchGroupList(); // Fetch groups
                log(groups);

                message.groups = groups; // Attach groups to message

                websocket.send(JSON.stringify(message)); // Send login message with groups

                document.querySelector("button[onclick='sendMessage()']").disabled = false;
            } catch (error) {
                console.error("Error fetching groups:", error);
            }
        };


        websocket.onmessage = async (event) => {
            log(event);
            const msg = JSON.parse(event.data);
            log(msg);
            if (msg.type == 'GROUP') {
                if (msg.groupId == currentreciever.slice(1)) {
                    

                    const customFormat = getCurrentTimeStamp();
                    let m = {
                        senderUsername: msg["sender"],
                        message: msg.message,
                        timestamp: customFormat,
                        image: msg.image

                    }
                    log(m);
                    displayMessage(m);

                
                    moveUserToTop(msg.groupId);
                } else {
                    log("get group message");
                    if (getGroupObj("g" + msg.groupId)) {
                        let id = msg.groupId;
                        log(id);
                        let groupObj = myGroups["g" + id];
                        let unReadCount = incrementOrSet(groupObj, 'unReadMessages');
                        groupObj['unReadMessages'] = unReadCount;
                        let unReadMsgDiv = document.getElementById("nmsgg" + id);
                        log(unReadMsgDiv);
                        if (unReadCount != 0) {
                            unReadMsgDiv.classList.add('display_block');
                            unReadMsgDiv.classList.remove('dis_none');

                        }
                        unReadMsgDiv.innerText = unReadCount;
                        moveUserToTop("g" + id);
                        let notiMsg={
							title: "Message received from "+ groupObj.name,
							message:msg["sender"]+" : "+msg.message
						}
                        notifications.chat.push(notiMsg);

                    }
                }

            } else if (msg.type == 'updateOnline') {
                log(msg.name);
                console.log("updationd online")
                console.log(msg);
                document.getElementById(msg.id).children[0].children[0].classList.toggle("status-offline");
                updateOnlineUser(msg.id, msg.online);
            } else if (msg.type == 'GROUP_CREATION') {
                log("new group...")
                let group = msg.group;
                container.prepend((createUserItem(group)));
                myGroups[group.id] = group;
                let notiMsg = {
        			title: "New Group Created: " + group.name,  
       				 message: "Group Admin: " + msg.sender 
    				};

    				notifications.chat.push(notiMsg); // 

            }
            else {
                log(msg);
                log(msg["message"]);
                log(msg["sender"]);

                if (chatUsers[currentreciever] != msg["sender"]) {
                    log("not equal")
                    log(msg);
                    let friendName = msg["sender"];
                    if (usersMap.hasOwnProperty(friendName)) {
                        let id = usersMap[friendName];
                        log(id);
                        let friendObj = friends[id];
                        let unReadCount = incrementOrSet(friendObj, 'unReadMessages');
                        friendObj['unReadMessages'] = unReadCount;
                        let unReadMsgDiv = document.getElementById("nmsg" + id);
                        log(unReadMsgDiv);
                        if (unReadCount != 0) {
                            unReadMsgDiv.classList.add('display_block');
                            unReadMsgDiv.classList.remove('dis_none');

                        }
                        unReadMsgDiv.innerText = unReadCount;
                        
						let notiMsg={
							title: "Message received from your friend",
							message:msg["sender"]+" : "+msg.message
						}
                        notifications.chat.push(notiMsg);
                        moveUserToTop(id);

                    } else {
                        let fetchId = await fetchUserId(friendName);
                        let id = fetchId["user_id"];
                        log(id);

                        let obj = {
                            name: friendName,
                            id: 'u' + id,
                            type: 'user',
                            online: true,
                            about: await fetchUserAbout(id),
                            unReadMessages: 1

                        }
                        friends["u" + id] = obj;
                        chatUsers["u" + id] = friendName;
                        usersMap[friendName] = "u" + id;
                        let userUI = createUserItem(obj);
                        container.prepend(userUI);
                        let unReadMsgDiv = document.getElementById("nmsgu" + id);
                        log(unReadMsgDiv);
                        unReadMsgDiv.classList.add('display_block');
                        unReadMsgDiv.classList.remove('dis_none');
                        unReadMsgDiv.innerText = 1;

						let notiMsg={
							title: "Message received from your friend",
							message: obj.name+" : "+msg.message
						}
                        notifications.chat.push(notiMsg);
                    }



                    return;
                }

                const customFormat = getCurrentTimeStamp();
                let m = {
                    senderUsername: msg["sender"],
                    message: msg.message,
                    timestamp: customFormat,
                    image: msg.image

                }
                log(12);
                log(m);
                //audio.play();
                displayMessage(m);

            }
        };

        websocket.onerror = (error) => {
            console.error("WebSocket error:", error);
        };

        websocket.onclose = () => {
            log("Disconnected from server");
        };
    } else {
        dispPopup("Please enter a username.");
    }
}

function moveUserToTop(userId) {
    const usersContainer = document.getElementById('users-container');
    const userItem = document.getElementById(`${userId}`);

    if (userItem) {
        // Remove the user from the current position
        usersContainer.removeChild(userItem);

        // Add the user to the top of the list
        usersContainer.prepend(userItem);
    }
}

function incrementOrSet(obj, key) {
    obj[key] = (obj.hasOwnProperty(key) ? obj[key] : 0) + 1;
    return obj[key];
}
async function fetchUserAbout(id) {
    return 'hello dude';
}


function getCurrentTimeStamp() {
    const now = new Date();
    return `${now.getFullYear()}-${now.getMonth() + 1}-${now.getDate()} ${now.getHours()}:${now.getMinutes()}:${now.getSeconds()}`;

}

function updateOnlineUser(id, online) {
    friends[id]["online"] = online;

}

let faqLeaderBoard = _(".faq-lb");
let postLeaderBoard = _(".post-lb")


document.querySelectorAll('.community-nav-button').forEach(btn => {
    btn.addEventListener('click', function () {
        const section = this.getAttribute('data-section');
        showsection(section);
    });
});

//
//function renderLeaderBoards(reference, lbDivs) {
//    console.log(reference);
//    console.log(lbDivs)
//    reference.innerText = "";
//    lbDivs.forEach((div) => reference.appendChild(div));
//    console.log(reference);
//
//}


function renderLeaderBoards(reference, lbDivs) {
    if (!reference) {
        console.error("Error: Reference element is null or undefined");
        return;
    }

    console.log("Reference Element:", reference);
    console.log("Leaderboard Divs:", lbDivs);

   // lbDivs = lbDivs.filter(div => div && div instanceof HTMLElement); // Ensuring valid DOM elements

    reference.innerText = ""; // Clear previous content

    lbDivs.forEach((div) => {
        console.log("Appending:", div);
        reference.appendChild(div);
    });

    console.log("Updated Reference:", reference);
}
let communitySection = {
    chats: false,
    posts: false,
    faq: false
}

let communityNavs = {
    chats: communityNav.children[0],
    posts: communityNav.children[1],
    faq: communityNav.children[2],
}

let faqSectionFetching = false;


async function showsection(section) {
    log('showwww');
    if ((currentCommunitySection == section) && (section === 'posts') && postfetching == false) {
        document.getElementById('posts').innerText = "";
        log("refresh posts.....");

        currentPage = 0;
        postfetching = true;
        showSkeletonLoader(postLeaderBoard, 4, 'leaderboard-skeleton');
        showSkeletonLoader(postsContainer, 4, 'post-skeleton');
        // setTimeout(async()=>{


        let postSectionData = await postSection();
        console.log(postSectionData);

        // Render leaderboards
        renderLeaderBoards(postLeaderBoard, postSectionData.slice(0, 3)); // First 3 are leaderboards


        // Render posts
        postsContainer.innerText = "";
        let posts = postSectionData[3];


        if (postsContainer) {
            postsContainer.appendChild(posts); // Try appending
        } else {
            console.error('Cannot append. Either postsContainer missing or posts is empty');
        }

        postfetching = false;
        //},10000);

        return;
    } else if ((currentCommunitySection == section) && (section === 'faq') && faqSectionFetching == false) {
        document.getElementById('faqs').innerText = "";
        log("refresh faqs....");
        currentFaqPage = 0;




        showSkeletonLoader(faqLeaderBoard, 4, 'leaderboard-skeleton');
        showSkeletonLoader(faqsContainer, 4, 'post-skeleton');

        faqSectionFetching = true;
        let lbDivs = await faqSecion();
        console.log(lbDivs);

        renderLeaderBoards(faqLeaderBoard, lbDivs.slice(0, 4));

        let faqs = lbDivs[4];
        console.log(faqs instanceof DocumentFragment); // true
        console.log(Array.isArray(faqs)); // false
        faqsContainer.innerText = "";
        // Check faqsContainer
        console.log(faqsContainer);

        if (faqsContainer && faqs.childNodes.length > 0) {
            faqsContainer.appendChild(faqs); // Try appending
        } else {
            console.error('Cannot append. Either faqsContainer missing or faqs is empty');
        }
        faqSectionFetching = false;
        return;

    }
    if (currentCommunitySection == section) return;
    if (currentCommunitySection != null) communityNavs[currentCommunitySection].classList.remove('active');

    currentCommunitySection = section;
    document.getElementById('chats-section').style.display = section === 'chats' ? 'flex' : 'none';
    document.getElementById('posts-section').style.display = section === 'posts' ? 'flex' : 'none';
    document.getElementById('faq-section').style.display = section === 'faq' ? 'flex' : 'none';


    communityNavs[currentCommunitySection].classList.add('active');



    if (section === 'chats') {
        if (!communitySection[section]) {
            loadGroupsAndFriends();
            communitySection[section] = true;
        }

    } else if (section === 'posts') {
        if (!communitySection[section]) {

            showSkeletonLoader(postLeaderBoard, 4, 'leaderboard-skeleton');
            showSkeletonLoader(postsContainer, 4, 'post-skeleton');
            postfetching = true;
            let postSectionData = await postSection();
            console.log(postSectionData);

            // Render leaderboards
            renderLeaderBoards(postLeaderBoard, postSectionData.slice(0, 3)); // First 3 are leaderboards


            // Render posts
            postsContainer.innerText = "";
            let posts = postSectionData[3];


            if (postsContainer) {
                postsContainer.appendChild(posts); // Try appending
            } else {
                console.error('Cannot append. Either postsContainer missing or posts is empty');
            }
            postfetching = false;

            communitySection[section] = true;
        }


    } else if (section === 'faq') {
        if (!communitySection[section]) {
            console.log("faq sectionnnnnnnnnnnnnnn")
            showSkeletonLoader(faqLeaderBoard, 4, 'leaderboard-skeleton');
            showSkeletonLoader(faqsContainer, 4, 'post-skeleton');

            faqSectionFetching = true;
            let lbDivs = await faqSecion();
            console.log(lbDivs);

            renderLeaderBoards(faqLeaderBoard, lbDivs.slice(0, 4));

            let faqs = lbDivs[4];
            console.log(faqs instanceof DocumentFragment); // true
            console.log(Array.isArray(faqs)); // false
            faqsContainer.innerText = "";
            // Check faqsContainer
            console.log(faqsContainer);

            if (faqsContainer) {
                faqsContainer.appendChild(faqs); // Try appending
            } else {
                console.error('Cannot append. Either faqsContainer missing or faqs is empty');
            }
            faqSectionFetching = false;


            communitySection[section] = true;
        }
    }
}


async function faqSecion() {
    try {
        let faqCountLbData = await fetchFaqCountLeaderboard();
        let faqCountLbDiv = displayLeaderboard(faqCountLbData, "questions", "Top Minds");
        console.log(faqCountLbDiv);

        let treadingUsersFaqData = await fetchTrendingUsers();
        let treadingUsersFaqDiv = displayLeaderboard(treadingUsersFaqData, "points", "Trending Users");
         console.log(treadingUsersFaqDiv);

        let topContributorsFaqData = await fetchFaqLeaderboard("topContributors");
        let topContributorsFaqDiv = displayLeaderboard(topContributorsFaqData, "points", "Top Contributors");
		 console.log(topContributorsFaqDiv);
	 
        let mostlikedUsersFaqData = await fetchFaqLeaderboard("mostLiked");
        let mostlikedUsersFaqDiv = displayLeaderboard(mostlikedUsersFaqData, "points", "Most liked Users");
	 	console.log(topContributorsFaqDiv);
	 
        let faqs = await fetchFaqs();



        const allResults = await Promise.all([
            faqCountLbDiv,
            treadingUsersFaqDiv,
            topContributorsFaqDiv,
            mostlikedUsersFaqDiv,
            faqs
        ]);

        return allResults;
    } catch (error) {
        console.error(error.message);
        return [];
    }
}


//async function faqSecion() {
//    try {
//        let faqCountLbData = await fetchFaqCountLeaderboard();
//        let faqCountLbDiv = displayLeaderboard(faqCountLbData, "questions", "Top Minds");
//        console.log(faqCountLbDiv);
//
//        let trendingUsersFaqData = await fetchTrendingUsers();  // Fixed spelling
//        let trendingUsersFaqDiv = displayLeaderboard(trendingUsersFaqData, "points", "Trending Users");
//        console.log(trendingUsersFaqDiv);
//
//        let topContributorsFaqData = await fetchFaqLeaderboard("topContributors");
//        let topContributorsFaqDiv = displayLeaderboard(topContributorsFaqData, "points", "Top Contributors");
//        console.log(topContributorsFaqDiv);
//
//        let mostLikedUsersFaqData = await fetchFaqLeaderboard("mostLiked");
//        let mostLikedUsersFaqDiv = displayLeaderboard(mostLikedUsersFaqData, "points", "Most Liked Users"); // Fixed log issue
//        console.log(mostLikedUsersFaqDiv);
//
//        let faqs = await fetchFaqs();
//
//        return [
//            faqCountLbDiv,
//            trendingUsersFaqDiv,
//            topContributorsFaqDiv,
//            mostLikedUsersFaqDiv,
//            faqs
//        ];
//    } catch (error) {
//        console.error("Error in faqSecion:", error.message);
//        return [];
//    }
//}


async function postSection() {
    try {
        let postCountLbData = await fetchPostCountLeaderboard();
        let postCountLbDiv = displayLeaderboard(postCountLbData, "posts", "Top Post Creators");

        let trendingUsersPostData = await fetchtTrendingUsersPostSection();
        let trendingUsersPostDiv = displayLeaderboard(trendingUsersPostData, "points", "Trending Post Users");

        let mostLikedPostersData = await fetchMostLikedPosters();
        let mostLikedPostersDiv = displayLeaderboard(mostLikedPostersData, "likes", "Most Liked Posters");

        let posts = await fetchPosts();

        const allResults = await Promise.all([
            postCountLbDiv,
            trendingUsersPostDiv,
            mostLikedPostersDiv,
            posts
        ]);

        return allResults;
    } catch (error) {
        console.error(error.message);
        return [];
    }
}



async function fetchUserList() {
    try {
        const response = await fetch(`GetUserListServlet`);
        
        if(!checkSession(response)) return;
        const users = await response.json();

        let username;
        let id;


        users.forEach((user) => {
            container.appendChild(createUserItem(user));
            chatUsers[user.id] = user.name;
            usersMap[user.name] = user.id;
            friends[user.id] = user;

            log(friends);
        });
        return "completed";


    } catch (error) {
        console.error('Error fetching users:', error);
    }
}


function createUserItem(item) {
    log(item);
    const div = document.createElement('div');
    div.className = 'user-item';
    div.id = item.id;

    const avatarContainer = document.createElement('div');
    avatarContainer.className = 'avatar-container';

    const avatarDiv = document.createElement('div');
    avatarDiv.className = `avatar ${item.type === 'group' ? 'group-avatar' : ''}`;
    avatarDiv.style.backgroundColor = getRandomColor(item.name);
    let name = item.name;
    if (item.type === 'group') {
        avatarDiv.innerHTML = createGroupIcon(item.id);
    } else {
        avatarDiv.textContent = getInitials(item.name);

        const statusDot = document.createElement('div');
        statusDot.className = `status-dot ${item.online ? '' : 'status-offline'}`;
        avatarContainer.appendChild(statusDot);
    }
    avatarContainer.appendChild(avatarDiv);


    const infoDiv = document.createElement('div');
    infoDiv.className = 'user-info';
    infoDiv.id = 'info' + item.id;


    const nameP = document.createElement('p');

    nameP.className = 'user-name';
    nameP.textContent = item.name;
    nameP.id = 'name' + item.id;
    if (name.length > 14) {
        nameP.textContent = name.slice(0, 15) + '....';
    }

    const aboutP = document.createElement('p');
    aboutP.className = 'user-about';
    aboutP.textContent = item.about;
    aboutP.id = 'abou' + item.id;

    const unReadMessages = document.createElement('div');
    unReadMessages.classList.add('unread-messages', "dis_none");

    unReadMessages.id = 'nmsg' + item.id;


    infoDiv.appendChild(nameP);
    infoDiv.appendChild(aboutP);
    div.appendChild(avatarContainer);
    div.appendChild(infoDiv);
    div.appendChild(unReadMessages);

    return div;
}


const colors = [
    '#3B82F6', // blue
    '#10B981', // green
    '#8B5CF6', // purple
    '#EC4899', // pink
    '#6366F1', // indigo
    '#EF4444'  // red
];


function getInitials(name) {
    return name
        .split(' ')
        .map(word => word[0])
        .join('')
        .toUpperCase()
        .slice(0, 2);
}


function getRandomColor(name) {

    const index = Math.ceil(Math.random() * 5);
    return colors[index];
}


function createGroupIcon(id) {
    console.log(id + " " + "group");
    if (id == 'g1') {
        return "<img src='/kunto/uploads/group_dp1.jpg'/>";
    } else if (id == 'g2') {
        return "<img src='/kunto/uploads/group_dp2.jpg'/>";
    } else if (id == 'g3') {
        return "<img src='/kunto/uploads/group_db3.webp'/>";
    } else {
        return `<svg class="group-icon" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        		<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
   				 </svg>`;
    }

}



async function fetchUserId(name) {
    let res = await fetch(`getuserid?username=${name}`);
	if(!checkSession(res)) return;
    return await res.json();

}

async function init() {
    let response = await fetchUserName();
    console.log("username" +response);
    if(response){
		userName = response;
		
    	login();
    	showsection('chats');
    	communitySection['chats'] = true;
	}
    
    


}
async function fetchUserName() {
    let res = await fetch(`GetUserNameFromSession`);
	if(!checkSession(res)) return false;
	
    let json= await res.json();
    console.log(json);
    return json.userName;

}

init();

async function fetchGroupList() {
    try {
        const res = await fetch(`GetGroupList?`);
		if(!checkSession(res)) return;
        if (!res.ok) {
            throw new Error(`Error: ${res.status} ${res.statusText}`);
        }

        return await res.json();

    } catch (error) {
        console.error("Failed to fetch groups:", error);
        return null;
    }
}


async function loadGroupsAndFriends() {


    await fetchUserList();
    await fetchGroups();


}

async function fetchGroups() {
    const groups = await fetchGroupList();
    if (groups) {
        const container = document.getElementById('users-container');
        let groupname;
        let id;


        groups.forEach((group) => {
            container.appendChild(createUserItem(group));
            let id = group.id;
            myGroups[id] = group;

        });
    }

}

let welcomeCommunityTap = getId("welcome-community")
async function openChat(chatuserId) {
    if (currentreciever != null) {
        welcomeCommunityTap.classList.toggle('dis_none', true);
        welcomeCommunityTap.classList.toggle('dis_flex', false);
    }


    if (!sayHelloTap.classList.contains("dis_none")) {
        log("close sayhelloo")
        sayHelloTap.classList.add("dis_none");
        sayHelloTap.classList.remove("dis_flex");
    };
    if (friends["u" + chatuserId] != 0) {
        friends["u" + chatuserId]['unReadMessages'] = 0;
        let unReadMsgDiv = getId("nmsgu" + chatuserId);
        log(unReadMsgDiv);
        unReadMsgDiv.innerText = "";

        unReadMsgDiv.classList.add('dis_none');
        unReadMsgDiv.classList.remove('display_block');
    }

    try {
        log("hkgk")
       // log("userid : " + userId + " chatuserid : " + chatuserId);
        const response = await fetch('getChatHistory', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ chatUserId: chatuserId })
        });
       if(!checkSession(response)) return;
		
        const data = await response.json();
        log(data);
        log("hkgk");


        if (data.status === 'success') {
            log("datalen" + data.chatHistory.length);
            const chatMessages = document.getElementById('chat-messages');
            if (data.chatHistory.length == 0) {
                sayHelloTap.classList.remove("dis_none");
                sayHelloTap.classList.add("dis_flex");


            } else {

                chatMessages.innerText = "";
                data.chatHistory.map((msg) => {
                    displayMessage(msg, false);
                }

                );
                chatMessages.scrollTop = chatMessages.scrollHeight;
            }

        }
        
    } catch (error) {
        console.error('Error fetching chat history:', error);
    }
}


function changeReceiver() {
    if (currentreciever != null) {
        welcomeCommunityTap.classList.toggle('dis_none', true);
        welcomeCommunityTap.classList.toggle('dis_flex', false);
    }
    let receiverId = currentreciever.slice(1);
    let user;
    let name;
    if (currentreciever.slice(0, 1) == 'u') {
        user = getUserObj(currentreciever);
        name = user.name;
        receiverHeader.children[0].children[0].classList.remove("dis_none");
        receiverHeader.children[0].children[1].innerText = getInitials(user.name);
        receiverHeader.children[1].children[0].innerText = (name.length > 11) ? name.slice(0, 12) : name + "...";
        receiverHeader.children[1].children[1].innerText = user.about;
    } else {
        user = getGroupObj(currentreciever);
        name = user.name;
        log(user);
        receiverHeader.children[0].children[0].classList.add("dis_none");
        receiverHeader.children[0].children[1].innerHTML = createGroupIcon();
        receiverHeader.children[1].children[0].innerText = (name.length > 11) ? name.slice(0, 12) : name + "...";
        receiverHeader.children[1].children[1].innerText = "Fitness group";
    }

}

function getUserObj(userId) {
    return friends[userId];
}

function getGroupObj(groupId) {
    return myGroups[groupId];

}

async function openGroupChat(groupId) {

    if (!sayHelloTap.classList.contains("dis_none")) {
        log("close sayhelloo")
        sayHelloTap.classList.add("dis_none");
        sayHelloTap.classList.remove("dis_flex");
    };

    if (myGroups["g" + groupId] != 0) {
        myGroups["g" + groupId]['unReadMessages'] = 0;
        let unReadMsgDiv = getId("nmsgg" + groupId);
        log(unReadMsgDiv);
        unReadMsgDiv.innerText = "";

        unReadMsgDiv.classList.add('dis_none');
        unReadMsgDiv.classList.remove('display_block');
    }

    try {
        log("hkgk")
        const response = await fetch('getGroupHistory', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ groupId: groupId })
        });
        if(!checkSession(response)) return;
        const data = await response.json();
        log(data);
        log("hkgk");

        if (data.status === 'success') {

            const chatMessages = document.getElementById('chat-messages');
            chatMessages.innerText = "";
            data.chatHistory.map((msg) => {
                displayMessage(msg, true);
            }

            );
            chatMessages.scrollTop = chatMessages.scrollHeight;
        }
    } catch (error) {
        console.error('Error fetching chat history:', error);
    }
}

document.getElementById("users-container").addEventListener('click', (ev) => {
    let id = ev.target.id;
    log(id);

    log(currentreciever);
    let idNum;
    if (id != "users-container") {
        if (id.slice(0, 1) == 'u' || id.slice(0, 1) == 'g') {
            idNum = id;
        }
        else {
            idNum = id.slice(4); document.getElementById("users-container").addEventListener('click', (ev) => {
                let id = ev.target.id;
                log(id);

                log(currentreciever);
                let idNum;
                if (id != "users-container") {
                    if (id.slice(0, 1) == 'u' || id.slice(0, 1) == 'g') {
                        idNum = id;
                    }
                    else {
                        idNum = id.slice(4);

                    }
                    currentreciever = idNum;
                    log(currentreciever);
                    openChatWindow(idNum);

                }


            })

        }
        currentreciever = idNum;
        log(currentreciever);
        changeReceiver();
        openChatWindow(idNum);

    }


})

document.getElementById("agf-users-list").addEventListener('click', (ev) => {
    let id = ev.target.id;
    log(id);

    log(currentreciever);
    let idNum;
    if (id != "users-container") {
        if (id.slice(0, 1) == 'u' || id.slice(0, 1) == 'g') {
            idNum = id;
        }
        else {
            idNum = id.slice(4);

        }
        let userObj = globalUsers[`${idNum}`];
        friends[`${idNum}`] = userObj;
        let name = userObj["name"];
        chatUsers[idNum] = name;
        usersMap[name] = idNum
        currentreciever = idNum;
        log(currentreciever);
        changeReceiver();
        openChatWindow(idNum);

    }


})

function openChatWindow(id) {
    if (id.slice(0, 1) == 'u') {
        openChat(id.slice(1));
    }
    else {
        openGroupChat(id.slice(1))
    }
}











function displayMessage(msg, isGroupChat) {
    msg.senderUsername = (msg.senderUsername === userName) ? 'you' : msg.senderUsername;

    const messageDiv = document.createElement('div');
    messageDiv.classList.add('message');
    if (msg.senderUsername === 'you') {
        messageDiv.classList.add('sent');
    } else {
        messageDiv.classList.add('received');
    }

    const messageContent = document.createElement('div');
    messageContent.classList.add('message-content');

    // Date Separator
    const msgDate = new Date(msg.timestamp).toDateString();
    if (lastMessageDate !== msgDate) {
        const dateSeparator = document.createElement('div');
        dateSeparator.classList.add('date-separator');

        const dateBox = document.createElement('div');
        dateBox.classList.add('date-box');
        dateBox.textContent = msgDate;

        dateSeparator.appendChild(dateBox);
        messagesDiv.appendChild(dateSeparator);
        lastMessageDate = msgDate;
    }

    // Message Header for Group Chats
    if (isGroupChat && msg.senderUsername !== 'you') {
        const authorSpan = document.createElement('span');
        authorSpan.classList.add('message-author');
        authorSpan.textContent = msg.senderUsername;
        messageDiv.appendChild(authorSpan);
    }

    // Message Text
    if (msg.message) {
        const messageText = document.createElement('p');
        messageText.textContent = msg.message;
        messageContent.appendChild(messageText);
    }

    let extraDiv = document.createElement("div");

    extraDiv.className = "extra";
    // Image Handling
    if (msg.image && msg.image !== 'no-image') {
        const imageContainer = document.createElement('div');
        imageContainer.classList.add('message-image-container');

        const image = document.createElement('img');
        image.src = msg.image;
        image.alt = 'Image';
        image.classList.add('message-image');

        const downloadIcon = document.createElement('div');
        downloadIcon.classList.add('download-icon');
        downloadIcon.innerHTML = `<i class="fa-solid fa-download"></i>`;
        downloadIcon.onclick = function () {
            downloadImage(msg.image);
        };

        imageContainer.appendChild(image);
        imageContainer.appendChild(downloadIcon);
        //messageContent.appendChild(imageContainer);
        extraDiv.appendChild(imageContainer);
    }


    // Time (moved inside message content)
    const timeSpan = document.createElement('span');
    timeSpan.classList.add('message-time');
    timeSpan.textContent = new Date(msg.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    messageContent.appendChild(timeSpan);

    if (msg.image && msg.image !== 'no-image') {
        extraDiv.appendChild(messageContent);
        messageDiv.appendChild(extraDiv);

    }
    else {
        messageDiv.appendChild(messageContent);
    }

    messagesDiv.appendChild(messageDiv);

    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}




// Function to handle image download
function downloadImage(imageUrl) {
    const link = document.createElement('a');
    link.href = imageUrl;
    link.download = imageUrl.split('/').pop();  // Extract image name from URL
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}




function sendPrivateMessagetoServer(msg, imageUrl) {
    log("private");
    log(msg);
    const message = {
        type: "PRIVATE",
        target: friends[currentreciever].name,
        message: msg,
        imageUrl: imageUrl
    };
    log(message);
    if (websocket && websocket.readyState === WebSocket.OPEN) {
        websocket.send(JSON.stringify(message));

    }
}


function sendGroupMessagetoServer(msg, imageUrl) {
    log("group");
    log(msg);
    const message = {
        type: "GROUP",
        target: parseInt(currentreciever.slice(1)),
        message: msg,
        imageUrl: imageUrl
    };
    log(message);
    if (websocket && websocket.readyState === WebSocket.OPEN) {
        websocket.send(JSON.stringify(message));

    }
}



sendMessageBtn.addEventListener('click', async () => {

    if (!currentreciever) {
        dispPopup('choose you friend or group....')
        return;
    }
    let msg = sendMessageInput.value.trim();
    log(sendMessageInput.value.trim());
    if (msg.length > 1000) {
        dispPopup("Please keep your messages under 1000 characters...");
        sendMessageInput.value = "";
        return;
    }


    if (msg || imageUpload) {
        sendmessage(msg);

    } else {
        console.warn("Cannot send an empty message.");
    }
});


async function sendmessage(messageInput) {

    log(messageInput);
    const now = new Date();
    const customFormat = `${now.getFullYear()}-${now.getMonth() + 1}-${now.getDate()} ${now.getHours()}:${now.getMinutes()}:${now.getSeconds()}`;
    let mmsg = {
        senderUsername: "you",
        message: messageInput,
        timestamp: customFormat

    }

    let image = await uploadImagetoServer(fileInput);
    
    log("image");
    log(image);

    if (image && image.imageUrl) {
        mmsg["image"] = image.imageUrl;
    }

    displayMessage(mmsg);


    let imageUrl = image ? image.imageUrl : "no-image";
    if (currentreciever.slice(0, 1) == 'u') {
        log('vvvvvvvvvvvvvvv')
        sendPrivateMessagetoServer(messageInput, imageUrl);
        try {

            let msg = {
                
                receiverId: currentreciever.slice(1),
                message: messageInput,
                image: image ? image.imageUrl : "no-image"
            };

            log(msg);
            await fetch('sendChatMessage', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(msg)
            });


        } catch (error) {
            console.error('Error sending message:', error);
        }

    } else {
        sendGroupMessagetoServer(messageInput, imageUrl);
        try {
            let msg = {
               
                groupId: currentreciever.slice(1),
                message: messageInput,
                image: image ? image.imageUrl : "no-image"
            };

            log(msg);
            fetch('SendGroupMessage', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(msg)
            });



        } catch (error) {
            console.error('Error sending message:', error);
        }
    }
    imgPreview.classList.add('dis_none');
    imgPreview.classList.remove('dis_flex');

    imageUpload = false;
    fileBtn.disabled = false;
    fileInput.value = "";
    sendMessageInput.value = "";




}


function createUserMeta(name, createdAt) {

    const postHeader = document.createElement('div');
    postHeader.className = 'post-header';

    const avatar = document.createElement('div');
    avatar.className = 'user-avatar';
    avatar.textContent = name.charAt(0).toUpperCase();

    const postMeta = document.createElement('div');
    postMeta.className = 'post-meta';

    const username = document.createElement('div');
    username.className = 'username';
    username.textContent = name;

    const postTime = document.createElement('div');
    postTime.className = 'post-time';
    postTime.textContent = new Date(createdAt).toLocaleDateString('en-US', {
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });

    postMeta.appendChild(username);
    postMeta.appendChild(postTime);

    postHeader.appendChild(avatar);
    postHeader.appendChild(postMeta);
    return postHeader;
}


async function createLikeActions(id, type) {
    const actionsDiv = document.createElement('div');
    actionsDiv.className = 'post-actions';

    const likeButton = document.createElement('button');
    const likeCount = document.createElement('span');
    likeButton.className = 'action-button';
    likeButton.innerHTML = '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3zM7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3"></path></svg>';

    // Fetch like history and update the like count
    const likeHistory = await fetchLikeHistory(id, type);
    likeCount.innerText = likeHistory.totalLikes;

    if (likeHistory.userLiked) {
        likeButton.classList.add('activelike');
    }

    likeButton.onclick = () => handleLike(id, likeButton, type);

    actionsDiv.appendChild(likeButton);
    actionsDiv.appendChild(likeCount);

    return { actionsDiv: actionsDiv };
}

async function addCommentOrAnswer(id, type, text) {
    if (text.value.trim()) {

        let json = {
            id: id,
            userId: userId,
            reply: text.value.trim()

        }
        try {
            const response = await fetch(type === 'FAQ' ? 'answerFAQ' : 'addComment', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(json)
            });
            if(!checkSession(response)) return;
            const data = await response.json();
            log(`Adding ${type} comment/answer`);
            text.value = "";
        } catch (error) {
            console.error(`Error submitting ${type} comment/answer:`, error);
        }
    }
}



postsContainer.addEventListener('click', async function (ev) {
    const target = ev.target;

    // Handle "Show More" button
    const showMoreBtn = target.closest('.show-more-btn');
    if (showMoreBtn) {
        const postText = showMoreBtn.previousElementSibling;
        const arrowIcon = showMoreBtn.querySelector('.arrow');

        postText.classList.toggle('expanded');
        arrowIcon.classList.toggle('down');
        arrowIcon.classList.toggle('up');
    }

    // Handle Like button
//    const likeBtn = target.closest('.like-btn');
//    if (likeBtn) {
//        const postId = likeBtn.closest('.post-card').dataset.postId;
//        handleLike(postId, likeBtn, 'Post');
//    }

    // Handle Comment button
    const commentBtn = target.closest('.comment-btn');
    if (commentBtn) {
        const postId = commentBtn.closest('.post-card').dataset.postId;
         const comments = await fetchComments(postId);
        displayComments(postId , comments);
    }

    // Handle Post Comment button
    const postCommentBtn = target.closest('.post-comment-btn');
    if (postCommentBtn) {
        const postId = postCommentBtn.closest('.post-card').dataset.postId;
        const commentInput = postCommentBtn.previousElementSibling;
        addCommentOrAnswer(postId, 'POST' ,commentInput);
    }
});


async function createPostUI(post) {
	console.log
    // Create the post card container
    const postCard = document.createElement('div');
    postCard.className = 'post-card';
    postCard.dataset.postId = post.id;

    // Create the post header
    const postHeader = document.createElement('div');
    postHeader.className = 'post-header';

    const postMeta = createUserMeta(post.username, post.createdAt);
    postHeader.appendChild(postMeta);

    const postType = document.createElement('div');
    postType.className = 'post-type';
    postType.textContent = 'has started a post.';

    // Create the post title
    const postTitle = document.createElement('div');
    postTitle.className = 'post-title';
    postTitle.textContent = (post.title == '') ? 'Untitled Discussion' : post.title;

    // Create the post image (if available)
    const imgDiv = document.createElement('div');
    const img = document.createElement('img');
    if (post.imageUrl && post.imageUrl != "no-image") {
        img.src = post.imageUrl;
        img.alt = 'Post Image';
        imgDiv.className = 'post-image';
    }

    // Create the post content with "Show More" feature
    const postContent = document.createElement('div');
    postContent.className = 'post-content';

    const postText = document.createElement('p');
    postText.className = 'post-text';
    postText.textContent = post.content;
	postContent.appendChild(postText);
	
	
	if(post.content.length >= 300){
		const showMoreBtn = document.createElement('div');
    	showMoreBtn.className = 'show-more-btn';
    	showMoreBtn.innerHTML = '<i class="arrow down"></i>';
    	postContent.appendChild(showMoreBtn);
	}
    

   
    

    // Create like actions (like button and like count)
    const actions = await createLikeActions(post.id, 'Post');
    const actionsDiv = actions.actionsDiv;

    // Create the comment button
    const commentButton = document.createElement('button');
    commentButton.className = 'comment-btn action-button';
    commentButton.innerHTML = '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"></path></svg> Comment';

    // Create the views stat
    const stats = document.createElement('div');
    stats.className = 'post-stats';

    const viewsStat = document.createElement('div');
    viewsStat.className = 'stat';
    viewsStat.innerHTML = '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle></svg>';
    viewsStat.appendChild(document.createTextNode(post.views || '0'));

    stats.appendChild(viewsStat);
    actionsDiv.appendChild(commentButton);
    actionsDiv.appendChild(stats);

    // Create the comments section
    const commentsSection = document.createElement('div');
    commentsSection.id = `comments-${post.id}`;
    commentsSection.className = 'comments-section';
    commentsSection.style.display = 'none';

    // Create the comment input container
    const commentInputContainer = document.createElement('div');
    commentInputContainer.className = 'comment-input-container';

    const commentInput = document.createElement('input');
    commentInput.type = 'text';
    commentInput.className = 'comment-input';
    commentInput.placeholder = 'Add your comment...';
    commentInput.id = `comment-input-${post.id}`;

    const postCommentBtn = document.createElement('i');
    postCommentBtn.className = 'post-comment-btn fas fa-paper-plane';

    commentInputContainer.appendChild(commentInput);
    commentInputContainer.appendChild(postCommentBtn);

    // Append all elements to the post card
    postCard.appendChild(postHeader);
    postCard.appendChild(postTitle);
    if (post.imageUrl && post.imageUrl != "no-image") {
        imgDiv.appendChild(img);
        postCard.appendChild(imgDiv);
    }

    postCard.appendChild(postContent);
    postCard.appendChild(actionsDiv);
    postCard.appendChild(commentsSection);
    postCard.appendChild(commentInputContainer);

    // Observe the post card for lazy loading or other purposes
    postObserver.observe(postCard);

    return postCard;
}



const viewedPosts = new Set();

const postObserver = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            const postCard = entry.target;
            const postId = entry.target.getAttribute('data-post-id');
            if (!viewedPosts.has(postId)) {
                markPostAsViewed(postId);
                viewedPosts.add(postId);
                observer.unobserve(postCard);
            }
        }
    });
}, { threshold: 0.5 });

function markPostAsViewed(postId) {
    let json = {
        post_id: postId,
       
    }
    fetch('viewPost', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(json)
    })
        .then(response => {
			if(!checkSession(response)) return;
			return response.json();
		})
        .then(data => {
            const viewsStat = document.querySelector(`[data-post-id="${postId}"] .stat`);
            console.log(viewsStat);
            viewsStat.dataset.views = data.views;
            viewsStat.innerHTML = `
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                <circle cx="12" cy="12" r="3"></circle>
            </svg> ${data.views}
        `;
        })
        .catch(err => console.error('Error updating view count:', err));
}

const viewedFAQs = new Set();

const faqObserver = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            const faqCard = entry.target;
            const faqId = faqCard.getAttribute('data-faq-id');

            if (!viewedFAQs.has(faqId)) {
                markFAQAsViewed(faqId);
                viewedFAQs.add(faqId);
                faqObserver.unobserve(faqCard);
            }
        }
    });
}, { threshold: 0.5 }); // 50% visibility to trigger

function markFAQAsViewed(faqId) {
    const json = {
        faq_id: faqId,
         
    };

    fetch('viewFAQ', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(json)
    })
        .then(response => {
			if(!checkSession(response)) return;
			return response.json();
        })
        .then(data => {
            console.log(data);
            const viewsStat = document.querySelector(`[data-faq-id="${faqId}"] .faq-views`);
            console.log(viewsStat);
            viewsStat.dataset.views = data.views;
            viewsStat.innerHTML = `
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                <circle cx="12" cy="12" r="3"></circle>
            </svg> ${data.viewCount}
        `;
        })
        .catch(err => console.error('Error updating FAQ view count:', err));
}




faqsContainer.addEventListener('click', async function (ev) {
    const target = ev.target;

    // Handle "Show More" button
    const showMoreBtn = target.closest('.show-more-btn');
    if (showMoreBtn) {
        const faqText = showMoreBtn.previousElementSibling;
        const arrowIcon = showMoreBtn.querySelector('.arrow');

        faqText.classList.toggle('expanded');
        arrowIcon.classList.toggle('down');
        arrowIcon.classList.toggle('up');
    }

    // Handle Answer button
    const answerBtn = target.closest('.answer-btn');
    if (answerBtn) {
        const faqId = answerBtn.closest('.faq-card').dataset.faqId;
        await fetchAnswers(faqId); // Your answer handling function
    }

    // Handle Post Answer button
    const postCommentBtn = target.closest('.post-comment-btn');
    if (postCommentBtn) {
        const faqId = postCommentBtn.closest('.faq-card').dataset.faqId;
        const commentInput = postCommentBtn.previousElementSibling;
       addCommentOrAnswer(faqId, 'FAQ',commentInput); // Your post answer handling function
    }
});


async function createFAQUI(faq) {
    const postCard = document.createElement('div');
    postCard.classList.add('post-card', 'faq-card');
    postCard.dataset.faqId = faq.id;

    const postHeader = document.createElement('div');
    postHeader.className = 'post-header';

    const postMeta = createUserMeta(faq.username, faq.createdAt);
    postHeader.appendChild(postMeta);

    const postTitle = document.createElement('div');
    postTitle.className = 'post-title';
    postTitle.textContent = (faq.title == '') ? 'Untitled Question' : faq.title;

    // FAQ Content with "Show More" Feature
    const postContent = document.createElement('div');
    postContent.className = 'post-content';

    const faqText = document.createElement('p');
    faqText.className = 'post-text';
    faqText.textContent = faq.questionText;
    postContent.appendChild(faqText);
    
    if(faq.questionText.length >=300){
		const showMoreBtn = document.createElement('div');
    	showMoreBtn.className = 'show-more-btn';
    	showMoreBtn.innerHTML = '<i class="arrow down"></i>';
    	postContent.appendChild(showMoreBtn);

	}

    
    
    

    const actions = await createLikeActions(faq.id, userId, 'FAQ');
    let actionsDiv = actions.actionsDiv;

    const stats = document.createElement('div');
    stats.className = 'post-stats';

    const viewsStat = document.createElement('div');
    viewsStat.className = 'faq-views stat';
    viewsStat.innerHTML = '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle></svg>';
    viewsStat.appendChild(document.createTextNode(faq.views || '0'));

    stats.appendChild(viewsStat);
    actionsDiv.appendChild(stats);

    const answerButton = document.createElement('button');
    answerButton.className = 'action-button answer-btn';
    answerButton.innerHTML = '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"></path></svg> Answer';

    actionsDiv.appendChild(answerButton);

    const commentsSection = document.createElement('div');
    commentsSection.id = `answers-${faq.id}`;
    commentsSection.className = 'comments-section';
    commentsSection.style.display = 'none';

    const commentInputContainer = document.createElement('div');
    commentInputContainer.className = 'comment-input-container';

    const commentInput = document.createElement('input');
    commentInput.type = 'text';
    commentInput.className = 'comment-input';
    commentInput.placeholder = 'Add your answer...';
    commentInput.id = `comment-input-${faq.id}`;

    const postCommentBtn = document.createElement('i');
    postCommentBtn.classList.add('fas', 'fa-paper-plane', 'post-comment-btn');

    commentInputContainer.appendChild(commentInput);
    commentInputContainer.appendChild(postCommentBtn);

    postCard.appendChild(postHeader);
    postCard.appendChild(postTitle);
    postCard.appendChild(postContent);
    postCard.appendChild(actionsDiv);
    postCard.appendChild(commentsSection);
    postCard.appendChild(commentInputContainer);

    faqObserver.observe(postCard);
    return postCard;
}






const observer = new IntersectionObserver(async (entries) => {
    const lastPost = entries[0];
    if (lastPost.isIntersecting && !postfetching) {
        postfetching = true;
        // Once the last post is in view, load more posts
        showLoading();

        // await setTimeout(async ()=>{
        let posts = await fetchPosts();
        postsContainer.appendChild(posts);
        hideLoading();
        postfetching = false;
        //},8000)// This will fetch and display posts


    }
}, {
    root: null,
    rootMargin: '0px',
    threshold: 1.0
});
let faqFetching = false;
const observer1 = new IntersectionObserver(async (entries) => {
    const lastFaq = entries[0];
    if (lastFaq.isIntersecting && !faqFetching) {
        faqFetching = true;
        // Once the last post is in view, load more posts
        showLoading();

        //        await setTimeout(async () => {
        let faqs = await fetchFaqs();
        faqsContainer.appendChild(faqs);
        hideLoading();
        faqFetching = false;
        //        }, 4000)// This will fetch and display posts


    }
}, {
    root: null,
    rootMargin: '0px',
    threshold: 1.0
});


function showLoading() {
    log("show loading....")
    const loading = document.createElement('div');
    loading.id = 'loading';

    // Create a spinner element
    const spinner = document.createElement('div');
    spinner.classList.add('spinner');
    loading.appendChild(spinner);
    if (currentCommunitySection == 'posts') {
        document.getElementById('posts').appendChild(loading);
    } else {
        document.getElementById('faqs').appendChild(loading);
    }

}

function hideLoading() {
    log("hide loading....")
    const loading = document.getElementById('loading');
    if (loading) loading.remove();
}

let postfetching = false;


async function displayFAQs(faqs) {
    log("display faqs..");
    const fragment = document.createDocumentFragment();

    const previousFaqEnd = document.querySelector('.faq-end');
    if (previousFaqEnd) {
        observer1.unobserve(previousFaqEnd);
        previousFaqEnd.remove();
    }

    for (let index = 0; index < faqs.length; index++) {
        const faq = faqs[index];
        const faqUI = await createFAQUI(faq);  // Await properly here
        fragment.appendChild(faqUI);

        if (index === faqs.length - 1) {
            const faqEnd = document.createElement('div');
            faqEnd.classList.add('faq-end');
            fragment.appendChild(faqEnd);

            observer1.observe(faqEnd);
        }
    }

    return fragment;
}
async function displayPosts(posts) {
    const fragment = document.createDocumentFragment();

    const previousPostEnd = document.querySelector('.post-end');
    if (previousPostEnd) {
        observer.unobserve(previousPostEnd);
        previousPostEnd.remove();
    }

    for (let index = 0; index < posts.length; index++) {
        const post = posts[index];
        const postUI = await createPostUI(post);  // Properly await here
        postsContainer.append(postUI);

        if (index === posts.length - 1) {
            const postEnd = document.createElement('div');
            postEnd.classList.add('post-end');
            fragment.appendChild(postEnd);

            observer.observe(postEnd);
        }
    }
    return fragment;
}



async function fetchPosts() {
    const response = await fetch(`getPosts?from=${currentPage * postsPerPage}&to=${(currentPage + 1) * postsPerPage}`);
    const data = await response.json();
    currentPage++;


    return displayPosts(data.posts);
}


async function fetchFaqs() {

    const response = await fetch(`getFAQ?from=${currentFaqPage * postsPerPage}&to=${(currentFaqPage + 1) * postsPerPage}`);
    if(!checkSession(response)) return;
    const data = await response.json();
    log(data);
    currentFaqPage++;


    return displayFAQs(data.faqs);
}


const lastPost = document.querySelector('.post-end');
if (lastPost) observer.observe(lastPost);
const lastFaq = document.querySelector('.faq-end');
if (lastFaq) observer1.observe(lastFaq);


async function fetchComments(postId) {
    try {
        const response = await fetch('getComments', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ postId })
        });
        if(!checkSession(response)) return;
        return await response.json();
    } catch (error) {
        console.error('Error fetching comments:', error);
        return [];
    }
}


function displayComments(postId, comments) {
    log('displaying comments............')
    const commentsSection = document.getElementById(`comments-${postId}`);
    commentsSection.innerHTML = '';

    if (Array.isArray(comments.comments)) {
        if (comments.comments.length == 0) {
            const commentDiv = document.createElement('div');
            commentDiv.className = 'comment';
            commentDiv.innerText = "no comments yet...."
            commentsSection.appendChild(commentDiv);
        } else {
            comments.comments.forEach(comment => {
                log(comment);
                const commentDiv = document.createElement('div');
                commentDiv.className = 'comment';

                const avatar = document.createElement('div');
                avatar.className = 'user-avatar';
                avatar.textContent = comment.username.charAt(0).toUpperCase();

                const commentContent = document.createElement('div');
                commentContent.className = 'comment-content';

                const commentHeader = document.createElement('div');
                commentHeader.className = 'comment-header';

                const username = document.createElement('span');
                username.className = 'username';
                username.textContent = comment.username;

                const commentTime = document.createElement('span');
                commentTime.className = 'post-time';
                commentTime.textContent = new Date(comment.createdAt).toLocaleDateString('en-US', {
                    month: 'short',
                    day: 'numeric',
                    hour: '2-digit',
                    minute: '2-digit'
                });


                commentHeader.appendChild(username);
                commentHeader.appendChild(commentTime);

                const commentText = document.createElement('div');
                commentText.className = 'comment-text';
                commentText.textContent = comment.comment;

                const commentActions = document.createElement('div');
                commentActions.className = 'comment-actions';

                const likeAction = document.createElement('span');
                likeAction.className = 'comment-action';
                likeAction.textContent = 'Like';

                const replyAction = document.createElement('span');
                replyAction.className = 'comment-action';
                replyAction.textContent = 'Reply';

                commentActions.appendChild(likeAction);
                commentActions.appendChild(replyAction);

                commentContent.appendChild(commentHeader);
                commentContent.appendChild(commentText);
                commentContent.appendChild(commentActions);

                commentDiv.appendChild(avatar);
                commentDiv.appendChild(commentContent);

                commentsSection.appendChild(commentDiv);
            });
        }
    }

    commentsSection.style.display = commentsSection.style.display === 'none' ? 'block' : 'none';
}




async function fetchAnswers(questionId) {

    const response = await fetch(`getAnswers?faqId=${questionId}`);
    if(!checkSession(response)) return;
    const data = await response.json();
    log(data);
    return await displayAnswers(questionId, data);

}



function displayAnswers(questionId, answers) {
    log("display Answers...");
    const answersSection = document.getElementById(`answers-${questionId}`);
    answersSection.innerHTML = '';

    log(answersSection);
    if (Array.isArray(answers.answers)) {
        if (answers.answers.length == 0) {
            const answerDiv = document.createElement('div');
            answerDiv.className = 'comment';
            answerDiv.innerText = "No answers yet......"
            answersSection.appendChild(answerDiv);
        } else {
            answers.answers.forEach(answer => {

                log(answer.userLiked);
                log(answer);

                const answerDiv = document.createElement('div');
                answerDiv.className = 'comment';

                const avatar = document.createElement('div');
                avatar.className = 'user-avatar';
                avatar.textContent = answer.username.charAt(0).toUpperCase();

                const answerContent = document.createElement('div');
                answerContent.className = 'comment-content';

                const answerHeader = document.createElement('div');
                answerHeader.className = 'comment-header';

                const username = document.createElement('span');
                username.className = 'username';
                username.textContent = answer.username;

                const answerTime = document.createElement('span');
                answerTime.className = 'post-time';
                answerTime.textContent = new Date(answer.createdAt).toLocaleDateString('en-US', {
                    month: 'short',
                    day: 'numeric',
                    hour: '2-digit',
                    minute: '2-digit'
                });

                answerHeader.appendChild(username);
                answerHeader.appendChild(answerTime);

                const answerText = document.createElement('div');
                answerText.className = 'comment-text';
                answerText.textContent = answer.answer;



                const likeContainer = document.createElement('div');
                likeContainer.className = 'like-container';

                const likeIcon = document.createElement('span');

                const likeButton = document.createElement('button');
                likeButton.className = 'action-button';
                likeButton.innerHTML = '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3zM7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3"></path></svg>';
                if (answer.userLiked) {
                    likeButton.classList.add('activelike');
                }


                const likeCount = document.createElement('span');
                likeCount.className = 'like-count';
                likeCount.textContent = answer.totalLikes;

                likeContainer.appendChild(likeButton);
                likeContainer.appendChild(likeCount);

                likeButton.addEventListener('click', () => toggleAnswerLike(answer.answerId, likeButton, likeCount));

                answerContent.appendChild(answerHeader);
                answerContent.appendChild(answerText);
                answerContent.appendChild(likeContainer);

                answerDiv.appendChild(avatar);
                answerDiv.appendChild(answerContent);

                log(answerDiv);

                answersSection.appendChild(answerDiv);
            });
        }
    }

    answersSection.style.display = answersSection.style.display === 'none' ? 'block' : 'none';
}



function toggleAnswerLike(answerId, likeIcon, likeCount) {

    log("toggle like.." + answerId)
    let json = {
        answerId: answerId,
        
    };
    fetch('toggleAnswerLike', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(json)
    })
        .then(response => {
			if(!checkSession(response)) return;
			return response.json()
			})
        .then(data => {
            if (data.success) {


                likeIcon.classList.toggle('activelike', data.liked);

                updateAnswerLikeCount(likeIcon, data.totalLikes)
            }
        })
        .catch(error => console.error('Error:', error));
}

function updateAnswerLikeCount(likeIcon, c) {
    log(likeIcon);
    likeIcon.nextElementSibling.innerText = c;
}



async function fetchLikeHistory(id, type) {
    log("getLikes");
    try {
        const response = await fetch(`${type === 'faq' ? 'getFaqLike' : 'getLike'}?postId=${id}`);
        if(!checkSession(response)) return;
        const resp = await response.json();
        return resp;
    } catch (error) {
        console.error('Error fetching likes:', error);
        return { totalLikes: 0, userLiked: false };
    }
}

async function handleLike(id, likeButton, type) {
    try {
        let json = { postId: id }
        const response = await fetch(type === 'FAQ' ? 'likeQuestion' : 'like', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(json)
        });
        if(!checkSession(response)) return;
        const data = await response.json();
        log(data)
        if (data.liked) {
            updateLikeCount(likeButton, 1);
            likeButton.classList.add('activelike');
            dispPopup(`${type} like added successfully`);
        } else {
            updateLikeCount(likeButton, -1);
            likeButton.classList.remove('activelike');
            dispPopup(`${type} like removed successfully`);
        }
    } catch (error) {
        console.error('Error handling like:', error);
    }
}



function updateLikeCount(likeButton, count) {
    likeButton.nextElementSibling.innerText = parseInt(likeButton.nextElementSibling.innerText) + count;
}







sayHelloBtn.addEventListener("click", async () => {
    const chatMessages = document.getElementById('chat-messages');
    chatMessages.innerText = "";
    await sendmessage("hello dude.....");
    await addFriend();
    //sendmsg
    //add friend db
    log("sayHello");
    sayHelloTap.classList.add("dis_none");
    sayHelloTap.classList.remove("dis_flex");

});


async function addFriend() {
    let res = await fetch("addFriend", {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            
            friendId: currentreciever.slice(1)
        })
    });
    if(!checkSession(res)) return;
    await res.json();
    //return await res.json();
    container.prepend(createUserItem(friends[currentreciever]));
}





plusFriend.addEventListener("click", () => {
    log("plus");
    FRIEND_SUGGEST.classList.toggle("agf-container-open");
    plusFriend.classList.toggle("rotate-plus-btn");


})





searchButton.addEventListener('click', () => {
    createGroup.classList.add('dis_none');
    agfSearchInput.classList.add('display_block');
    appContainer.classList.add('agf-search-mode');
    headerTitle.textContent = 'Search';
    agfSearchInput.querySelector('input').focus();
    agfMode = 'quickchat';
});


backButton.addEventListener('click', () => {
    log(agfMode)
    if (agfMode == 'initial') {

        FRIEND_SUGGEST.classList.remove("agf-container-open");
        plusFriend.classList.toggle("rotate-plus-btn");
        usersList.innerText = "";

    } else if (agfMode == 'quickchat') {
        createGroup.classList.remove('dis_none');
        agfSearchInput.classList.remove('display_block');
        appContainer.classList.remove('agf-search-mode');
        headerTitle.textContent = 'Quick Chat';
        agfSearchInput.querySelector('input').value = '';

        agfMode = 'initial';
    } else if (agfMode == 'creategroup') {
        log("craete-group to quickchat")
        createGroup.classList.remove('dis_none');
        appContainer.classList.remove('agf-search-mode');
        agfSearchInput.classList.remove('display_block');
        agfSearchInput.querySelector('input').value = '';
        headerTitle.textContent = 'Quick Chat';
        selectedUsersDiv.children[0].innerText = "";
        selectedUsers.length = 0;
        selectedUsersDiv.classList.remove("dis_flex");
        selectedUsersDiv.classList.add("dis_none");
        selectedUsersDiv.children[0].classList.add("dis_none")
        selectedUsersDiv.children[0].classList.remove("dis_flex")

        continueButton.classList.remove("dis_flex");
        continueButton.children[0].classList.add("dis_none")
        continueButton.children[0].children[0].classList.add("dis_none")
        continueButton.disabled = true;
        usersList.innerText = "";
        agfMode = 'initial';


    } else {
        confirmationPage.classList.remove('dis_flex');

        confirmationUsersList.classList.remove('dis_flex');
        confirmationUsersList.classList.add('dis_none');
        agfSearchInput.classList.add('display_block');
        appContainer.classList.add('agf-search-mode');
        headerTitle.textContent = 'Quick Chat';
        usersList.classList.add('dis_flex');
        usersList.classList.add('dis_grid');
        usersList.classList.remove('dis_none');
        selectedUsersDiv.classList.add("dis_flex");
        selectedUsersDiv.classList.remove("dis_none");
        selectedUsersDiv.children[0].classList.add("dis_flex");
        selectedUsersDiv.children[0].classList.remove("dis_none");
        continueButton.classList.add("dis_flex");
        continueButton.children[0].classList.remove("dis_none")
        continueButton.children[0].children[0].classList.remove("dis_none");
        usersList.classList.add('dis_none');
        searchButton.classList.remove("dis_none");


        agfMode = 'creategroup';
    }

});







createGroup.addEventListener('click', () => {
    createGroup.classList.add('dis_none');
    agfSearchInput.classList.add('display_block');
    appContainer.classList.add('agf-search-mode');
    //         headerTitle.textContent = 'Search';
    agfSearchInput.querySelector('input').focus();
    selectedUsersDiv.classList.add("dis_flex");
    selectedUsersDiv.classList.remove("dis_none");
    selectedUsersDiv.children[0].classList.remove("dis_none")
    selectedUsersDiv.children[0].classList.add("dis_flex")


    continueButton.classList.add("dis_flex");
    continueButton.children[0].classList.remove("dis_none")
    continueButton.children[0].children[0].classList.remove("dis_none")

    agfMode = 'creategroup';
    log(agfMode)
})



let selectedUsers = [];
const selectedUsersList = document.getElementById('selected-users-list');


selectedUsersList.addEventListener('click', (ev) => {
    if (ev.target.classList.contains('remove-user')) {
        removeUser(ev.target.parentElement.dataId)

    }
})
// Render all users
function renderUsers() {
    const usersList = document.getElementById('agf-users-list');
    usersList.innerHTML = ''; // Clear the list
    log(friends);
    friends.forEach(user => {
        const userItem = document.createElement('div');
        userItem.classList.add('agf-user-item');
        userItem.innerHTML = `
                <div class="agf-user-avatar">
                    <div class="agf-status-dot ${user.status}"></div>
                </div>
                <div class="agf-user-info">
                    <div class="agf-user-name">${user.name}</div>
                </div>
                <input type="checkbox" class="user-checkbox" data-id="${user.id}">
            `;
        const checkbox = userItem.querySelector('.user-checkbox');
        checkbox.addEventListener('change', () => toggleUserSelection(user, checkbox));
        usersList.appendChild(userItem);
    });
}


function toggleUserSelection(user, checkbox) {
    if (checkbox.checked) {
        selectedUsers.push(user);
    } else {
        selectedUsers = selectedUsers.filter(u => u.id !== user.id);
    }
    renderSelectedUsers();
}







continueButton.addEventListener('click', () => {
    agfMode = "createconfirm";
    confirmationPage.classList.add('dis_flex');
    confirmationUsersList.classList.add('dis_flex');
    confirmationUsersList.classList.remove('dis_none');
    agfSearchInput.classList.remove('display_block');
    appContainer.classList.remove('agf-search-mode');
    headerTitle.textContent = 'Confirm users';
    agfSearchInput.querySelector('input').value = '';
    usersList.classList.remove('dis_flex');
    usersList.classList.remove('dis_grid');
    usersList.classList.add('dis_none');
    selectedUsersDiv.classList.remove("dis_flex");
    selectedUsersDiv.classList.add("dis_none");
    selectedUsersDiv.children[0].classList.add("dis_none")
    selectedUsersDiv.children[0].classList.remove("dis_flex");
    continueButton.classList.remove("dis_flex");
    continueButton.children[0].classList.add("dis_none")
    continueButton.children[0].children[0].classList.add("dis_none");
    searchButton.classList.add("dis_none");

    confirmationUsersList.innerHTML = selectedUsers.map(user => `
         <div class="confirmation-user">
            <div class="avatar confirmation-user-avatar" style="background-color: rgb(16, 185, 129);">${getInitials(user.name)}</div>
             <span class="confirmation-user-name">${user.name}</span>
         </div>
     `).join('');
});



function renderSelectedUsers() {
    selectedUsersList.innerHTML = '';

    selectedUsers.forEach(user => {
        const selectedUserItem = document.createElement('div');
        selectedUserItem.classList.add('selected-user-item');
        selectedUserItem.setAttribute("data-user-id", user.id);

        selectedUserItem.innerHTML += `
	            <div class="avatar selected-user-avatar" style="background-color: rgb(16, 185, 129);">
	                ${getInitials(user.name)}
	            </div>
	            <button class="remove-user-icon" data-user-id="${user.id}">
	                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
	                    <path d="M18 6 6 18M6 6l12 12"/>
	                </svg>
	            </button>
	        `;

        selectedUsersList.appendChild(selectedUserItem);


    });

    continueButton.disabled = selectedUsers.length === 0;
}


selectedUsersList.addEventListener("click", function (event) {
    if (event.target.closest(".remove-user-icon")) {
        const userId = event.target.closest(".remove-user-icon").getAttribute("data-user-id");
        removeUser(userId);
    }
});


function removeUser(userId) {
    selectedUsers = selectedUsers.filter(user => user.id !== userId);
    renderSelectedUsers();

    const checkbox = document.querySelector(`.user-checkbox[data-id="${userId}"]`);
    if (checkbox) checkbox.checked = false;
}






function searchUser() {
    let query = document.querySelector(".agf-search-input > input").value;

    if (agfMode === 'quickchat') {
        if (!fetchUsers) {
            fetchUsers = true;
            fetch(`searchUser?query=${query}`)
                .then(response => {
					if(!checkSession(response)) return;
					return response.json()})
                .then(data => {
                    let resultsDiv = document.getElementById("agf-users-list");
                    resultsDiv.innerHTML = "";
                    fetchUsers = false;

                    if (data.length === 0) {
                        resultsDiv.innerHTML = "<p>No users found</p>";
                        return;
                    }

                    data.forEach((user) => {
                        let id = user.id;
                        if (user.name !== userName) {
                            globalUsers[`${id}`] = user;
                            resultsDiv.appendChild(createUserItem(user));
                        }
                    });

                    log(globalUsers);
                })
                .catch(error => console.error("Error fetching users:", error));
        } else {
            log("Fetching users...");
        }
    } else {
//        const filteredUsers = Object.values(friends)
//            .filter(user => user["name"].toLowerCase().includes(query.toLowerCase()));
//        renderFilteredUsers(filteredUsers);
	const filteredUsers = Object.values(friends)
    .filter(user => user && user.name && user.name.toLowerCase().includes(query.toLowerCase()));

renderFilteredUsers(filteredUsers);

    }
}
//(friends only)
function renderFilteredUsers(filteredUsers) {
    const usersList = document.getElementById('agf-users-list');
    usersList.innerHTML = '';
    filteredUsers.forEach(user => {
        const userItem = document.createElement('div');
        userItem.classList.add('agf-user-item');
        userItem.innerHTML = `
                <div class="agf-user-avatar">
                    <div class="agf-status-dot ${user.status}"></div>
                </div>
                <div class="agf-user-info">
                    <div class="agf-user-name">${user.name}</div>
                </div>
                <input type="checkbox" class="user-checkbox" data-id="${user.id}">
            `;
        const checkbox = userItem.querySelector('.user-checkbox');
        checkbox.addEventListener('change', () => toggleUserSelection(user, checkbox));
        usersList.appendChild(userItem);
    });
}







// group actions
successCreateGroupBtn.addEventListener('click', () => {

    let name = groupName.value.trim();
    if (name.length > 100) {
        dispPopup("Please ensure the group name is under 100 characters. 😊");
        groupName.value = '';
        return;
    }
    groupName.value = '';

    if (name == '') {
        dispPopup('Please enter a group name. 😊');
        return;
    }


    let members = selectedUsers.map((u) => {
        return +u.id.slice(1);

    })
    let membersMapedByNames = selectedUsers.map((u) => {
        return u.name;

    })

    createGroupUpdateDB(name, members, membersMapedByNames);
})

async function createGroupUpdateDB(groupName, members, membersMapedByNames) {
    const response = await fetch("createGroup", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ group_name: groupName, members: members })
    });
    if(!checkSession(response)) return;
    const result = await response.json();
    console.log("result")
    console.log(result)
    let group = {
        id: "g"+result.id,
        name: groupName,
        type: "group"
    }
    sendGroupCreationNotification(group, membersMapedByNames);
    myGroups["g"+result.id] = group;
    const container = document.getElementById('users-container');
    container.appendChild(createUserItem(group));
    currentreciever = group.id;
    log("cu.receiver" + currentreciever);
    changeReceiver();
    openChatWindow(currentreciever);
    confirmationUsersList.classList.remove('dis_flex');
    confirmationUsersList.classList.add('dis_none');
    appContainer.classList.add('agf-search-mode');
    headerTitle.textContent = 'Quick Chat';
    usersList.classList.add('dis_flex');
    usersList.classList.add('dis_grid');
    usersList.classList.remove('dis_none');


    searchButton.classList.remove("dis_none");
    usersList.innerText = "";
    FRIEND_SUGGEST.classList.toggle("agf-container-open");
    plusFriend.classList.toggle("rotate-plus-btn");
    confirmationPage.classList.remove('dis_flex');
    confirmationUsersList.classList.remove('dis_flex');
    confirmationUsersList.classList.add('dis_none');
    createGroup.classList.remove('dis_none');
    selectedUsers.length = 0;
    selectedUsersDiv.children[0].innerText = "";
    agfMode = "initial";
    log(result);
}

function sendGroupCreationNotification(group, members) {
    const message = {
        type: "NEW-GROUP",
        group: group,
        members: members

    };
    log(message);
    if (websocket && websocket.readyState === WebSocket.OPEN) {
        websocket.send(JSON.stringify(message));

    }

}



//Leader board
let postCountLeaderboardElement = document.getElementById('lb-post-count');
let faqCountLeaderboardElement = document.getElementById('lb-faq-count');



async function fetchPostCountLeaderboard() {
    try {
        const response = await fetch('postCountLeaderBoard?limit=5');
        if(!checkSession(response)) return;
        if (!response.ok) {
            throw new Error('Failed to fetch leaderboard data');
        }
        const data = await response.json();
        console.log(data);
        return data.data;
        // return displayLeaderboard(data.data,"posts","Top Posters");
    } catch (error) {
        console.error('Error fetching leaderboard:', error);
    }
}


const badges = [
    '🏆', // Gold Trophy for 1st
    '💪', // Flexing Bicep for 2nd
    '🔥'  // Flame for 3rd
];

async function displayLeaderboard(data, basedOn, title) {
    log(title);

    let LeaderboardElement = document.createElement('div');
    LeaderboardElement.innerHTML = '';
    LeaderboardElement.className = "leader-board-items";
    LeaderboardElement.innerHTML = `<h2 class='leader-board-title'>${title}</h2>`;
    data.forEach((item, index) => {
        const div = document.createElement('div');
        div.classList.add('lb-pc-item', 'user-item');


        const avatarContainer = document.createElement('div');
        avatarContainer.className = 'avatar-container';

        const avatarDiv = document.createElement('div');
        avatarDiv.className = 'avatar';

        avatarDiv.style.backgroundColor = getRandomColor(item.name);
        let name = item.username;
        avatarDiv.textContent = getInitials(item.username);

        avatarContainer.appendChild(avatarDiv);


        const infoDiv = document.createElement('div');
        infoDiv.className = 'user-info';




        const nameP = document.createElement('p');

        nameP.className = 'user-name';
        nameP.textContent = name;

        if (name.length > 11) {
            nameP.textContent = name.slice(0, 11) + '....';
        }

        const aboutP = document.createElement('p');
        aboutP.className = 'user-about';
        aboutP.textContent = item.count + " " + basedOn;
        infoDiv.appendChild(nameP);
        infoDiv.appendChild(aboutP);



        let badgeSpan = null;
        if (index < 3) {
            badgeSpan = document.createElement('div');
            badgeSpan.className = 'user-badge';
            badgeSpan.textContent = badges[index];

        }

        div.appendChild(avatarContainer);
        div.appendChild(infoDiv);
        if (badgeSpan != null) div.appendChild(badgeSpan); // Only append if badge exists
        LeaderboardElement.appendChild(div);
    });
    return LeaderboardElement;
}



async function fetchFaqCountLeaderboard() {
    try {
        const response = await fetch('faqCountLeaderBoard');
        if(!checkSession(response)) return;
        if (!response.ok) {
            throw new Error('Failed to fetch leaderboard data');
        }
        const data = await response.json();
        console.log(data);
        return data.data;

    } catch (error) {
        console.error('Error fetching leaderboard:', error);
    }
}
let mostLikedPosters = getId("lb-most-liked-posters");
let trendingUsersForPOsts = getId("lb-trending-users-post");





// Fetch Post Count Leaderboard
async function fetchPostCountLeaderboard() {
    try {
        const response = await fetch('postCountLeaderBoard?limit=5');
        if(!checkSession(response)) return;
        if (!response.ok) {
            throw new Error('Failed to fetch leaderboard data');
        }
        const data = await response.json();
        console.log('Post Count Leaderboard:', data);
        return data.data;
    } catch (error) {
        console.error('Error fetching leaderboard:', error);
    }
}

// Fetch Most Liked Posters
async function fetchMostLikedPosters() {
    try {
        const response = await fetch('mostLikedPosters');
        if(!checkSession(response)) return;
        if (!response.ok) {
            throw new Error('Failed to fetch most liked posters');
        }
        const data = await response.json();
        console.log('Most Liked Posters:', data);
        return data.data;
    } catch (error) {
        console.error('Error fetching most liked posters:', error);
    }
}

// Fetch Trending Users for Post Section
async function fetchtTrendingUsersPostSection() {
    try {
        const response = await fetch('trendingUsersforPosts');
        if (!response.ok) {
            throw new Error('Failed to fetch trending users for posts');
        }
        const data = await response.json();
        console.log('Trending Users for Posts:', data);
        return data.data;
    } catch (error) {
        console.error('Error fetching trending users for posts:', error);
    }
}

// let trendingUserFaq = getId("lb-faq-trending")
// let mostLikedUsersFaq = getId("lb-most-liked-users-faq")
// let topContributors = getId("top-contributors-faq")


async function fetchTrendingUsers() {
    console.log("in the trending function");
    try {
        const response = await fetch('trendingUsersFaq');
        if(!checkSession(response)) return;
        const data = await response.json();
        console.log("daaaaaaaaaaaaaaaaaaaaaaaaaa");
        return data.data;
    } catch (error) {
        console.error('Error fetching trending users:', error);
    }
}

async function fetchFaqLeaderboard(type) {
    console.log("in the testing of the 2 thing ", type);
    try {
        const response = await fetch(`faqLeaderBoard?type=${type}`);
        if(!checkSession(response)) return;
        const data = await response.json();
        console.log("last two....");
        console.log(data);
        return data.data;
    } catch (error) {
        console.error(`Error fetching ${type} leaderboard:`, error);
    }
}


function showSkeletonLoader(section, count, type) {
    let skeletonHTML = '';

    for (let i = 0; i < count; i++) {
        if (type === 'post-skeleton') {

            skeletonHTML += `
        <div class="skeleton-card post-skeleton">
          <div class="card-top-skeleton">
            <div style="display: flex; align-items: center; gap: 12px;">
              <div class="skeleton skeleton-profile"></div>
              <div style="flex: 1;">
                <div class="skeleton skeleton-line short"></div>
                <div class="skeleton skeleton-line medium"></div>
              </div>
            </div>
            <div class="skeleton skeleton-line long"></div>
            <div class="skeleton skeleton-line long"></div>
            <div class="skeleton skeleton-line medium"></div>
          </div>
          <div class="card-bottom-skeleton">
            <div class="skeleton skeleton-line veryshort"></div>
            <div class="skeleton skeleton-line veryshort"></div>
            <div class="sskeleton keleton-line veryshort"></div>
          </div>
        </div>
      `;
        } else if (type === 'leaderboard-skeleton') {

            skeletonHTML += `
        <div class="skeleton-leaderboard leaderboard-skeleton">
          <div class="skeleton skeleton-line medium"></div>
          <div style="display: flex; align-items: center; gap: 12px;">
            <div class="skeleton skeleton-profile"></div>
            <div style="flex: 1;">
              <div class="skeleton skeleton-line short"></div>
              <div class="skeleton skeleton-line medium"></div>
            </div>
          </div>
          <div style="display: flex; align-items: center; gap: 12px;">
            <div class="skeleton skeleton-profile"></div>
            <div style="flex: 1;">
              <div class="skeleton skeleton-line short"></div>
              <div class="skeleton skeleton-line medium"></div>
            </div>
          </div>
        </div>
      `;
        }
    }


    section.innerHTML = skeletonHTML;
}


let dashBoardPost = _(".card-post");

async function updateDashBoardpost() {

    const response = await fetch(`getPosts?from=${0}&to=${1}`);
    if(!checkSession(response)) return;
    const data = await response.json();


    let post = data.posts;

    let singlepostUI = await createPostUI(post[0])
    dashBoardPost.appendChild(singlepostUI);
    return displayPosts(post[0]);

}

updateDashBoardpost();


function checkSession(response) {
    if (response.redirected) {
        dispPopup("Session is expired");
        window.location.href = "loginandSignup.html";
        return false; 
    }
    return true; 
}
