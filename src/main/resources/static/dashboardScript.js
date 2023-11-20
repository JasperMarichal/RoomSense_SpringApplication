const date = document.getElementById("dateTime");
const timePeriod = document.getElementById("timePeriod");

let tempTimeToUse = [];
let tempDataToUse = [];

let humidTimeToUse = [];
let humidDataToUse = [];

let CO2TimeToUse = [];
let CO2DataToUse = [];

let tempChartCanvas;
let humidChartCanvas;
let CO2ChartCanvas;

let temperature = false;
let humidity = false;
let CO2 = false;


init()

/**
 * Checks if data is available for a datatype and adjusts variables as needed
 * Adds event listeners to check for changes to start date or time period to show data for
 */
function init() {
    temperature = tempList != null;
    humidity = humidList != null;
    CO2 = CO2List != null;

    getData();

    // date.addEventListener("change", getData)
    // timePeriod.addEventListener("change", getData)
}

/**
 * Fetches data for the data-types that have been defined based on the time period selected by the user,
 * by default from last reading for 10 minutes before, if the end time is before the first recorded datapoint,
 * the end time becomes the first recorded datapoint
 *
 * Calls methods to sort the data for the different data-types
 */
function getData() {

    if(temperature) prepareTemperatureData();
    if(humidity) prepareHumidityData();
    if(CO2) prepareCO2Data();

}

function prepareTemperatureData() {
    tempTimeToUse = [];
    tempDataToUse = [];

    let firstTime = Date.parse(tempList_rawTimes[0]);

    for (let i = 0; i < tempList_rawTimes.length; i++){
        let time = Date.parse(tempList_rawTimes[i]);
        let convertedTime = Math.round((time - firstTime) / 1000);

        if ((tempList[i] !== tempList[i - 1] || tempList[i] !== tempList[i + 1] ||
                (tempDataToUse.length === 0) || (i === tempList_rawTimes.length - 2) ||
                tempTimeToUse[tempTimeToUse.length - 1] - convertedTime < -50) &&
            !tempTimeToUse.includes(convertedTime)) {

            tempTimeToUse.push(convertedTime);
            tempDataToUse.push(tempList[i]);
        }
    }

    getStatistics(tempList, tempList_rawTimes, "temp", "temperature")

    temperatureChart();
}

function prepareHumidityData() {
    humidTimeToUse = [];
    humidDataToUse = [];

    let firstTime = Date.parse(humidList_rawTimes[0]);

    for (let i = 0; i < humidList_rawTimes.length; i++){
        let time = Date.parse(humidList_rawTimes[i]);
        let convertedTime = Math.round((time - firstTime) / 1000);

        if ((humidList[i] !== humidList[i - 1] || humidList[i] !== humidList[i + 1] ||
                (humidDataToUse.length === 0) || (i === humidList_rawTimes.length - 2) ||
                humidTimeToUse[humidTimeToUse.length - 1] - convertedTime < -50) &&
            !humidTimeToUse.includes(convertedTime)) {

            humidTimeToUse.push(convertedTime);
            humidDataToUse.push(humidList[i]);
        }
    }

    getStatistics(humidList, humidList_rawTimes, "humid", "humidity");

    humidityChart();
}

function prepareCO2Data() {
    CO2TimeToUse = [];
    CO2DataToUse = [];

    let firstTime = Date.parse(CO2List_rawTimes[0]);

    for (let i = 0; i < CO2List_rawTimes.length; i++){
        let time = Date.parse(CO2List_rawTimes[i])
        let convertedTime = Math.round((time - firstTime) / 1000);

        if ((CO2List[i] !== CO2List[i - 1] || CO2List[i] !== CO2List[i + 1] ||
                (CO2DataToUse.length === 0) || (i === CO2List_rawTimes.length - 2)||
                CO2TimeToUse[CO2TimeToUse.length - 1] - convertedTime < -50) &&
            !CO2TimeToUse.includes(convertedTime)) {

            CO2TimeToUse.push(convertedTime)
            CO2DataToUse.push(CO2List[i])
        }
    }

    getStatistics(CO2List, CO2List_rawTimes, "CO2", "CO2");

    CO2Chart();
}

