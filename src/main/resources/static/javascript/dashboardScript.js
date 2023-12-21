import {getDateTimeString, minMaxAverageGraph} from "./gaugeGraph.js";

let tempTimeToUse = [];
let tempDataToUse = [];

let humidTimeToUse = [];
let humidDataToUse = [];

let CO2TimeToUse = [];
let CO2DataToUse = [];

let noiseTimeToUse = [];
let noiseDataToUse = [];

let tempChartCanvas;
let humidChartCanvas;
let CO2ChartCanvas;
let noiseChartCanvas;

let temperature = false;
let humidity = false;
let CO2 = false;
let noise = false;

const VENTILATION_ALERT_ID = "ventilationAlertId";

init()

setInterval(ventilationSignal, 15000);

/**
 * Checks if data is available for a datatype and adjusts variables as needed
 * Calls method to show data on those graphs
 */
function init() {
    temperature = tempList != null;
    humidity = humidList != null;
    CO2 = CO2List != null;
    noise = noiseList != null;

    getData();
    ventilationSignal();
}

/**
 * Fetches data for the data-types that have been defined based on the time period selected by the user,
 * by default from last reading for 10 minutes before, if the end time is before the first recorded datapoint,
 * the end time becomes the first recorded datapoint
 *
 * Calls methods to sort the data for the different data-types
 */
function getData() {
    console.log(CO2List)

    if (temperature) {
        prepareTemperatureData();
        getStatistics(tempList, tempListTimes, "temp", "Temperature");
        tempChartCanvas = showChart(tempChartCanvas, "tempChart", "rgba(255,0,0,1)", tempTimeToUse, tempDataToUse, "Temperature", "(°C)", 2.5, 10, 40);
    }
    if (humidity) {
        prepareHumidityData();
        getStatistics(humidList, humidListTimes, "humid", "Humidity");
        humidChartCanvas = showChart(humidChartCanvas, "humidChart", "rgba(0,128,255,1)", humidTimeToUse, humidDataToUse, "Humidity", "(%)", 5, 20, 80);
    }
    if (CO2) {
        prepareCO2Data();
        getStatistics(CO2List, CO2ListTimes, "CO2", "CO2");
        CO2ChartCanvas = showChart(CO2ChartCanvas, "CO2Chart", "rgba(0,128,0,1)", CO2TimeToUse, CO2DataToUse, "CO2", "Concentration (ppm)", 250, 500, 5000);
    }
    if (noise) {
        prepareNoiseData();
        getStatistics(noiseList, noiseListTimes, "noise", "Noise");
        noiseChartCanvas = showChart(noiseChartCanvas, "noiseChart", "rgba(200,128,0,1)", noiseTimeToUse, noiseDataToUse, "Noise", "", 50, 0, 512);
    }
}


/**
 * Prepares the temperature data to get statistics and show on the graphs,
 * this method converts timestamps into seconds since the first reading
 * and then pushes non-duplicate data-points into arrays
 */
function prepareTemperatureData() {
    tempTimeToUse = [];
    tempDataToUse = [];

    let firstTime = Date.parse(tempListTimes[0]);

    for (let i = 0; i < tempListTimes.length; i++) {
        let time = Date.parse(tempListTimes[i]);
        let convertedTime = Math.round((time - firstTime) / 1000);

        if ((tempList[i] !== tempList[i - 1] || tempList[i] !== tempList[i + 1] ||
                (tempDataToUse.length === 0) || (i === tempListTimes.length - 2) ||
                tempTimeToUse[tempTimeToUse.length - 1] - convertedTime < -50) &&
            !tempTimeToUse.includes(convertedTime)) {

            tempTimeToUse.push(convertedTime);
            tempDataToUse.push(tempList[i]);
        }
    }
}

/**
 * Prepares the humidity data to get statistics and show on the graphs,
 * this method converts timestamps into seconds since the first reading
 * and then pushes non-duplicate data-points into arrays
 */
function prepareHumidityData() {
    humidTimeToUse = [];
    humidDataToUse = [];

    let firstTime = Date.parse(humidListTimes[0]);

    for (let i = 0; i < humidListTimes.length; i++) {
        let time = Date.parse(humidListTimes[i]);
        let convertedTime = Math.round((time - firstTime) / 1000);

        if ((humidList[i] !== humidList[i - 1] || humidList[i] !== humidList[i + 1] ||
                (humidDataToUse.length === 0) || (i === humidListTimes.length - 2) ||
                humidTimeToUse[humidTimeToUse.length - 1] - convertedTime < -50) &&
            !humidTimeToUse.includes(convertedTime)) {

            humidTimeToUse.push(convertedTime);
            humidDataToUse.push(humidList[i]);
        }
    }
}

