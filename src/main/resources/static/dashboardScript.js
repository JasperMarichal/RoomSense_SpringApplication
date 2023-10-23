const xValuesTemp = tempList_timestamp;
const yValuesTemp = tempList;

new Chart("tempChart", {
    type: "line",
    data: {
        labels: tempList_timestamp,
        datasets: [{
            backgroundColor:"rgba(255,0,0,1)",
            borderColor: "rgba(255,0,0,1)",
            borderWidth: 4,
            fill: false,
            data: tempList,
            label: "Temperature"
        }]
    },
    options: {

    }
});

new Chart("humidChart", {
    type: "line",
    data: {
        labels: humidList_timestamp,
        datasets: [{
            backgroundColor:"rgba(0,128,255,1)",
            borderColor: "rgba(0,128,255,1)",
            borderWidth: 4,
            fill: false,
            data: humidList,
            label: "Humidity"
        }]
    },
    options: {

    }
});

console.log(tempList);

