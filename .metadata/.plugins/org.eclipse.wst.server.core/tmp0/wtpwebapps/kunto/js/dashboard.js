

// Get all the nav icons
function _(selector) {
    return document.querySelector(selector);
}
function getId(selector) {
    return document.getElementById(selector);
}
const navIcons = document.querySelectorAll('.nav-icon');
const SLIDEBAR = _(".sidebar");
const BGICON = _(".bg-icon");
const botArea = _('.ku-bot-area');
const BOT = _(".ku-bot");
const BOT_INP = _("#user-input");
const BOT_MSG = _(".ku-bot-body");

const DA_WALK_TOT=_(".da-walk-cal-tot");
const DA_WORKOUT_TOT=_(".da-work-cal-tot")
const DA_ACTIVITY_TOT=_(".da-activity-cal-tot");
const DA_TOT=_(".da-tot-cal-in")
const OVERALL_PERCENTAGE=_(".overall-percentage");

const DA_WAT=_(".da-wat-per");
const DA_PRO=_(".da-pro-per");
const DA_CAR=_(".da-car-per");
const DA_FAT=_(".da-fat-per");






// Add event listener to each icon

/*SLIDEBAR.addEventListener("click", (elem) => {
    let target = elem.target.parentElement;
    let preElem = document.querySelector(".active");
    let preTarget = preElem.classList.contains("fa-solid") ? preElem.parentElement.dataset.target : preElem.dataset.target;
    let currentTarget = target.dataset.target;
    console.log(target);
    console.log(preElem);
    console.log(preTarget);
    console.log(currentTarget);

    console.log(target == preElem);



    if (target.classList.contains("bg-icon")) {
        if (target != preElem) {
            console.log("hio");

            target.classList.add("active");
            getId(currentTarget).classList.add("dis_block")
            preElem.classList.remove("active");
            getId(preTarget).classList.remove("dis_block");
        }
        return;
    }
})*/




_(".da-chat").addEventListener('click',function(elem){
	console.log("uyjsfgjdgfjs")
	goToCommunityTab() 
	if(elem.target.closest	(".da-card-chat")){
		showsection('chats');
	}else{
		showsection('posts');
	}
	
})
getId("activity-tap").addEventListener('click',function(elem){
	console.log("uyjsfgjdgfjs")
	 goToActivityTab();
	
	
})
getId("workout-tap").addEventListener('click',function(elem){
	console.log("uyjsfgjdgfjs")
	  goToWorkoutTap()
	
	
})
getId("nutrition-tap").addEventListener('click',function(elem){
	console.log("uyjsfgjdgfjs")
	goToNutritionTap();
	
	
})




function goToWorkoutTap() {
     let communityIcon = document.querySelector('[data-target="workout"]');
 	let fakeEvent = { target: communityIcon };
	SLIDEBARMenu(fakeEvent);
}
function goToActivityTab() {
     let communityIcon = document.querySelector('[data-target="activity"]');
 	let fakeEvent = { target: communityIcon };
	SLIDEBARMenu(fakeEvent);
}
function goToNutritionTap() {
     let communityIcon = document.querySelector('[data-target="nutrition"]');
 	let fakeEvent = { target: communityIcon };
	SLIDEBARMenu(fakeEvent);
}



function goToCommunityTab() {
     let communityIcon = document.querySelector('[data-target="community"]');
 	let fakeEvent = { target: communityIcon };
	SLIDEBARMenu(fakeEvent);
}
SLIDEBAR.addEventListener("click",(elem)=>{
	console.log("elem")
	console.log(elem)
    SLIDEBARMenu(elem);
})

