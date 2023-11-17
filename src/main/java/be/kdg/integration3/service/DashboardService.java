package be.kdg.integration3.service;

import be.kdg.integration3.domain.CO2Data;
import be.kdg.integration3.domain.HumidityData;
import be.kdg.integration3.domain.RawDataRecord;
import be.kdg.integration3.domain.TemperatureData;

import java.util.List;

public interface DashboardService {
    List<TemperatureData> getTemperatureList(int id);

    List<HumidityData> getHumidityList(int id);

    List<CO2Data> getCO2List(int id);

    List<TemperatureData> getTemperatureList();

    List<HumidityData> getHumidityList();

    List<CO2Data> getCO2List();
}
