import {minMaxAverageGraph, getDateTimeString} from "./gaugeGraph.js";

let spikeTimeToUse = [];
let spikeDataToUse = [];

let spikeChartCanvas;

init()

function init() {
    prepareSpikeData();
}

/**
 * Prepares the data to display on the spike chart, converts the time to be seconds since first measurement,
 * then calls the method to show the statistics and the chart itself.
 */
function prepareSpikeData() {
    spikeTimeToUse = [];
    spikeDataToUse = [];

    let firstTime = Date.parse(spikeListTimes[0]);

    for (let i = 0; i < spikeListTimes.length; i++){
        let time = Date.parse(spikeListTimes[i]);
        let convertedTime = Math.round((time - firstTime) / 1000);

        spikeTimeToUse.push(convertedTime);
        spikeDataToUse.push(spikeList[i]);
    }

    getStatistics(spikeList, spikeListTimes, "spike", "Amplitude")

    spikeChart();
}

/**
 * get the min, max and average values for the spike and provides text based on them as well as calls the method from
 * gaugeChart.js to show the gauge chart.
 * @param allDataInRange All the data to calculate from
 * @param allTimeInRange All the times to calculate from
 * @param textID Identifier for the field names
 * @param dataType Text value of identifier to display on graph and in text
 */
function getStatistics(allDataInRange, allTimeInRange, textID, dataType){
    const textObject = document.getElementById(textID + "Stats");

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

        minMaxAverageGraph(minData, maxData, Math.round(total / allDataInRange.length), textID, dataType);
    }
}

/**
 * Show chart for spike based on processed data.
 */
function spikeChart() {
    if (spikeChartCanvas){
        spikeChartCanvas.destroy();
    }

    spikeChartCanvas = new Chart("spikeChart", {
        type: "line",
        data: {
            labels: spikeTimeToUse,
            datasets: [{
                backgroundColor: "rgba(255,0,0,1)",
                borderColor: "rgba(255,0,0,1)",
                borderWidth: 4,
                fill: false,
                data: spikeDataToUse,
                label: "Sound Spike",
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
                        labelString: "Amplitude"
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