function SLIDEBARMenu(elem) {
	
    let target = elem.target.closest('.bg-icon'); // Use closest to ensure correct selection
    let preElem = document.querySelector(".active");

    if (!target) {
        console.log("No target found");
        return;
    }

    let preTarget = preElem?.dataset?.target || (preElem?.classList.contains("fa-solid") ? preElem.parentElement.dataset.target : undefined);
    let currentTarget = target.dataset.target;

    console.log("Target Element:", target);
    console.log("Previous Active Element:", preElem);
    console.log("Previous Target:", preTarget);
    console.log("Current Target:", currentTarget);

    if (target.classList.contains("bg-icon")) {
        if (target !== preElem) {
            console.log("Switching tabs...");
            target.classList.add("active");
            getId(currentTarget).classList.add("dis_block");
            preElem.classList.remove("active");
            getId(preTarget).classList.remove("dis_block");
        }
    }
}
BOT.addEventListener('click', function (ev) {
    if (ev.target.classList.contains("ku-bot-img") || ev.target.classList.contains("close-bot")) {
        botArea.classList.toggle('open');

    }
});
function sendMessage() {
    let query = BOT_INP.value.trim();
    if (!query) return;

    // Display user message instantly
    createMessage(query, "user-message");

    BOT_INP.value = ""; // Clear input

    // Add "Thinking..." message
    let thinkingMsg = document.createElement("p");
    thinkingMsg.classList.add("message", "bot-message", "thinking");
    thinkingMsg.innerText = "Thinking";
    BOT_MSG.appendChild(thinkingMsg);
    BOT_MSG.scrollTop = BOT_MSG.scrollHeight; // Auto-scroll

    fetch(`/kunto/kubot?query=${query}`)
        .then(res => res.json())
        .then((data) => {
            console.log(data);

            let botResponse = data.data.results[0] || "I'm not sure how to respond to that.";

            // Remove "Thinking..." before displaying real response
            thinkingMsg.remove();

            // Display bot message with typing effect
            createTypingEffect(botResponse, "bot-message");
        })
        .catch(error => {
            thinkingMsg.remove();
            console.error("Error:", error);
        });
}


// Function to create a simple user message
function createMessage(text, className) {
    const msg = document.createElement("p");
    msg.classList.add("message", className);
    msg.innerText = text;
    BOT_MSG.appendChild(msg);
    BOT_MSG.scrollTop = BOT_MSG.scrollHeight; // Auto-scroll
}

// Function to create typing effect for bot messages
function createTypingEffect(text, className) {
    const msg = document.createElement("p");
    msg.classList.add("message", className);
    BOT_MSG.appendChild(msg);

    let i = 0;
    function typeLetter() {
        if (i < text.length) {
            msg.innerHTML += text.charAt(i);
            i++;
            setTimeout(typeLetter, 30); // Speed of typing effect
        }
    }
    typeLetter();
    BOT_MSG.scrollTop = BOT_MSG.scrollHeight; // Auto-scroll
}


// dashboard

