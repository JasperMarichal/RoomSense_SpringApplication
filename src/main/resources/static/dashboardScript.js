const form = document.getElementById("timeForm");
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

let endOfTimePeriodTemp;
let endOfTimePeriodHumid;
let endOfTimePeriodCO2;

let startTempTime;
let lastTempTime;
let startHumidTime;
let lastHumidTime;
let startCO2Time;
let lastCO2Time;

let temperature = false;
let humidity = false;
let CO2 = false;


init()

function init() {
    temperature = tempList != null;
    humidity = humidList != null;
    CO2 = CO2List != null;

    // console.log(temperature + " " + humidity + " " + CO2)

    getData();

    date.addEventListener("change", getData)
    timePeriod.addEventListener("change", getData)
}

function getData() {
    if(temperature) startTempTime = Date.parse(tempList_rawTimes[0]);
    if(temperature) lastTempTime = Date.parse(tempList_rawTimes[tempList_rawTimes.length - 1]);
    if(humidity) startHumidTime = Date.parse(humidList_rawTimes[0]);
    if(humidity) lastHumidTime = Date.parse(humidList_rawTimes[humidList_rawTimes.length - 1]);
    if(CO2) startCO2Time = Date.parse(CO2List_rawTimes[0]);
    if(CO2) lastCO2Time = Date.parse(CO2List_rawTimes[CO2List_rawTimes.length - 1]);

    // console.log(date.value)
    if (!date.value){
        if(temperature) endOfTimePeriodTemp = lastTempTime - (timePeriod.value * 60000);
        if(humidity) endOfTimePeriodHumid = lastHumidTime - (timePeriod.value * 60000);
        if(CO2) endOfTimePeriodCO2 = lastCO2Time - (timePeriod.value * 60000);

        if (endOfTimePeriodTemp < startTempTime && temperature) endOfTimePeriodTemp = startTempTime;
        if (endOfTimePeriodHumid < startHumidTime && humidity) endOfTimePeriodHumid = startHumidTime;
        if (endOfTimePeriodCO2 < startCO2Time && CO2) endOfTimePeriodCO2 = startCO2Time;

        if(temperature) sortTemperatureChartData(lastTempTime, endOfTimePeriodTemp);

        if(humidity) sortHumidityChartData(lastHumidTime, endOfTimePeriodHumid);

        if(CO2) sortCO2ChartData(lastCO2Time, endOfTimePeriodCO2);
    } else {
        const startDate = Date.parse(date.value)

        if(temperature) endOfTimePeriodTemp = startDate - (timePeriod.value * 60000);
        if(humidity) endOfTimePeriodHumid = startDate - (timePeriod.value * 60000);
        if(CO2) endOfTimePeriodCO2 = startDate - (timePeriod.value * 60000)

        if (endOfTimePeriodTemp < startTempTime && temperature) endOfTimePeriodTemp = startTempTime;
        if (endOfTimePeriodHumid < startHumidTime && humidity) endOfTimePeriodHumid = startHumidTime;
        if (endOfTimePeriodCO2 < startCO2Time && CO2) endOfTimePeriodCO2 = startCO2Time;

        if(temperature) sortTemperatureChartData(startDate, endOfTimePeriodTemp);

        if(humidity) sortHumidityChartData(startDate, endOfTimePeriodHumid);

        if(CO2) sortCO2ChartData(lastCO2Time, endOfTimePeriodCO2);
    }

}


function sortTemperatureChartData(startOfRange, endOfRange) {
    tempTimeToUse = [];
    tempDataToUse = [];

    for (let i = 0; i < tempList_rawTimes.length; i++){
        let time = Date.parse(tempList_rawTimes[i])
        if (time < startOfRange && time > endOfRange) {
            let convertedTime = Math.round((time - endOfRange) / 1000);

            if ((tempList[i] !== tempList[i - 1] || tempList[i] !== tempList[i + 1] ||
                (tempDataToUse.length === 0) || (i === tempList_rawTimes.length - 2) ||
                tempTimeToUse[tempTimeToUse.length - 1] - convertedTime < -50) &&
                !tempTimeToUse.includes(convertedTime)) {

                tempTimeToUse.push(convertedTime)
                tempDataToUse.push(tempList[i])
            }
        }
    }

    temperatureChart();
}

function sortHumidityChartData(startOfRange, endOfRange) {
    humidTimeToUse = [];
    humidDataToUse = [];

    for (let i = 0; i < humidList_rawTimes.length; i++){
        let time = Date.parse(humidList_rawTimes[i])
        if (time < startOfRange && time > endOfRange) {
            let convertedTime = Math.round((time - endOfRange) / 1000);

            if ((humidList[i] !== humidList[i - 1] || humidList[i] !== humidList[i + 1] ||
                (humidDataToUse.length === 0) || (i === humidList_rawTimes.length - 2)||
                humidTimeToUse[humidTimeToUse.length - 1] - convertedTime < -50) &&
                !humidTimeToUse.includes(convertedTime)) {

                humidTimeToUse.push(convertedTime)
                humidDataToUse.push(humidList[i])
            }
        }
    }

    humidityChart();
}

function sortCO2ChartData(startOfRange, endOfRange) {
    CO2TimeToUse = [];
    CO2DataToUse = [];

    for (let i = 0; i < CO2List_rawTimes.length; i++){
        let time = Date.parse(CO2List_rawTimes[i])
        if (time < startOfRange && time > endOfRange) {
            let convertedTime = Math.round((time - endOfRange) / 1000);

            if ((CO2List[i] !== CO2List[i - 1] || CO2List[i] !== CO2List[i + 1] ||
                (CO2DataToUse.length === 0) || (i === CO2List_rawTimes.length - 2)||
                CO2TimeToUse[CO2TimeToUse.length - 1] - convertedTime < -50) &&
                !CO2TimeToUse.includes(convertedTime)) {

                CO2TimeToUse.push(convertedTime)
                CO2DataToUse.push(CO2List[i])
            }
        }
    }

    CO2Chart();
}


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

function CO2Chart() {
    if (CO2ChartCanvas){
        CO2ChartCanvas.destroy();
    }

    CO2ChartCanvas = new Chart("CO2Chart", {
        type: "line",
        data: {
            labels: CO2List_timestamp,
            datasets: [{
                backgroundColor: "rgba(0,128,0,1)",
                borderColor: "rgba(0,128,0,1)",
                borderWidth: 4,
                fill: false,
                data: CO2List,
                label: "CO2",
                tension: 0
            }]
        },
        options: {

        }
    });
}



