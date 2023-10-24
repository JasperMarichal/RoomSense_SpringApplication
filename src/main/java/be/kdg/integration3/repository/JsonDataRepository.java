package be.kdg.integration3.repository;

import be.kdg.integration3.domain.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class JsonDataRepository implements DataRepository {
    private final Logger logger = LoggerFactory.getLogger(JsonDataRepository.class);

    List<File> temperatureFiles = new ArrayList<>();
    List<File> humidityFiles = new ArrayList<>();
    List<File> soundFiles = new ArrayList<>();
    List<File> CO2Files = new ArrayList<>();

    private final List<RawDataRecord> recordList;

    public JsonDataRepository() {
        this.recordList = new ArrayList<>();
    }

    @Scheduled(fixedDelay = 30000)
    public void read(){
        findFiles("temperature");
        findFiles("humidity");
        findFiles("sound");
        findFiles("CO2");

        readAllFiles();

        System.out.println(getRecordList());
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
            });
        }

        if (!humidityFiles.isEmpty()){
            humidityFiles.forEach(file -> {
                try (BufferedReader data = new BufferedReader(new FileReader(file))) {
                    HumidityData[] humidData = gson.fromJson(data, HumidityData[].class);
                    for (HumidityData dataPoint : humidData){
                        recordList.add(new HumidityData(dataPoint.getTimestamp(), dataPoint.getValue()));
                    }
                } catch (IOException e) {
                    logger.error("Something went wrong reading humidity!");
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
            });
        }

        if (!CO2Files.isEmpty()){
            CO2Files.forEach(file -> {
                try (BufferedReader data = new BufferedReader(new FileReader(file))) {
                    CO2Data[] CO2Data = gson.fromJson(data, CO2Data[].class);
                    for (CO2Data dataPoint : CO2Data){
                        recordList.add(new CO2Data(dataPoint.getTimestamp(), dataPoint.getValue()));
                    }
                } catch (IOException e) {
                    logger.error("Something went wrong reading CO2!");
                }
            });
        }
    }

    public List<RawDataRecord> getRecordList() {
        return recordList;
    }
}