/*document.addEventListener('DOMContentLoaded', function () {
    const textColor = '#a1a1aa';
    const gridColor = '#2d2d30';
    const primaryColor = '#e4c1f9';

    Chart.defaults.color = textColor;
    Chart.defaults.borderColor = gridColor;
    Chart.defaults.font.family = '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif';

    // Steps Chart
    const stepsCtx = document.getElementById('stepsChart').getContext('2d');
    new Chart(stepsCtx, {
        type: 'bar',
        data: {
            labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            datasets: [{
                data: [3000, 4500, 2800, 5050, 3800, 4200, 3500],
                backgroundColor: primaryColor,
                borderRadius: 8,
                barThickness: 12,
                maxBarThickness: 12
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false },
                tooltip: {
                    backgroundColor: '#1f2937',
                    titleFont: { size: 13 },
                    bodyFont: { size: 12 },
                    padding: 12,
                    displayColors: false
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    grid: {
                        color: gridColor,
                        drawBorder: false,
                        lineWidth: 0.5
                    },
                    ticks: {
                        color: textColor,
                        font: { size: 11 },
                        padding: 8,
                        maxTicksLimit: 5
                    }
                },
                x: {
                    grid: { display: false },
                    ticks: {
                        color: textColor,
                        font: { size: 11 },
                        padding: 8
                    }
                }
            }
        }
    });

    // Meditation Chart
    const meditationCtx = document.getElementById('meditationChart').getContext('2d');
    new Chart(meditationCtx, {
        type: 'line',
        data: {
            labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            datasets: [{
                data: [20, 25, 20, 30, 25, 20, 20],
                borderColor: '#4ade80',
                backgroundColor: 'rgba(74, 222, 128, 0.1)',
                fill: true,
                tension: 0.4,
                borderWidth: 2,
                pointRadius: 0,
                pointHoverRadius: 4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false },
                tooltip: {
                    backgroundColor: '#1f2937',
                    titleFont: { size: 13 },
                    bodyFont: { size: 12 },
                    padding: 12,
                    displayColors: false
                }
            },
            scales: {
                y: {
                    display: false,
                    beginAtZero: true
                },
                x: {
                    display: false
                }
            }
        }
    });

    // Sleep Chart
    const sleepCtx = document.getElementById('sleepChart').getContext('2d');
    new Chart(sleepCtx, {
        type: 'line',
        data: {
            labels: ['20:00', '22:00', '00:00', '02:00', '04:00', '06:00'],
            datasets: [{
                data: [2, 4, 3.5, 5, 4, 3],
                borderColor: '#fb923c',
                backgroundColor: 'rgba(251, 146, 60, 0.1)',
                fill: true,
                tension: 0.4,
                borderWidth: 2,
                pointRadius: 0,
                pointHoverRadius: 4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false },
                tooltip: {
                    backgroundColor: '#1f2937',
                    titleFont: { size: 13 },
                    bodyFont: { size: 12 },
                    padding: 12,
                    displayColors: false
                }
            },
            scales: {
                y: {
                    display: false,
                    beginAtZero: true,
                    suggestedMax: 6
                },
                x: {
                    display: false
                }
            }
        }
    });
});*/

// food
const metrics = {
        dwater: 42,
        services: 28,
        heat: 17,
        energy: 16
    };

    // Calculate the circumference of the circle
    const radius = 90;
    const circumference = 2 * Math.PI * radius;

    // Function to set the segment position and progress
    function setSegment(className, startAngle, percentage) {
        const circle = document.querySelector(`.progress-ring-segment.${className}`);
        const segmentLength = (circumference / 4); // Each segment takes 1/4 of the circle

        // Calculate the dasharray and offset
        circle.style.strokeDasharray = `${segmentLength} ${circumference}`;

        // Set the rotation for the segment
        circle.style.transform = `rotate(${startAngle}deg)`;

        // Make segment visible
        circle.style.opacity = 1;

        // Animate the fill based on percentage
        setTimeout(() => {
            const offset = segmentLength - (percentage / 100) * segmentLength;
            circle.style.strokeDashoffset = offset;
        }, 100);
    }

    // Initialize the segments with their positions (90 degrees apart)
    const segmentPositions = {
        dwater: -90,      // Top
        services: 0,     // Right
        heat: 90,        // Bottom
        energy: 180      // Left
    };

    // Set up each segment with delays
    function updateFoodMeasurement(){
		    Object.entries(metrics).forEach(([metric, percentage], index) => {
        setTimeout(() => {
            setSegment(metric, segmentPositions[metric], percentage);
        }, index * 200);
    });
	}
	