/**
 * Based on all the data and time for the dataType this method gets the maximum, minimum and average values and
 * provides recommendations and warnings based on those.
 *
 * @param allDataInRange All the data within the given time period available to be used
 * @param allTimeInRange All the time within the given time period available to be used
 * @param textID The start of the ID for the text objects for recommendations and insights
 * @param dataType The qualified name of the data type
 */
function getStatistics(allDataInRange, allTimeInRange, textID, dataType){
    const textObject = document.getElementById(textID + "Stats");
    const warnObject = document.getElementById(textID + "Warn");
    const recommendObject = document.getElementById(textID + "Recommend");

    let total = 0;
    let minData = 100;
    let maxData = 0;

    allDataInRange.forEach(data => {
        total += data;
        if (data < minData) minData = data;
        if (data > maxData) maxData = data;
    })

    if (allDataInRange.length === 0){
        textObject.innerHTML = "No Data For Time Period Available";
    } else {
        textObject.innerHTML = "Average " + dataType + ": " + Math.round(total / allDataInRange.length) +
            "<br>" + "Minimum " + dataType + ": " + minData +
            "<br>" + "Maximum " + dataType + ": " + maxData + "<br><br>" +
            "First reading time: " + getDateTimeString(allTimeInRange, 0) +
            "<br>" + "Last reading time: " + getDateTimeString(allTimeInRange, allTimeInRange.length - 1);

        warnObject.innerHTML = "";
        recommendObject.innerHTML = "";

        switch (dataType){
            case "temperature": {
                if (maxData > 30) warnObject.innerHTML = warnObject.innerHTML + "<br>Maximum temperature is too high consider cooling the room!<br>"
                if (minData < 20) warnObject.innerHTML = warnObject.innerHTML + "<br>Minimum temperature is too low consider heating the room!<br>"
                if (total / allDataInRange.length > 30) warnObject.innerHTML = warnObject.innerHTML + "<br>Average temperature is too high consider cooling the room!<br>"
                if (total / allDataInRange.length < 20) warnObject.innerHTML = warnObject.innerHTML + "<br>Average temperature is too low consider heating the room!<br>"

                if (maxData > 24 && !(total / allDataInRange.length > 24)) recommendObject.innerHTML = recommendObject.innerHTML + "Maximum temperature is getting too high consider maintaining between 20°C and 24°C for an optimal work environment.<br>"
                else if (total / allDataInRange.length > 24 && !(maxData > 24)) recommendObject.innerHTML = recommendObject.innerHTML + "Average temperature is getting too high consider maintaining between 20°C and 24°C for an optimal work environment.<br>"
                else if (maxData > 24 && total / allDataInRange.length > 24) recommendObject.innerHTML = recommendObject.innerHTML + "Average and Maximum temperature is getting too high consider maintaining between 20°C and 24°C for an optimal work environment.<br>"

                break;
            }
            case "humidity": {
                if (maxData > 70) warnObject.innerHTML = warnObject.innerHTML + "<br>Maximum humidity is too high consider using a dehumidifier!<br>"
                if (minData < 30) warnObject.innerHTML = warnObject.innerHTML + "<br>Minimum humidity is too low consider using a humidifier!<br>"
                if (total / allDataInRange.length > 70) warnObject.innerHTML = warnObject.innerHTML + "<br>Average humidity is too high consider using a dehumidifier!<br>"
                if (total / allDataInRange.length < 30) warnObject.innerHTML = warnObject.innerHTML + "<br>Average humidity is too low consider using a humidifier!<br>"

                if (maxData > 60 && !(total / allDataInRange.length > 60)) recommendObject.innerHTML = recommendObject.innerHTML + "Maximum humidity is getting too high consider maintaining between 40% and 60% humidity for an optimal work environment.<br>"
                else if (total / allDataInRange.length > 60 && !(maxData > 60)) recommendObject.innerHTML = recommendObject.innerHTML + "Average humidity is getting too high consider maintaining between 40% and 60% humidity for an optimal work environment.<br>"
                else if (maxData > 60 && total / allDataInRange.length > 60) recommendObject.innerHTML = recommendObject.innerHTML + "Average and Maximum humidity is getting too high consider maintaining between 40% and 60% humidity for an optimal work environment.<br>"

                break;
            }
            case "CO2": {
                if (maxData > 5000) warnObject.innerHTML = warnObject.innerHTML + "Maximum CO2 concentration is too high, ventilate the room, do not maintain this concentration for over 8 hours, there is a risk of serious health issues!<br>"
                if (total / allDataInRange.length > 5000) warnObject.innerHTML = warnObject.innerHTML + "Average CO2 concentration is too high, ventilate the room, do not maintain this concentration for over 8 hours, there is a risk of serious health issues!<br>"

                if (maxData > 1000 && !(total / allDataInRange.length > 1000)) recommendObject.innerHTML = recommendObject.innerHTML + "Maximum CO2 concentration is getting too high, consider ventilating the room, maintain a concentration of less than 1000ppm.<br>"
                else if (total / allDataInRange.length > 1000 && !(maxData > 1000)) recommendObject.innerHTML = recommendObject.innerHTML + "Average CO2 concentration is getting too high, consider ventilating the room, maintain a concentration of less than 1000ppm.<br>"
                else if (maxData > 1000 && total / allDataInRange.length > 1000) recommendObject.innerHTML = recommendObject.innerHTML + "Average and Maximum CO2 concentration is getting too high, consider ventilating the room, maintain a concentration of less than 1000ppm.<br>"

                break;
            }
        }
    }
}

