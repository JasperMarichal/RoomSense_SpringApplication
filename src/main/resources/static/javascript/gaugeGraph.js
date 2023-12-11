let data;
let colors;

/**
 * Summary:
 * This function is responsible for getting everything setup for the gauge chart including getting the colours in place
 * to display the minimum, maximum and average values, it then calls a method to show the gauge chart.
 *
 * Detailed:
 * The function first identifies what the absolute min and max values are (the limits of what our sensors can detect)
 * as well as the minimum and maximum recommended values, these are set using a switch for textID.
 * The function then adds absolute min, absolute max, minimum recommended, maximum recommended alongside the
 * min/max/average values provided in the function call to an array, it then removed the duplicates in any of those
 * values.
 * Finally, it loops over that array and based on the values compared to the minRecommend and maxRecommend values
 * it adds the appropriate colors for the values to the colors array and the corresponding data to the data array.
 *
 * It then calls the method to show the chart.
 *
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
            absoluteMax = 1100/25;
            minRecommend = 1/25;
            maxRecommend = 1099/25;

            minValue = minValue/25;
            maxValue = maxValue/25;
            averageValue = averageValue/25;
            break;
        } case "spike": {
            absoluteMin = 0;
            absoluteMax = 1100/25;
            minRecommend = 1/25;
            maxRecommend = 1059/25;

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
            /**
             * If value is below minRecommend add data - 1 and a corresponding out of recommended color to the data and color arrays
             * Then push 1 to the data and call method to determine if the value was the min, average or max and add the appropriate color
             */
            data.push(value - absoluteMin - 1);
            data.push(1);
            colors.push(outOfRecommendedColor);
            valueColors(value, minValue, averageValue, maxValue);
            rawData.push(value);
        } else if (value === minRecommend || value === maxRecommend || value === absoluteMax){
            /**
             * If the value is equal to minRecommend or maxRecommend or absoluteMax, if it is, check if it is the first data entry
             * if it is not the first data entry subtract the rawData value of the previous entry,
             * if the uncalibrated value is also equal to the min or average or max act appropriately
             */
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
            /**
             * If the value is between minRecommend and maxRecommend it is within the recommended range and therefore
             * gets surrounded by white space instead of gray, as usual push the calibrated value - 1 then 1 to data and
             * then push white and the color depending on if it is min, max or average value
             */
            data.push(value-rawData[rawData.length - 1]-1);
            data.push(1);
            colors.push("white");
            valueColors(value, minValue, averageValue, maxValue);
            rawData.push(value);
        } else if (value > maxRecommend && value < absoluteMax){
            /**
             * If the value is larger than maxRecommend it is once again out of the recommended range and has the
             * appropriate colors pushed
             */
            data.push(value-maxRecommend-1);
            data.push(1);
            colors.push(outOfRecommendedColor);
            valueColors(value, minValue, averageValue, maxValue);
            rawData.push(value);
        } else {
            // For all other data it is erroneous and therefore push the appropriate value
            data.push(value);
            colors = ["black"];
        }
    })

    showGaugeGraph(textID, absoluteMin, absoluteMax, dataType);
}

/**
 * This function works out if the value is equal to min, average or max values and pushes the appropriate color to
 * the colors array.
 * @param value The value to compare to
 * @param minValue The minimum value
 * @param averageValue The average value
 * @param maxValue The maximum value
 */
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

/**
 * Show the gauge chart which is a modified version of a doughnut chart with the appropriate values
 * @param textID The identifier for the datatype
 * @param absoluteMin The absolute minimum value to display
 * @param absoluteMax The absolute maximum value to display
 * @param dataType The data type name to use in chart title
 */
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