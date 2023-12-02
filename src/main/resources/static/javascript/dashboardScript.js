import {minMaxAverageGraph, getDateTimeString} from "./gaugeGraph.js";

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


init()

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
    if(noise) prepareNoiseData();

}

/**
 * Prepares the temperature data to get statistics and show on the graphs,
 * this method converts timestamps into seconds since the first reading
 * and then pushes non-duplicate data-points into arrays, then calls a method to display the statistics
 */
function prepareTemperatureData() {
    tempTimeToUse = [];
    tempDataToUse = [];

    let firstTime = Date.parse(tempListTimes[0]);

    for (let i = 0; i < tempListTimes.length; i++){
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

    getStatistics(tempList, tempListTimes, "temp", "Temperature")

    temperatureChart();
}

/**
 * Prepares the humidity data to get statistics and show on the graphs,
 * this method converts timestamps into seconds since the first reading
 * and then pushes non-duplicate data-points into arrays, then calls a method to display the statistics
 */
function prepareHumidityData() {
    humidTimeToUse = [];
    humidDataToUse = [];

    let firstTime = Date.parse(humidListTimes[0]);

    for (let i = 0; i < humidListTimes.length; i++){
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

    getStatistics(humidList, humidListTimes, "humid", "Humidity");

    humidityChart();
}

/**
 * Prepares the CO2 data to get statistics and show on the graphs,
 * this method converts timestamps into seconds since the first reading
 * and then pushes non-duplicate data-points into arrays, then calls a method to display the statistics
 */
function prepareCO2Data() {
    CO2TimeToUse = [];
    CO2DataToUse = [];

    let firstTime = Date.parse(CO2ListTimes[0]);

    for (let i = 0; i < CO2ListTimes.length; i++){
        let time = Date.parse(CO2ListTimes[i])
        let convertedTime = Math.round((time - firstTime) / 1000);

        if ((CO2List[i] !== CO2List[i - 1] || CO2List[i] !== CO2List[i + 1] ||
                (CO2DataToUse.length === 0) || (i === CO2ListTimes.length - 2)||
                CO2TimeToUse[CO2TimeToUse.length - 1] - convertedTime < -50) &&
            !CO2TimeToUse.includes(convertedTime)) {

            CO2TimeToUse.push(convertedTime)
            CO2DataToUse.push(CO2List[i])
        }
    }

    getStatistics(CO2List, CO2ListTimes, "CO2", "CO2");

    CO2Chart();
}

/**
 * Prepares the noise data to get statistics and show on the graphs,
 * this method converts timestamps into seconds since the first reading
 * and then pushes non-duplicate data-points into arrays, then calls a method to display the statistics
 */
function prepareNoiseData() {
    noiseTimeToUse = [];
    noiseDataToUse = [];

    let firstTime = Date.parse(noiseListTimes[0]);

    for (let i = 0; i < noiseListTimes.length; i++){
        let time = Date.parse(noiseListTimes[i])
        let convertedTime = Math.round((time - firstTime) / 1000);

        if ((noiseList[i] !== noiseList[i - 1] || noiseList[i] !== noiseList[i + 1] ||
                (noiseDataToUse.length === 0) || (i === noiseListTimes.length - 2)||
                noiseTimeToUse[CO2TimeToUse.length - 1] - convertedTime < -50) &&
            !noiseTimeToUse.includes(convertedTime)) {

            noiseTimeToUse.push(convertedTime)
            noiseDataToUse.push(noiseList[i])
        }
    }

    getStatistics(noiseList, noiseListTimes, "noise", "Noise");

    noiseChart();
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
function getStatistics(allDataInRange, allTimeInRange, textID, dataType){
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

    if (allDataInRange.length === 0){
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

        switch (dataType){
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
            plugins : {legend: false},
            scales: {
                yAxes: [{
                    ticks: {
                        callback: function(value) {if (value % 1 === 0) {return value;}}
                    },
                    scaleLabel: {
                        display: true,
                        labelString: "Temperature (°C)"
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
            plugins : {legend: false},
            scales: {
                yAxes: [{
                    ticks: {
                        callback: function(value) {if (value % 1 === 0) {return value;}}
                    },
                    scaleLabel: {
                        display: true,
                        labelString: "Humidity (%)"
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
            plugins : {legend: false},
            scales: {
                yAxes: [{
                    ticks: {
                        callback: function(value) {if (value % 1 === 0) {return value;}}
                    },
                    scaleLabel: {
                        display: true,
                        labelString: "CO2 Concentration (ppm)"
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

/**
 * Displays the filtered data on the noise graph.
 */
function noiseChart() {
    if (noiseChartCanvas){
        noiseChartCanvas.destroy();
    }

    noiseChartCanvas = new Chart("noiseChart", {
        type: "line",
        data: {
            labels: noiseTimeToUse,
            datasets: [{
                backgroundColor: "rgba(200,128,0,1)",
                borderColor: "rgba(200,128,0,1)",
                borderWidth: 4,
                fill: false,
                data: noiseDataToUse,
                label: "Noise",
                tension: 0
            }]
        },
        options: {
            plugins : {legend: false},
            scales: {
                yAxes: [{
                    ticks: {
                        callback: function(value) {if (value % 1 === 0) {return value;}}
                    },
                    scaleLabel: {
                        display: true,
                        labelString: "Noise"
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