/**
 * Prepares the CO2 data to get statistics and show on the graphs,
 * this method converts timestamps into seconds since the first reading
 * and then pushes non-duplicate data-points into arrays
 */
function prepareCO2Data() {
    CO2TimeToUse = [];
    CO2DataToUse = [];

    let firstTime = Date.parse(CO2ListTimes[0]);

    for (let i = 0; i < CO2ListTimes.length; i++) {
        let time = Date.parse(CO2ListTimes[i])
        let convertedTime = Math.round((time - firstTime) / 1000);

        if ((CO2List[i] !== CO2List[i - 1] || CO2List[i] !== CO2List[i + 1] ||
                (CO2DataToUse.length === 0) || (i === CO2ListTimes.length - 2) ||
                CO2TimeToUse[CO2TimeToUse.length - 1] - convertedTime < -50) &&
            !CO2TimeToUse.includes(convertedTime)) {

            CO2TimeToUse.push(convertedTime)
            CO2DataToUse.push(CO2List[i])
        }
    }
}

/**
 * Prepares the noise data to get statistics and show on the graphs,
 * this method converts timestamps into seconds since the first reading
 * and then pushes non-duplicate data-points into arrays
 */
function prepareNoiseData() {
    noiseTimeToUse = [];
    noiseDataToUse = [];

    let firstTime = Date.parse(noiseListTimes[0]);

    for (let i = 0; i < noiseListTimes.length; i++) {
        let time = Date.parse(noiseListTimes[i])
        let convertedTime = Math.round((time - firstTime) / 1000);

        if ((noiseList[i] !== noiseList[i - 1] || noiseList[i] !== noiseList[i + 1] ||
                (noiseDataToUse.length === 0) || (i === noiseListTimes.length - 2) ||
                noiseTimeToUse[CO2TimeToUse.length - 1] - convertedTime < -50) &&
            !noiseTimeToUse.includes(convertedTime)) {

            noiseTimeToUse.push(convertedTime)
            noiseDataToUse.push(noiseList[i])
        }
    }
}

/**
 * Based on all the data and time for the dataType this method gets the maximum, minimum and average values and
 * provides recommendations and warnings based on those.
 *
 * For temperature a recommendation is provided when temperature is above 24 degrees and a
 * warning when it is below 20 or above 30 degrees
 *
 * For humidity a recommendation is provided when humidity is above 60% and a warning when it is below 30% or above 70%
 *
 * For CO2 a recommendation is provided when ppm is above 1500 and a warning when it is above 5000
 *
 * This method also calls a method from gaugeChart.js to displaying the gauge graph for the min/max/average values
 *
 * @param allDataInRange All the data within the given time period available to be used
 * @param allTimeInRange All the time within the given time period available to be used
 * @param textID The start of the ID for the text objects for recommendations and insights
 * @param dataType The qualified name of the data type
 */