let calorieChartInstance;

 document.addEventListener('DOMContentLoaded', () => {
            const ctx = document.getElementById('calorieChart').getContext('2d');

            // 7-day calorie burn data
            const calorieData = {
                labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'], // Days of the week
                datasets: [
	{
                        label: 'Walking',
                        data: [250, 300, 200, 350, 280, 400, 150],
                        borderColor: '#4ade80', // Green
                        backgroundColor: 'rgba(74, 222, 128, 0.2)',
                        borderWidth: 2,
                        pointRadius: 5,
                        pointBackgroundColor: '#4ade80',
                        fill: true,
                        tension: 0.4
                    },
                    {
                        label: 'Workout',
                        data: [450, 600, 500, 700, 550, 800, 400],
                        borderColor: '#60a5fa', // Blue
                        backgroundColor: 'rgba(96, 165, 250, 0.2)',
                        borderWidth: 2,
                        pointRadius: 5,
                        pointBackgroundColor: '#60a5fa',
                        fill: true,
                        tension: 0.4
                    },
                    {
                        label: 'Activities',
                        data: [300, 400, 350, 450, 380, 500, 250],
                        borderColor: '#c084fc', // Purple
                        backgroundColor: 'rgba(192, 132, 252, 0.2)',
                        borderWidth: 2,
                        pointRadius: 5,
                        pointBackgroundColor: '#c084fc',
                        fill: true,
                        tension: 0.4
                    }
                ]
            };

            // Chart configuration
            const config = {
                type: 'line',
                data: calorieData,
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        y: {
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: 'Calories'
                            }
                        },
                        x: {
                            title: {
                                display: true,
                                text: 'Days of the Week'
                            }
                        }
                    },
                    plugins: {
                        legend: {
                            display: true,
                            position: 'top'
                        }
                    }
                }
            };

            // Render chart
            new Chart(ctx, config);
            calorieChartInstance = Chart.getChart("calorieChart");
            console.log(calorieChartInstance)
        });




// post

document.addEventListener('DOMContentLoaded', () => {
    const pollOptions = document.querySelectorAll('.poll-option');
    let hasVoted = false;

    pollOptions.forEach(option => {
        option.addEventListener('click', () => {
            if (!hasVoted) {
                // Remove any previous selections
                pollOptions.forEach(opt => opt.classList.remove('selected'));

                // Select the clicked option
                option.classList.add('selected');

                // Animate the fill
                const percentage = option.dataset.percentage;
                const fillElement = option.querySelector('.poll-fill');
                fillElement.style.width = `${percentage}%`;

                hasVoted = true;
            }
        });
    });

    // Initialize poll bars with animation
    setTimeout(() => {
        pollOptions.forEach(option => {
            const percentage = option.dataset.percentage;
            const fillElement = option.querySelector('.poll-fill');
            fillElement.style.width = `${percentage}%`;
        });
    }, 500);
});







console.log(Chart.getChart("calorieChart"),"hi");
function getCalData(){
	fetch("/kunto/ku_calorie_burned")
	.then(res=>{
		if(res.ok){}
		return res.json()
	})
	.then((data)=>{
		console.log(data);
		updateCalorieChart(data)
	})
	
}


function updateDashboardGrapghInnerText(walk,workout,activity){
	DA_WALK_TOT.innerText=walk;
	DA_WORKOUT_TOT.innerText=workout;
	DA_ACTIVITY_TOT.innerText=activity;
	DA_TOT.innerText=walk+workout+activity
}


function updateCalorieChart(calorieObj) {
    if (calorieChartInstance) {
        let today = new Date();
        let currentDayIndex = today.getDay(); // 0 (Sunday) to 6 (Saturday)
        
        let daysMapping = ['SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'];

        // Get only days from Sunday to today (currentDayIndex)
        let filteredDays = daysMapping.slice(0, currentDayIndex + 1);

        // Function to extract data up to today
        function extractData(activityType) {
            return filteredDays.map(day => calorieObj[activityType]?.[day] || 0);
        }

        // Update chart labels (only up to today)
        calorieChartInstance.data.labels = filteredDays.map(day => day.charAt(0) + day.slice(1).toLowerCase());

        // Update datasets dynamically
        calorieChartInstance.data.datasets[0].data = extractData("Walking");
        calorieChartInstance.data.datasets[1].data = extractData("Workout");
        calorieChartInstance.data.datasets[2].data = extractData("Activity");
        let sum1=0;
        let sum2=0;
        let sum3=0;
        
        calorieChartInstance.data.datasets[0].data.forEach((da)=>sum1+=da);
        calorieChartInstance.data.datasets[1].data.forEach((da)=>sum2+=da);
        calorieChartInstance.data.datasets[2].data.forEach((da)=>sum3+=da);
        console.log("innnnnnnnnnnnnnd")
        console.log(sum1,sum2,sum3);
        updateDashboardGrapghInnerText(sum1,sum2,sum3);

        // Refresh the chart
        calorieChartInstance.update();
    }
}





