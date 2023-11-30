let data;
let colors;

/**
 * This method is responsible for getting everything setup for the gauge chart including getting the colours in place
 * to display the minimum, maximum and average values, it then calls a method to show the gauge chart.
 * @param minValue The minimum value to display on the chart
 * @param maxValue The maximum value to display on the chart
 * @param averageValue The average value to display on the chart
 * @param textID The ID that identifies what kind of data we are dealing with
 * @param dataType The datatype to use in the chart to display the kind of data
 */
export function minMaxAverageGraph(minValue, maxValue, averageValue, textID, dataType){
    let absoluteMin;
    let minRecommend;
    let maxRecommend;
    let absoluteMax;

    switch (textID){
        case "temp": {
            absoluteMin = 0;
            absoluteMax = 40;

            minRecommend = 20;
            maxRecommend = 30;
            break;
        }
        case "humid": {
            absoluteMin = 20/1.8;
            absoluteMax = 90/1.8;

            minRecommend = 30/1.8;
            maxRecommend = 70/1.8;

            minValue = minValue/1.8;
            maxValue = maxValue/1.8;
            averageValue = averageValue/1.8;
            break;
        }
        case "CO2": {
            absoluteMin = 350/150;
            absoluteMax = 8000/150;

            minRecommend = 400/150;
            maxRecommend = 4000/150;

            minValue = minValue/150;
            maxValue = maxValue/150;
            averageValue = averageValue/150;
            break;
        } case "noise": {
            absoluteMin = 0;
            absoluteMax = 1023/25;
            minRecommend = 1;
            maxRecommend = 1022/25;

            minValue = minValue/25;
            maxValue = maxValue/25;
            averageValue = averageValue/25;
            break;
        } case "spike": {
            absoluteMin = 0;
            absoluteMax = 1023/25;
            minRecommend = 1;
            maxRecommend = 1022/25;

            minValue = minValue/25;
            maxValue = maxValue/25;
            averageValue = averageValue/25;
            break;
        }
    }

    let minMaxAverage = [minValue, maxValue, averageValue];
    if (minValue === maxValue && minValue === averageValue){
        minMaxAverage = [minValue];
    } else if (minValue === averageValue && minValue !== maxValue){
        minMaxAverage = [averageValue, maxValue];
    } else if (maxValue === averageValue && maxValue !== minValue){
        minMaxAverage = [minValue, averageValue];
    }
    minMaxAverage.push(minRecommend); minMaxAverage.push(maxRecommend); minMaxAverage.push(absoluteMax);
    minMaxAverage.sort(function(a, b) {return a - b});

    minMaxAverage = minMaxAverage.filter(function(item, pos) {
        return minMaxAverage.indexOf(item) === pos;
    })

    data = [];
    colors = [];
    let rawData = [];

    let outOfRecommendedColor = "silver"

    minMaxAverage.forEach(value => {
        if (value < minRecommend && value > absoluteMin){
            data.push(value - absoluteMin - 1);
            data.push(1);
            colors.push(outOfRecommendedColor);
            valueColors(value, minValue, averageValue, maxValue);
            rawData.push(value);
        } else if (value === minRecommend || value === maxRecommend || value === absoluteMax){
            let valueToAdd = value;
            if (data.length > 0){
                valueToAdd -= rawData[rawData.length - 1];
            }
            if (value === minValue || value === averageValue || value === maxValue) {
                if (value === minRecommend) data.push(valueToAdd-absoluteMin-1);
                else data.push(valueToAdd - 1);
                data.push(1);
                if (value === minRecommend || value === absoluteMax) colors.push(outOfRecommendedColor);
                else if (value === maxRecommend) colors.push("white");

                valueColors(value, minValue, averageValue, maxValue);
            } else {
                if (value === minRecommend) data.push(valueToAdd-absoluteMin);
                else data.push(valueToAdd);
                if (value === minRecommend || value === absoluteMax) colors.push(outOfRecommendedColor);
                else if (value === maxRecommend) colors.push("white");
            }
            rawData.push(value);
        } else if (value > minRecommend && value < maxRecommend){
            data.push(value-rawData[rawData.length - 1]-1);
            data.push(1);
            colors.push("white");
            valueColors(value, minValue, averageValue, maxValue);
            rawData.push(value);
        } else if (value > maxRecommend && value < absoluteMax){
            data.push(value-maxRecommend-1);
            data.push(1);
            colors.push(outOfRecommendedColor);
            valueColors(value, minValue, averageValue, maxValue);
            rawData.push(value);
        } else {
            data.push(value);
            colors = ["black"];
        }
    })

    showGaugeGraph(textID, absoluteMin, absoluteMax, dataType);
}

function valueColors(value, minValue, averageValue, maxValue){
    if (value === minValue){
        colors.push("blue");
    } else if (value === averageValue){
        colors.push("green");
    } else if (value === maxValue){
        colors.push("red");
    } else {
        colors.push("orangered");
    }
}

function showGaugeGraph(textID, absoluteMin, absoluteMax, dataType){
    let chartName = textID + "ValuesDisplay"
    new Chart(chartName, {
        data: {
            datasets: [
                {
                    type: "doughnut",
                    data: data,
                    minValue: absoluteMin,
                    maxValue: absoluteMax,
                    backgroundColor: colors,
                    borderWidth: 2,
                    borderColor: "darkgray"
                },
            ]
        },
        options: {
            cutoutPercentage: 70,
            rotation: Math.PI,
            circumference: Math.PI,
            tooltips: {enabled: false},
            plugins : {
                legend: false,
            },
            title: {
                display: true,
                text: "Min/Max/Average" + " " + dataType,
            },
        }
    })
}

/**
 * Based on a given time returns the formatted version of that time to be in dd/MM/yyyy hh:mm:ss format
 *
 * @param timeArray The array of times
 * @param position The specific time to use
 * @returns {string} The formatted date time
 */
export function getDateTimeString(timeArray, position) {
    let date = new Date(timeArray[position])
    return ("00" + date.getDate()).slice(-2) + "/" + ("00" + (date.getMonth() + 1)).slice(-2) + "/" + date.getFullYear() + " " +
        ("00" + date.getHours()).slice(-2) + ":" + ("00" + date.getMinutes()).slice(-2) + ":" + ("00" + date.getSeconds()).slice(-2)
}