async function init(){
		getLogInfo(formattedDate);
		getOneMonthCalorieData();
		getWeeklyWorkoutTime();
		getWeeklyWalkData();
		getCalData();
		updateFoodMeasurement();
		await updateStars();
		TRACKING_DATE.innerText=DAYS[date_now.getDay()]+","+nowDay.slice(8)+" "+MONTHS[date_now.getMonth()]
		fetchUserProfile();
};
init();