async function getStatistics(allDataInRange, allTimeInRange, textID, dataType) {
    const textObject = document.getElementById(textID + "Stats");
    const warnObject = document.getElementById(textID + "Warn");
    const recommendObject = document.getElementById(textID + "Recommend");

    let total = 0;
    let minData = 10000;
    let maxData = 0;

    allDataInRange.forEach(data => {
        total += data;
        if (data < minData) minData = data;
        if (data > maxData) maxData = data;
    })

    if (allDataInRange.length === 0) {
        textObject.innerHTML = "No Data For Time Period Available";
    } else {
        textObject.innerHTML = "🟦 Minimum " + dataType + ": " + minData +
            "<br>" + "🟩 Average " + dataType + ": " + Math.round(total / allDataInRange.length) +
            "<br>" + "🟥 Maximum " + dataType + ": " + maxData + "<br><br>" +
            "First reading time: " + getDateTimeString(allTimeInRange, 0) +
            "<br>" + "Last reading time: " + getDateTimeString(allTimeInRange, allTimeInRange.length - 1);

        warnObject.innerHTML = "";
        recommendObject.innerHTML = "";

        minMaxAverageGraph(minData, maxData, Math.round(total / allDataInRange.length), textID, dataType);

        switch (dataType) {
            case "Temperature": {
                if (maxData > 30) warnObject.innerHTML = warnObject.innerHTML + "<br>Maximum temperature is too high consider cooling the room!<br>"
                if (minData < 20) warnObject.innerHTML = warnObject.innerHTML + "<br>Minimum temperature is too low consider heating the room!<br>"
                if (total / allDataInRange.length > 30) warnObject.innerHTML = warnObject.innerHTML + "<br>Average temperature is too high consider cooling the room!<br>"
                if (total / allDataInRange.length < 20) warnObject.innerHTML = warnObject.innerHTML + "<br>Average temperature is too low consider heating the room!<br>"

                if (maxData > 24 && !(total / allDataInRange.length > 24)) recommendObject.innerHTML = recommendObject.innerHTML + "Maximum temperature is getting too high consider maintaining between 20°C and 24°C for an optimal work environment.<br>"
                else if (total / allDataInRange.length > 24 && !(maxData > 24)) recommendObject.innerHTML = recommendObject.innerHTML + "Average temperature is getting too high consider maintaining between 20°C and 24°C for an optimal work environment.<br>"
                else if (maxData > 24 && total / allDataInRange.length > 24) recommendObject.innerHTML = recommendObject.innerHTML + "Average and Maximum temperature is getting too high consider maintaining between 20°C and 24°C for an optimal work environment.<br>"

                break;
            }
            case "Humidity": {
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

                if (maxData > 1500 && !(total / allDataInRange.length > 1500)) recommendObject.innerHTML = recommendObject.innerHTML + "Maximum CO2 concentration is getting too high, consider ventilating the room, maintain a concentration of less than 1500ppm.<br>"
                else if (total / allDataInRange.length > 1500 && !(maxData > 1500)) recommendObject.innerHTML = recommendObject.innerHTML + "Average CO2 concentration is getting too high, consider ventilating the room, maintain a concentration of less than 1500ppm.<br>"
                else if (maxData > 1500 && total / allDataInRange.length > 1500) recommendObject.innerHTML = recommendObject.innerHTML + "Average and Maximum CO2 concentration is getting too high, consider ventilating the room, maintain a concentration of less than 1500ppm.<br>"

                break;
            }
        }
    }
}

function showChart(chartCanvas, chartID, lineColor, labels, data, datasetLabel, yUnits, stepSize, minValue, maxValue) {
    if (chartCanvas) {
        chartCanvas.destroy();
    }

    return new Chart(chartID, {
        type: "line",
        data: {
            labels: labels,
            datasets: [{
                backgroundColor: lineColor,
                borderColor: lineColor,
                borderWidth: 4,
                fill: false,
                data: data,
                label: datasetLabel,
                tension: 0
            }]
        },
        options: {
            plugins: {legend: false},
            scales: {
                yAxes: [{
                    ticks: {
                        callback: function (value) {
                            if (value % 1 === 0) {
                                return value;
                            }
                        },
                        stepSize: stepSize,
                        suggestedMin: minValue,
                        suggestedMax: maxValue,
                    },
                    scaleLabel: {
                        display: true,
                        labelString: datasetLabel + " " + yUnits,
                    }
                }],
                xAxes: [{
                    scaleLabel: {
                        display: true,
                        labelString: "Time (s)"
                    }
                }]
            }
        }
    });
}

const alertPlaceholder = document.getElementById('liveAlertPlaceholder')
const appendAlert = (message, type, id) => {
    const wrapper = document.createElement('div')
    wrapper.innerHTML = [
        `<div id="${id}" class="alert alert-${type} alert-dismissible" role="alert">`,
        `   <div>${message}</div>`,
        '   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>',
        '</div>'
    ].join('')

    alertPlaceholder.append(wrapper)
}

/**
 * Fetches the average values of Co2, Humidity and Temperature over a specified period of time.
 * If one or more of those values are above a specified threshold,
 * passes a signal to the webpage, letting the user know that the room needs to be ventilated.
 * Refresh is done according to the value in refreshInterval.
 */
async function ventilationSignal() {
    if (document.getElementById(VENTILATION_ALERT_ID)) {
        return;
    }

    let pathArray = window.location.pathname.split("/");
    let roomId = pathArray[2];
    let response = await fetch(`/api/sensors?roomId=${roomId}&intervalSeconds=${refreshInterval}`);
    if (!response.ok) {
        throw new Error("HTTP-Error: " + response.status);
    }

    let sensorsOverview = await response.json();

    const showAlert = sensorsOverview.results.some(e => e.aboveThreshold);
    if (showAlert) {
        appendAlert("You need to ventilate the room!", "warning", VENTILATION_ALERT_ID);
    }
}
