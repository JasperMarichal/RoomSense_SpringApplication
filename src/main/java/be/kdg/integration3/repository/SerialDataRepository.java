package be.kdg.integration3.repository;

import be.kdg.integration3.domain.*;
import com.fazecast.jSerialComm.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@Repository
public class SerialDataRepository implements DataRepository {
    private final Logger logger = LoggerFactory.getLogger(SerialDataRepository.class);
    private SerialPort port;
    private boolean readingDataValue;
    private int currentValue;
    private char currentDataType;

    private final List<RawDataRecord> recordList;

    public SerialDataRepository() {
        this.recordList = new ArrayList<>();
        initSerial();
        this.currentDataType = ' ';
    }

    private void initSerial() {
        SerialPort[]  commPorts = SerialPort.getCommPorts();
        logger.debug("List COM ports");
        for(int i = 0; i < commPorts.length; i++) {
            logger.debug("comPorts[" + i + "] = " + commPorts[i].getDescriptivePortName());
        }
        SerialPort port = commPorts[0];     // array index to select COM port
        port.setBaudRate(115200);
        port.openPort();
        this.port = port;
    }

    @Override
    public void read(int roomID, LocalDateTime startDateTime, LocalDateTime endDateTime, boolean readSpikes) {
        try {
            while (port.bytesAvailable() > 0) {
                byte[] readBuffer = new byte[port.bytesAvailable()];
                int numRead = port.readBytes(readBuffer, readBuffer.length);
                char[] readChars = new char[numRead];
                for(int i = 0; i < readChars.length; i++) {
                    readChars[i] = (char)readBuffer[i];
                }
                parseSerial(readChars);
            }
        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()));
        }
//        return 0;
    }

    @Override
    public RoomType getRoomType(int roomId) {
        return null;
    }

    @Override
    public List<SoundData> getSpikeData(int roomId, int spikeId) {
        return null;
    }

    @Override
    public List<Room> getUserRooms(String userAccount) {
        return null;
    }

    @Override
    public LocalDateTime getLastReadingTime(int roomID){
        return null;
    }

    @Override
    public void addRoom(Room room, String email) {}

    @Override
    public void updateRoom(int roomID, String roomName, double width, double length, double height, String userEmail) {

    }

    @Override
    public List<SoundSpike> getSpikeRecordList() {
        return null;
    }

    private int parseSerial(char[] newSerialData) {
        int newDataCount = 0;
        for(char c : newSerialData) {
            if(c == 'T' || c == 'H' || c == 'C' || c == 'S') {
                currentDataType = c;
                readingDataValue = true;
                currentValue = 0;
            }
            if(readingDataValue && c >= '0' && c <= '9'){
                currentValue *= 10;
                currentValue += (c - '0');
            }
            if(readingDataValue && c == '\n') {
                readingDataValue = false;
                Timestamp recordTimestamp = Timestamp.from(Instant.now());
                switch (currentDataType) {
                    case 'T':
                        recordList.add(new TemperatureData(recordTimestamp, currentValue));
                        newDataCount++;
                        break;
                    case 'H':
                        recordList.add(new HumidityData(recordTimestamp, currentValue));
                        newDataCount++;
                        break;
                    case 'C':
                        recordList.add(new CO2Data(recordTimestamp, currentValue));
                        newDataCount++;
                        break;
                    case 'S':
                        recordList.add(new SoundData(recordTimestamp, currentValue));
                        newDataCount++;
                        break;
                }
//                logger.debug("Added new record: {} {}", currentDataType, currentValue);
                currentDataType = ' ';
            }
        }
        return newDataCount;
    }

    @Override
    public List<TemperatureData> getTemperatureRecordList() {
        return recordList.stream().filter(record -> record instanceof TemperatureData).map(rawDataRecord -> (TemperatureData) rawDataRecord).toList();
    }

    @Override
    public List<HumidityData> getHumidityRecordList() {
        return recordList.stream().filter(record -> record instanceof HumidityData).map(rawDataRecord -> (HumidityData) rawDataRecord).toList();
    }

    @Override
    public List<CO2Data> getCO2RecordList() {
        return recordList.stream().filter(record -> record instanceof CO2Data).map(rawDataRecord -> (CO2Data) rawDataRecord).toList();
    }

    @Override
    public List<SoundData> getNoiseRecordList() {
        return null;
    }
}
