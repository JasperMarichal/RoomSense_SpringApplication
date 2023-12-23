package be.kdg.integration3.repository;

import be.kdg.integration3.SpringProjectApplication;
import be.kdg.integration3.domain.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
@Profile("jsonrepository")
public class JsonDataRepository implements DataRepository {
    private boolean development = SpringProjectApplication.DEVELOPMENT;

    private final Logger logger = LoggerFactory.getLogger(JsonDataRepository.class);

    List<File> temperatureFiles = new ArrayList<>();
    List<File> humidityFiles = new ArrayList<>();
    List<File> soundFiles = new ArrayList<>();
    List<File> CO2Files = new ArrayList<>();

    private final List<RawDataRecord> recordList;

    private JdbcTemplate jdbcTemplate;

    public JsonDataRepository(JdbcTemplate jdbcTemplate) {
        this.recordList = new ArrayList<>();
        this.jdbcTemplate = jdbcTemplate;
    }

    @Scheduled(fixedDelay = 3000000)
    public void read(int roomID, LocalDateTime startDateTime, LocalDateTime endDateTime, boolean readSpikes){
        findFiles("temperature");
        findFiles("humidity");
        findFiles("sound");
        findFiles("CO2");

        readAllFiles();
    }

    @Override
    public String getRoomType(int roomId) {
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
    public void addRoom(Room room, String email) {}

    @Override
    public void updateRoom(int roomID, String roomName, double width, double length, double height, String userEmail) {

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

    @Override
    public LocalDateTime getLastReadingTime(int roomID){
        return null;
    }

    @Override
    public List<SoundSpike> getSpikeRecordList() {
        return null;
    }

    @Override
    public double getAverageCo2(int roomId, Timestamp from, Timestamp to) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public double getAverageHumidity(int roomId, Timestamp from, Timestamp to) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public double getAverageTemp(int roomId, Timestamp from, Timestamp to) {
        throw new RuntimeException("Not implemented");
    }

    private void findFiles(String name) {
        File root = new File("..\\JSONSaves\\");
        FilenameFilter filesFilter = (directory, filename) -> filename.startsWith(name);

        if (root.listFiles(filesFilter) != null){
            switch (name){
                case "temperature" -> temperatureFiles = Arrays.stream(root.listFiles(filesFilter)).toList();
                case "humidity" -> humidityFiles = Arrays.stream(root.listFiles(filesFilter)).toList();
                case "sound" -> soundFiles = Arrays.stream(root.listFiles(filesFilter)).toList();
                case "CO2" -> CO2Files = Arrays.stream(root.listFiles(filesFilter)).toList();
            }
        }
    }

    private void readAllFiles() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        if (!temperatureFiles.isEmpty()){
            temperatureFiles.forEach(file -> {
                try (BufferedReader data = new BufferedReader(new FileReader(file))) {
                    TemperatureData[] tempData = gson.fromJson(data, TemperatureData[].class);
                    for (TemperatureData dataPoint : tempData){
                        recordList.add(new TemperatureData(dataPoint.getTimestamp(), dataPoint.getValue()));
                    }
                } catch (IOException e) {
                    logger.error("Something went wrong reading temperature!");
                }

                if (!development) {
                    if (file.delete()) {
                        logger.debug("File " + file.getName() + " has been successfully processed and deleted.");
                    } else {
                        logger.warn("File " + file.getName() + " could not be deleted.");
                    }
                }
            });
        }

        if (!humidityFiles.isEmpty()){
            humidityFiles.forEach(file -> {
                try (BufferedReader data = new BufferedReader(new FileReader(file))) {
                    HumidityData[] humidData = gson.fromJson(data, HumidityData[].class);
                    for (HumidityData dataPoint : humidData){
                        recordList.add(new HumidityData(dataPoint.getTimestamp(), dataPoint.getValue()));
//                        System.out.println(dataPoint);
                    }
                } catch (IOException e) {
                    logger.error("Something went wrong reading humidity!");
                }

                if (!development) {
                    if (file.delete()) {
                        logger.debug("File " + file.getName() + " has been successfully processed and deleted.");
                    } else {
                        logger.warn("File " + file.getName() + " could not be deleted.");
                    }
                }
            });
        }

        if (!soundFiles.isEmpty()){
            soundFiles.forEach(file -> {
                try (BufferedReader data = new BufferedReader(new FileReader(file))) {
                    SoundData[] soundData = gson.fromJson(data, SoundData[].class);
                    for (SoundData dataPoint : soundData){
                        recordList.add(new SoundData(dataPoint.getTimestamp(), dataPoint.getValue()));
                    }
                } catch (IOException e) {
                    logger.error("Something went wrong reading sound data!");
                }

                if (!development) {
                    if (file.delete()) {
                        logger.debug("File " + file.getName() + " has been successfully processed and deleted.");
                    } else {
                        logger.warn("File " + file.getName() + " could not be deleted.");
                    }
                }
            });
        }

        if (!CO2Files.isEmpty()){
            CO2Files.forEach(file -> {
                try (BufferedReader data = new BufferedReader(new FileReader(file))) {
                    CO2Data[] CO2Data = gson.fromJson(data, CO2Data[].class);
                    for (CO2Data dataPoint : CO2Data){
                        recordList.add(new CO2Data(dataPoint.getTimestamp(), dataPoint.getValue()));
//                        System.out.println(dataPoint);
                    }
                } catch (IOException e) {
                    logger.error("Something went wrong reading CO2!");
                }

                if (!development) {
                    if (file.delete()) {
                        logger.debug("File " + file.getName() + " has been successfully processed and deleted.");
                    } else {
                        logger.warn("File " + file.getName() + " could not be deleted.");
                    }
                }
            });
        }
    }
}
