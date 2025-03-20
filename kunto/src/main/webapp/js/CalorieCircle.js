function updateProgress(percent) {
    let circle = document.getElementById("progress-circle");  
    let circle2 = document.getElementById("progress-circle2"); 
    const circumference = 2 * Math.PI * 135; 

    if (percent <= 100) {
        let offset = circumference - (percent / 100) * circumference;
        circle.style.strokeDashoffset = offset;
        circle2.style.strokeDashoffset = circumference; 
    } else {
        circle.style.strokeDashoffset = 0; 
        setTimeout(() => {
            let extraProgress = percent - 100; 
            let offset2 = circumference - (extraProgress / 100) * circumference;
            circle2.style.strokeDashoffset = offset2; 
        }, 2000); 
    }
}
