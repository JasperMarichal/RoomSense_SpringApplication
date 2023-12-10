init();

function init() {
    for (let room = 0; room < roomOverview.length; room++){
        let avgText = document.getElementById("roomAvg" + roomOverview[room][0][0]);
        let recText = document.getElementById("roomRec" + roomOverview[room][0][0]);
        let warnText = document.getElementById("roomWarn" + roomOverview[room][0][0]);

        for (let type = 1; type < 5; type++){
            let variableName;
            let warnMax;
            let warnMin;
            let recMax;
            switch (type){
                case 1: {
                    variableName = 'temperature';
                    warnMax = 30; warnMin = 20; recMax = 24;
                    break;
                }
                case 2: {
                    variableName = 'humidity';
                    warnMax = 70; warnMin = 30; recMax = 60;
                    break;
                }
                case 3: {
                    variableName = 'CO2';
                    warnMin = 5000; recMax = 1500;
                    break;
                }
                case 4: {
                    variableName = 'noise';
                    break;
                }
            }

            if (roomOverview[room][type].length > 0) {
                let total = 0;
                for (let dataPoint = 0; dataPoint < roomOverview[room][type].length; dataPoint++) total += roomOverview[room][type][dataPoint];
                let avg = total / roomOverview[room][type].length;
                if (avgText.innerHTML !== "") avgText.innerHTML += "<br>";
                avgText.innerHTML += "Average " + variableName + ": " + parseFloat(avg).toFixed(2);

                if (variableName !== 'noise' && avg > warnMax){
                    if (warnText.innerHTML !== "") warnText.innerHTML += "<br>";
                    warnText.innerHTML += "Average " + variableName + " is too high"
                    if (variableName === 'temperature') warnText.innerHTML += " consider cooling the room!"
                    else if (variableName === 'humidity') warnText.innerHTML += " consider using a dehumidifier!"
                    else if (variableName === 'CO2') warnText.innerHTML += ", ventilate the room, do not maintain this concentration for over 8 hours, there is a risk of serious health issues!"
                }
                if (variableName !== 'CO2' && variableName !== 'noise' && avg < warnMin){
                    if (warnText.innerHTML !== "") warnText.innerHTML += "<br>";
                    warnText.innerHTML += "Average " + variableName + " is too low"
                    if (variableName === 'temperature') warnText.innerHTML += " consider heating the room!"
                    else if (variableName === 'humidity') warnText.innerHTML += " consider using a humidifier!"
                }
                if (variableName !== 'noise' && avg > recMax){
                    if (recText.innerHTML !== "") recText.innerHTML += "<br>";
                    recText.innerHTML += "Average " + variableName + " is getting too high"
                    if (variableName === 'temperature') recText.innerHTML += " consider maintaining between 20°C and 24°C for an optimal work environment."
                    else if (variableName === 'humidity') recText.innerHTML += " consider maintaining between 40% and 60% humidity for an optimal work environment."
                    else if (variableName === 'CO2') recText.innerHTML += " consider ventilating the room, maintain a concentration of less than 1500ppm."
                }

            } else {
                if (avgText.innerHTML !== "") avgText.innerHTML += "<br>";
                avgText.innerHTML += "No " + variableName + " data is available."
            }
        }

        if (recText.innerHTML === "" && warnText.innerHTML === "") recText.innerHTML = "There are no active recommendations or warnings";
    }
}