async function getUserStars(){
	console.log("star updating")
	let response =await  fetch("updateStars");
	if(!checkSession(response)) return;
	
	let json=await response.json();
	if(json.success){
		return  json.stars;
		
	}
	return 6;
	
	
}

async function updateStars() {
    let starCount = await getUserStars(); 
	console.log("user stars :"+ starCount)
    let stars = document.querySelectorAll(".star-container .fa-star");
	if(starCount <=3){
		removeFeatures();
	}
    stars.forEach((star, index) => {
        if (index < starCount) {
            star.classList.add("gold");
            star.classList.remove("gray");
        } else {
            star.classList.add("gray");
            star.classList.remove("gold");
        }
    });
}
let nutritionGraphOverlay=getId("nu-graph-overlay");
let activityGraphOverlay=getId("ac-graph-overlay");
let nutritionCalOverlay=getId("nu-cal-overlay"); 


function removeFeatures(){
	nutritionGraphOverlay.classList.remove("dis_none");
	nutritionGraphOverlay.classList.add("dis_flex");
	activityGraphOverlay.classList.remove("dis_none");
	activityGraphOverlay.classList.add("dis_flex");
	nutritionCalOverlay.classList.remove("dis_none");
	nutritionCalOverlay.classList.add("dis_flex");
	
}

function fetchUserProfile() {
	console.log("i'm here");
        fetch(`userProfile`)
            .then(response => response.json())
            .then(data => {

                // Update profile image
                document.getElementById('profile-picture').src = data.user_profile_picture;
				document.getElementById('profileImage').src = data.user_profile_picture;

                // Update profile name
                document.getElementById('profile-name').textContent = data.user_name;
				document.getElementById('name').value = data.user_name;

                // Update height (convert to feet if necessary)
                document.getElementById('profile-height').textContent = `${data.height} cm`; // Convert cm to ft
				document.getElementById('height').value =data.height;

                // Update weight
                document.getElementById('profile-weight').textContent = `${data.weight} kg`;
				document.getElementById('weight').value = data.weight;
				console.log(document.getElementById('weight') , typeof data.weight , data.weight);
                // Update BMI (for example, you can calculate BMI if needed)
                let heightInMeters = data.height / 100;
                let bmi = (data.weight / (heightInMeters * heightInMeters)).toFixed(2);
                document.getElementById('profile-bmi').textContent = bmi;

                // Update age
                document.getElementById('profile-age').textContent = `${data.age} yrs`;
				
				document.getElementById("activityLevel").value = data.active_level;
				document.getElementById("activityLevel").dispatchEvent(new Event("change"));
				
				document.getElementById("email").value = data.email;
				document.getElementById("about").value = data.about;
            })
            .catch(error => {
                console.error('Error fetching user profile:', error);
            });
    };
	
	
	

	document.getElementById("profileForm").addEventListener("submit", async function(event) {
	            event.preventDefault();
				
				
				let imgData= await uploadImagetoServer(document.getElementById('imageUpload'));


	            let formData = {
					user_profile_picture : (imgData)? imgData.imageUrl : document.getElementById('profileImage').src,
	                user_name: document.getElementById("name").value,
	                height: document.getElementById("height").value,
	                weight: document.getElementById("weight").value,
	                activity_level: document.getElementById("activityLevel").value,
	                about: document.getElementById("about").value
	            };

	            fetch("/kunto/updateUserProfile", {
	                method: "POST",
	                headers: { "Content-Type": "application/json" },
	                body: JSON.stringify(formData)
	            })
	            .then(response => response.json())
	            .then(data => {
	                if (data.message) {
						console.log(data);
	                    fetchUserProfile();
	                } else{
						console.log("msg illaaaa.............")
					}
	            })
	            .catch(error => console.error("Error:", error));
	        });
	
    // Call the function with the user ID (for example, 15)
