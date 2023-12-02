package be.kdg.integration3.domain;

import java.sql.Timestamp;

public class SoundSpike {
    private int spikeId;
    private int roomId;
    private Timestamp startTime;
    private Timestamp endTime;
    private int peakAmplitude;
    private int numberMeasurements;

    public SoundSpike(int spikeId, int roomId, Timestamp startTime, Timestamp endTime) {
        this.spikeId = spikeId;
        this.roomId = roomId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getSpikeId() {
        return spikeId;
    }

    public void setSpikeId(int spikeId) {
        this.spikeId = spikeId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public int getPeakAmplitude() {
        return peakAmplitude;
    }

    public void setPeakAmplitude(int peakAmplitude) {
        this.peakAmplitude = peakAmplitude;
    }

    public int getNumberMeasurements() {
        return numberMeasurements;
    }

    public void setNumberMeasurements(int numberMeasurements) {
        this.numberMeasurements = numberMeasurements;
    }

    public double getDuration() {
        long endTimeMillis = endTime.toInstant().toEpochMilli();
        long startTimeMillis = startTime.toInstant().toEpochMilli();
        return (endTimeMillis - startTimeMillis);
    }

    @Override
    public String toString() {
        return "SoundSpike{" +
                "spikeId=" + spikeId +
                ", roomId=" + roomId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", peakAmplitude=" + peakAmplitude +
                ", numberMeasurements=" + numberMeasurements +
                '}';
    }
}