/**
 * Based on a given time returns the formatted version of that time to be in dd/MM/yyyy hh:mm:ss format
 *
 * @param timeArray The array of times
 * @param position The specific time to use
 * @returns {string} The formatted date time
 */
function getDateTimeString(timeArray, position) {
    let date = new Date(timeArray[position])
    return ("00" + date.getDate()).slice(-2) + "/" + ("00" + (date.getMonth() + 1)).slice(-2) + "/" + date.getFullYear() + " " +
        ("00" + date.getHours()).slice(-2) + ":" + ("00" + date.getMinutes()).slice(-2) + ":" + ("00" + date.getSeconds()).slice(-2)
}


/**
 * Displays the filtered data on the temperature graph.
 */
function temperatureChart() {
    if (tempChartCanvas){
        tempChartCanvas.destroy();
    }

    tempChartCanvas = new Chart("tempChart", {
        type: "line",
        data: {
            labels: tempTimeToUse,
            datasets: [{
                backgroundColor: "rgba(255,0,0,1)",
                borderColor: "rgba(255,0,0,1)",
                borderWidth: 4,
                fill: false,
                data: tempDataToUse,
                label: "Temperature",
                tension: 0
            }]
        },
        options: {

        }
    });
}

/**
 * Displays the filtered data on the humidity graph.
 */
function humidityChart() {
    if (humidChartCanvas){
        humidChartCanvas.destroy();
    }

    humidChartCanvas = new Chart("humidChart", {
        type: "line",
        data: {
            labels: humidTimeToUse,
            datasets: [{
                backgroundColor: "rgba(0,128,255,1)",
                borderColor: "rgba(0,128,255,1)",
                borderWidth: 4,
                fill: false,
                data: humidDataToUse,
                label: "Humidity",
                tension: 0
            }]
        },
        options: {

        }
    });
}

/**
 * Displays the filtered data on the CO2 graph.
 */
function CO2Chart() {
    if (CO2ChartCanvas){
        CO2ChartCanvas.destroy();
    }

    CO2ChartCanvas = new Chart("CO2Chart", {
        type: "line",
        data: {
            labels: CO2TimeToUse,
            datasets: [{
                backgroundColor: "rgba(0,128,0,1)",
                borderColor: "rgba(0,128,0,1)",
                borderWidth: 4,
                fill: false,
                data: CO2DataToUse,
                label: "CO2",
                tension: 0
            }]
        },
        options: {

        }
    });
}



