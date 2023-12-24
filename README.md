# SpringProject
## Teamname = Int3-Team3 aka RoomSense

### Team members:

    - Abel Turlej
    - Roman Gordon
    - Anna Vorozhtsova
    - Boldi Olajos
    - Jasper Marichal

### Spring Profiles:
#### Database profiles:
- test - This profile will create a local database with the credentials supplied in application-test.properties, this will remove any existing data from that database and override it
- prod - This profile will use the database hosted on the KdG servers, using application-prod.properties

#### Application profiles:
- jdbcrepository - The main profile for the website
- jsonrepository **DEPRICATED**
    - The files: JsonDataRepository, SerialDataRepository and DashboardJSONService are all deprecated and no longer in use

### Installation and/or configuration instructions
    
1. Install device

    Mount the device to a wall or a place you want
    Plug the device into a power outlet
    Connect the device to your computer to connect to the wifi network
    Once the device is connected to the wifi network you will have wireless connectivity


2. Connect to website

    Make an account on the website to monitor your rooms
    Add a room to your account
    Connect to the device to the local WiFi network (for setup steps see “Configure Device WiFi Network”)
    Start collecting data from your device


3. What will be possible? 

    View the collected data of each room in a graph over time
    Get recommendations for each room
    Message to start ventilating the room
    Compare different rooms with each other


4. Configure Device WiFi Network

    - Start Configuration
    Connect to the device via the USB-B port
    Open a serial monitor software
    Enter the command “SENDING OFF” to pause the sending of the data
    Then enter either “CONFIG EDIT” or “CONFIG RESET” to enter configuration mode

    - Possible Commands
    Basic Configuration Commands
    Command “SSID <network name>”: Change the SSID that the WiFi connects to
    Command “PASS <wifi password>”: Change the password that the WiFi connects to
    Command “USER <username>”: The username and identity for a WPA2-Enterprise network (experimental feature)

    - Static IP Configuration Commands
    Addresses and Subnet Masks must be entered in octet form
    Command “IP <ip address>”: Set the static IP address to connect to
    Command “SN <subnet mask>”: Set the subnet mask of the local network
    Command “GW <gateway address>”: Set the gateway address of the local network
    Command “PORT <port number>”: Change the port to start the server at

    - Save Configuration
    Enter the command “CONFIG COMMIT” to save the configuration changes
    Or enter the command “CONFIG RESTORE” to discard the changes (only restore correctly if “CONFIG EDIT” was used)
    Then unplug the device from the computer and plug it back in to its own USB-A port
    Plug the device into the power outlet using the DC adapter

    - General Commands
    Command “CONFIG SHOW”: Show the active configuration
    Command “STATUS”: Show the wifi adapter status




### Dependencies: 
what libraries did you use and why

    Library name: jSerialComm
    We use this library to access our wifi router without external libraries and while remaining platform-independent.
    It allows to open a few ports at the same time.

    Library name: Chart.js
    We use this library for data visualisation in form of graphs. 


### Documentation of the interface with other systems (Arduino,...)

The protocols currently in use are telnet and serial.

To transmit the data we have made a simple format, where each datapoint is sent as a new line:

The first character indicates the type of data:
'T' for temperature
'H' for humidity
'C' for CO2
'S' for sound

The following characters must be digits. All other characters are ignored (unless its a newline), the number of digits 
may differ but should be at least 1 digit.
When a newline ('\n') is received, we try to parse the data and add it to a queue to be saved to the database.

Note that we do not use carriage returns ('\r') - they should be assumed to be there whenever a '\n' character is received.

Also, the data format and our processing of it does not depend on any particular protocol - it only expects to receive chunks of bytes that are assumed to contain ASCII text in this format. 

The Spring project has no direct connection to the Arduino - it communicates only with the database.
The ReadingSerial project saves the Arduino data to the database, from which Spring reads it. It needs to run
on the same network as the RoomSense device.
