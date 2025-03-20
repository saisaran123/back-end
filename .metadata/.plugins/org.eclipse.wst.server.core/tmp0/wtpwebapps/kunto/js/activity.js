
const PLUS_SLEEP=_(".plus-time");
const PLUS_DISTANCE=_(".plus-distance");
let nowDay=new Date().toISOString().split("T")[0];
const MONTHS=['Jan','Feb','Mar','Apr','May','Jun','Jul','Agu','Sep','Oct','Nov','Dec'];
const DAYS=["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"]
const TRACKING_DATE=_(".tracking-date");
const MAN_CAL_SAVE_BTN=_(".man-save-btn");
const OTHER_ACTIVITY_NAME=_("#ac-activity");
const OTHER_ACTIVITY_CAL=_("#calories");
const OTHER_ACTIVITY_DATE=_("#date");
let ONE_MONTH_CAL={};
const TOT_SLEEP_TIME=_(".tot-sleep-time");
const TOT_KM_WALKED=_(".tot-km-walked");
const AC_DURATION=_("#duration")
		

let temp_days={
	"MONDAY":0,
	"TUESDAY":1,
	"WEDNESDAY":2,
	"THURSDAY":3,
	"FRIDAY":4,
	"SATURDAY":5,
	"SUNDAY":6
	}
// Activity Chart
        const activityCtx = document.getElementById('activityChart').getContext('2d');
new Chart(activityCtx, {
    type: 'line',
    data: {
        labels: Array.from({ length: 31 }, (_, i) => `${i + 1}`), // 31 days
        datasets: [
            {
                label: 'Activity',
                data: [], 
          borderColor: '#42A5F5',
            backgroundColor: 'rgba(66, 165, 245, 0.2)',
                tension: 0.4,
                fill: true,
                pointBackgroundColor: '#42a5f5',
                pointBorderColor: '#42A5F5',
                pointRadius: 4,
                pointHoverRadius: 6
            },
   
        ]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                display: true, // Show legend for two datasets
                position: 'top'
            }
        },
        scales: {
            y: {
                beginAtZero: true,
                grid: {
                    display: true
                },
                title: {
                    display: true,
                    text: 'cal'
                }
            },
            x: {
                grid: {
                    display: false
                }
            }
        },
        elements: {
            line: {
                borderWidth: 2
            }
        }
    }
});

