
let popupMsg = document.getElementById("popup-msg");

function dispPopup(message, time = 1500) {
    popupMsg.innerText = message;
    popupMsg.classList.add("show");

    setTimeout(() => {
        popupMsg.classList.remove("show");
    }, time);
}
