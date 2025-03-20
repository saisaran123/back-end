let currentDate = new Date();
let selectedDate = null;
const today = new Date();

function renderCalendar() {
    const monthYear = document.getElementById("cale-monthYear");
    const datesContainer = document.getElementById("cale-dates");
    
    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();
    monthYear.textContent = `${currentDate.toLocaleString('default', { month: 'long' })} ${year}`;

    datesContainer.innerHTML = "";
    const firstDay = new Date(year, month, 1).getDay();
    const lastDate = new Date(year, month + 1, 0).getDate();
    
    for (let i = 0; i < firstDay; i++) {
        datesContainer.innerHTML += `<div></div>`;
    }
    for (let i = 1; i <= lastDate; i++) {
        let isToday = year === today.getFullYear() && month === today.getMonth() && i === today.getDate();
        let isSelected = selectedDate && selectedDate.year === year && selectedDate.month === month && selectedDate.day === i;
        datesContainer.innerHTML += `<div class="cale-date ${isToday ? 'cale-today' : ''}  ${isSelected ? 'cale-selected' : ''}" onclick="selectDate(${year}, ${month}, ${i}, this)">${i}</div>`;
    }
}

function selectDate(year, month, day, element) {

    document.querySelector(".cale-selected")?.classList.remove("cale-selected");
    selectedDate = { year, month, day };
    element.classList.add("cale-selected");
    console.log(`${year}-${(month + 1)>=10?(month + 1):'0'+(month + 1)}-${day>=10?day:'0'+day}`);
    dataShowndate=`${year}-${(month + 1)>=10?(month + 1):'0'+(month + 1)}-${day>=10?day:'0'+day}`;
    							checkCursor();
	if(new Date(dataShowndate)<=new Date(formattedDate)){
			getLogInfo(`${year}-${(month + 1)>=10?(month + 1):'0'+(month + 1)}-${day>=10?day:'0'+day}`);

	}
}


function prevMonth() {
    currentDate.setMonth(currentDate.getMonth() - 1);
    renderCalendar();
}

function nextMonth() {
    currentDate.setMonth(currentDate.getMonth() + 1);
    renderCalendar();
}

function goToToday() {
    currentDate = new Date();
    selectedDate = { year: today.getFullYear(), month: today.getMonth(), day: today.getDate() };
    renderCalendar();
}

function goToYesterday() {
    const yesterday = new Date();
    yesterday.setDate(yesterday.getDate() - 1);
    currentDate = new Date(yesterday);
    selectedDate = { year: yesterday.getFullYear(), month: yesterday.getMonth(), day: yesterday.getDate() };
    renderCalendar();
}
renderCalendar();