const activityChartInstance=Chart.getChart("activityChart");
function updateActivity(actObj) {
    if (activityChartInstance) {
        let today = new Date();
        let year = today.getFullYear();
        let month = String(today.getMonth() + 1).padStart(2, "0"); // Ensure two-digit month
        let day = today.getDate(); // Current day of the month

        let orderedData = [];
        
        // Loop from day 1 to today
        for (let d = 1; d <= day; d++) {
            let dateKey = `${year}-${month}-${String(d).padStart(2, "0")}`;
            
            if(d==day){
				setTimeout(()=>{
												            orderedData.push(actObj[dateKey]+Number(DA_WALK_TOT.innerText)|| 0);
				        	TOT_CAL_BURN.innerText=(actObj[dateKey]+Number(DA_WALK_TOT.innerText) || 0)+" kcal"
				},500)


			}
			else{
			orderedData.push(actObj[dateKey] || 0);

			} // Use value from actObj or 0 if missing
        }

        activityChartInstance.data.datasets[0].data = orderedData;

        activityChartInstance.update();
    }
}


        // Mini Circle Chart for stat card
        const miniCircleCtx = document.getElementById('miniCircleChart').getContext('2d');
        new Chart(miniCircleCtx, {
            type: 'doughnut',
            data: {
                datasets: [{	
                    data: [75, 25],
                    backgroundColor: [
                        '#ffa500',
                        '#777777'
                    ],
                    borderWidth: 0,
                    
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                cutout: '80%',
                plugins: {
                    legend: {
                        display: false
                    }
                }
            }
        });
        
  const chartInstance = Chart.getChart("miniCircleChart"); // Select by canvas ID

function updateChart(newValue) {
if (chartInstance) {
	let rem=100-newValue;
    chartInstance.data.datasets[0].data = [newValue,rem]; // Update values
    chartInstance.update(); // Refresh chart
} 
}
        // Time Chart
        const timeCtx = document.getElementById('timeChart').getContext('2d');
        new Chart(timeCtx, {
            type: 'bar',
            data: {
                labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                datasets: [{
                    data: [0,0,0,0,0,0,0],
                    backgroundColor: '#455A64',
                    borderRadius: 4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false
                    }
                },
                scales: {
                    y: {
                        display: false
                    },
                    x: {
                        display: true,
                        ticks:{
							color:'black',
							font:{
								size:14
							}
						}
                    }
                }
            }
        });
        
        const workoutTimeInstance=Chart.getChart("timeChart");
        function updateWorkoutTime(obj){
			let totalTime=0;
				    console.log("printed", obj); // Corrected console log

    		if (workoutTimeInstance) {
       			 Object.keys(obj).forEach(key => {
            			workoutTimeInstance.data.datasets[0].data[temp_days[key]] = +obj[key];
            			totalTime+= Number(obj[key]); // Corrected access
            			console.log("TOTAL TIMEEEEEEEEEEEEEEEEE",totalTime)
        });
                    			console.log("TOTAL TIMEEEEEEEEEEEEEEEEE",totalTime)

       let hours= Number((""+totalTime).split(".")[0] || 0);
        let mins= Number((""+totalTime).split(".")[1] || 0			);
        
        hours+=Number((mins/60).toFixed(0))
		mins=mins%60
		
		TOT_SLEEP_TIME.innerText=hours+"h "+mins+"m"
		
        
		console.log("workot array",workoutTimeInstance.data.datasets[0].data);
		console.log("TOTAL TIME TAKEN",totalTime)
        workoutTimeInstance.update(); // Corrected instance update
    	}
	}
		

        // Distance Chart (Changed to bar chart)
        const distanceCtx = document.getElementById('distanceChart').getContext('2d');
        new Chart(distanceCtx, {
            type: 'bar',
            data: {
                labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                datasets: [{
                    data: [0,0,0,0,0,0,0],
                    backgroundColor: '#455A64',
                    borderRadius: 4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false
                    }
                },
                scales: {
                    y: {
                        display: false,
                        beginAtZero: true
                    },
                    x: {
                        display: true,
                        ticks:{
							color:'black',
							font:{
								size:14
							}
						}
                    }
                }
            }
        });
          const walkInstance= Chart.getChart("distanceChart");
function updateWalkChart(obj) {
    console.log("printed", obj); // Corrected console log
	let totalWalk=0;
    if (walkInstance) {
        Object.keys(obj).forEach(key => {
			console.log("walked data",obj[key])
            walkInstance.data.datasets[0].data[temp_days[key]] = obj[key];
            totalWalk+=Number(obj[key]) // Corrected access
        });
        TOT_KM_WALKED.innerText=totalWalk+" km	"

        walkInstance.update(); // Corrected instance update
    }
}

function getOneMonthCalorieData(){
	fetch("/kunto/activity_stat")
	.then((res)=>{
		if(res.ok){
			return res.json();
		}
	})
	.then((data)=>{
		console.log(data);
		ONE_MONTH_CAL=data;
		updateActivity(data)
	})
}

function getDayOfMonth(date){
	if(date.split("-")[2]>=10){
		return +date.split("-")[2];
	}
	return +date.split("-")[2].slice(1)
}


   /*     // Speed Chart
        const speedCtx = document.getElementById('speedChart').getContext('2d');
        new Chart(speedCtx, {
            type: 'bar',
            data: {
                labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                datasets: [{
                    data: [0.3, 0.5, 0.7, 0.4, 0.6, 0.8, 0.5],
                    backgroundColor: '#242424',
                    borderRadius: 4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false
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
        });*/
        
        

        


        const activities = {
            'cycling': { perHour: 400, perKm: 30 },
            'running': { perHour: 600, perKm: 60 },
            'swimming': { perHour: 500, perKm: 150 },
            'hiking': { perHour: 350, perKm: 40 },
            'dancing': { perHour: 300, perKm: 0 }, // Dancing doesn't use distance
            'basketball': { perHour: 450, perKm: 0 },
            'tennis': { perHour: 400, perKm: 0 },
            'soccer': { perHour: 500, perKm: 50 },
            'yoga': { perHour: 200, perKm: 0 },
            'pilates': { perHour: 250, perKm: 0 },
            'weight-training': { perHour: 350, perKm: 0 },
            'jump-rope': { perHour: 600, perKm: 0 },
            'rowing': { perHour: 450, perKm: 40 },
            'elliptical': { perHour: 400, perKm: 35 },
            'aerobics': { perHour: 350, perKm: 0 },
            'boxing': { perHour: 500, perKm: 0 },
            'crossfit': { perHour: 550, perKm: 0 },
            'rock-climbing': { perHour: 400, perKm: 0 },
            'martial-arts': { perHour: 450, perKm: 0 }
          };
          
          function parseDuration(durationStr) {
            const [hours, minutes, seconds] = durationStr.split(':').map(Number);
            return (hours * 60) + minutes + (seconds / 60);
          }
          
          function parseDistance(distanceStr) {
            return parseFloat(distanceStr.split(' ')[0]) || 0;
          }
          
          function calculateCalories() {
            const activity = document.getElementById('ac-activity').value;
            const durationStr = document.getElementById('duration').value;
            const distanceStr = document.getElementById('distance').value;
            
            const durationInMinutes = parseDuration(durationStr);
            const distanceInKm = parseDistance(distanceStr);
            
            const activityRates = activities[activity];
            
            // Calculate calories from duration
            const durationCalories = (activityRates.perHour * (durationInMinutes / 60));
            
            // Calculate calories from distance (if applicable)
            const distanceCalories = activityRates.perKm * distanceInKm;
            
            // Use the higher value between duration-based and distance-based calculations
            const totalCalories = Math.round(Math.max(durationCalories, distanceCalories));
            
            document.getElementById('calories').value = `${totalCalories} cal`;
          }
          
          document.addEventListener('DOMContentLoaded', () => {
            // Set current date
            const now = new Date();
            const dateInput = document.getElementById('date');
            dateInput.valueAsDate = now;
          
            // Add event listeners for activity, duration, and distance changes
            document.getElementById('ac-activity').addEventListener('change', calculateCalories);
            document.getElementById('duration').addEventListener('input', calculateCalories);
            document.getElementById('distance').addEventListener('input', calculateCalories);
          
            // Initial calories calculation
            calculateCalories();
          });
          
          // Add input formatting for duration
          document.getElementById('duration').addEventListener('blur', function(e) {
            let value = e.target.value;
            if (value.length === 0) return;
            
            // Split the time into components
            let [hours, minutes, seconds] = value.split(':').map(Number);
            
            // Ensure we have valid numbers
            hours = hours || 0;
            minutes = minutes || 0;
            seconds = seconds || 0;
            
            // Format with leading zeros
            e.target.value = `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
          });
          
          // Add input formatting for distance
          document.getElementById('distance').addEventListener('blur', function(e) {
            let value = parseFloat(e.target.value);
            if (isNaN(value)) return;
            
            e.target.value = `${value.toFixed(2)} km`;
          });













function putSleepInfo(sleep_time,wake_up_time,dura){
fetch("/kunto/sleepTrack", {
    method: "POST", // Specify method if needed
    headers: {
        "Content-Type": "application/json"
    },
    body: JSON.stringify({
        sleep_time: sleep_time,
        wake_up_time: wake_up_time,        
        date: nowDay
    })
})
.then((res) => {
    if (res.ok) {
        return res.json();
    }
    throw new Error("Network response was not ok");
})
.then((data) => {
    if (data.message === "success") {
        console.log("Hurray!!!!");
        currNutrition.sleep_time=dura;
		STAT_VALUE.innerText=(`${dura} of sleep `);
		getWeeklyWalkData();
		getWeeklyWorkoutTime();

    }
})
.catch((error) => {
    console.error("Fetch error:", error);
});

}

function getWeeklyWorkoutTime(){
	fetch("/kunto/sleep_logs")
	.then((res)=>{
		if(res.ok){
			return res.json();
		}
	})
	.then((data)=>{
		console.log(data);
		updateWorkoutTime(data);
	})
}



function askSleepTime() {
    Swal.fire({
        title: "Sleep Scheduler",
        html: `
            <div style="display: flex; flex-direction: column; align-items: center; gap: 15px; width: 100%;">
                <div style="display: flex; flex-direction: column; width: 100%; max-width: 320px; text-align: left;">
                    <label for="sleepTime" style="font-weight: bold; margin-bottom: 5px;">Bedtime</label>
                    <input type="time" id="sleepTime" class="swal2-input" style="width: 100%;">
                </div>
                <div style="display: flex; flex-direction: column; width: 100%; max-width: 320px; text-align: left;">
                    <label for="wakeTime" style="font-weight: bold; margin-bottom: 5px;">Wake-up Time</label>
                    <input type="time" id="wakeTime" class="swal2-input" style="width: 100%;">
                </div>
            </div>
        `,
        showCancelButton: true,
        confirmButtonText: "Schedule Sleep",
        cancelButtonText: "Cancel",
        focusConfirm: false,
        preConfirm: () => {
            const sleepTime = document.getElementById("sleepTime").value;
            const wakeTime = document.getElementById("wakeTime").value;

            if (!sleepTime || !wakeTime) {
                Swal.showValidationMessage("Please enter both times!");
            }

            const [sleepHours, sleepMinutes] = sleepTime.split(":").map(Number);
            const [wakeHours, wakeMinutes] = wakeTime.split(":").map(Number);

            let totalMinutesSlept = (wakeHours * 60 + wakeMinutes) - (sleepHours * 60 + sleepMinutes);
            if (totalMinutesSlept < 0) totalMinutesSlept += 24 * 60;

            const hoursSlept = Math.floor(totalMinutesSlept / 60);
            const minutesSlept = totalMinutesSlept % 60;
            const duration = `${hoursSlept}h ${minutesSlept}m`;

            // Convert time to 12-hour format with AM/PM

            return { sleepTime, wakeTime, duration };
        }
    }).then((result) => {
        if (result.isConfirmed) {
            const { duration,wakeTime,sleepTime } = result.value;
				

            Swal.fire("Sleep Schedule", 
                `${duration} of sleep `, 
                "success"
            ).then((msg)=>{
				if(msg.isConfirmed){
					   putSleepInfo(sleepTime,wakeTime,duration);
				}	
			});
        }
    });
}
function getWeeklyWalkData(){
	fetch("/kunto/weekly_walk")
	.then((res)=>{
		if(res.ok){
			return res.json();
		}
	})
	.then((data)=>{
		updateWalkChart(data)
	})
}

function checkIfExist(type,tit){
	return notifications[type].some(obj=>{
		return obj.title==tit;
	});
}

function putKilometer(result){
	                fetch(`/kunto/log_kilometers?kilometer=${result}`)
	                .then((res)=>{
						if(res.ok){
							
							return res.json();
						}
					})
                  .then(data =>{
					console.log('Server response:', data);
					let val=Number(((data.total_walk / data.goal_walk) * 100))
					let percentage =val > 100 ? 100 : val;
					updateChart(percentage);
					   if (percentage >= 100) {
        					let obj = {
            					title: "KM Goal",
            					message: "You have Reached Your KM Goal"
       					 };
       					         if(!checkIfExist("activity","KM Goal")){
			notifications.activity.push(obj);

		}
        

    }
					if(!checkIfExist("activity","KM Goal")){
						
					}
					KM_WALKED.innerText=data.total_walk.toFixed(2)+"km / "+data.goal_walk+"km";
					
							getWeeklyWalkData();
							getWeeklyWorkoutTime();
				  } )
                  .catch(error => console.log('Error:', error));
}


 function logKilometers() {
        Swal.fire({
            title: 'Log Your Walk',
            input: 'number',
            inputAttributes: {
                min: 0,
                step: 0.1,
                placeholder: 'Enter kilometers walked'
            },
            showCancelButton: true,
            confirmButtonText: 'Log',
            cancelButtonText: 'Cancel',
            preConfirm: (km) => {
                if (!km || km <= 0) {
                    Swal.showValidationMessage('Please enter a valid number!');
                }
                return km;
            }
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire({
                    title: 'Success!',
                    text: `You logged ${result.value} km.`,
                    icon: 'success'
                })
                .then((res)=>{
					if(res.isConfirmed){
						  	putKilometer(result.value);
						  	

					}
				})
              

                // Send data to the backend (example using fetch)

            }
        });
    }



PLUS_SLEEP.addEventListener("click",(ev)=>{

	askSleepTime();

    
})

PLUS_DISTANCE.addEventListener("click",()=>{
	logKilometers();
})


function confirmAction() {
    
}
function putOtherActivitiesInfo(totTime){
	
	
	Swal.fire({
        title: "Are you sure?",
        text: "You won't be able to revert this!",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, save it!"
    }).then((result) => {
        if (result.isConfirmed) {
			
			
			
	fetch("/kunto/other_activities",{
	    method: "POST", // Specify method if needed
    	headers: {
        "Content-Type": "application/json"
    },
    	body: JSON.stringify({
        activityName: OTHER_ACTIVITY_NAME.value,
        activityCal:OTHER_ACTIVITY_CAL.value.slice(0,3),        
        activityDate: OTHER_ACTIVITY_DATE.value,
        TimeTaken:totTime,
    })
})
.then((res)=>{
		if(res.ok){
		return res.json();
	}
}).then((msg)=>{
	if(msg.isInserted){
		            Swal.fire(
                "saved!",
                "Your Activity has been saved."
            );
            console.log("heeeeeeeeeeeeeeeeeeeeeeee")
            getOneMonthCalorieData(ONE_MONTH_CAL);
	}
	
})
			

            // Perform your delete action here
        }	
    });
	
	


}


function isFutureDate(userInput) {
    const today = new Date(); // Get today's date
    today.setHours(0, 0, 0, 0); // Reset time to midnight for accurate comparison

    const userDate = new Date(userInput); // Convert user input to Date object

    return userDate.getTime() > today.getTime(); // Check if the date is in the future
}

MAN_CAL_SAVE_BTN.addEventListener("click",()=>{
	if(isFutureDate(OTHER_ACTIVITY_DATE.value)){
			console.log("saved")

			putOtherActivitiesInfo(AC_DURATION.value);

	}
	else{
		dispPopup("Please check the Date!",2000)
	}
})


