**Teamname** = Int3-Team3 aka RoomSense

**Team members**:

    - Abel Turlej
    - Roman Gordon
    - Anna Vorozhtsova
    - Boldi Olajos
    - Jasper Marichal

**Installation and/or configuration instructions**
    
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




**Dependencies**: what libraries did you use and why

    Library name: jSerialComm
    We use this library to access our wifi router without external libraries and while remaining platform-independent.
    It allows to open a few ports at the same time.

    Library name: Chart.js
    We use this library for data visualisation in form of graphs. 


Documentation of the interface with other systems (Arduino,...)
