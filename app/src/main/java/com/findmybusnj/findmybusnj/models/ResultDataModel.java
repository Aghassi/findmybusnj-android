package com.findmybusnj.findmybusnj.models;

/**
 * Created by davidaghassi on 10/19/16.
 */

public class ResultDataModel {
    private String busNumber = "";
    private String route = "";
    private int time = 0;
    private boolean noPrediction = false;
    private boolean delayed = false;
    private boolean arriving = false;

    public boolean isDelayed() {
        return delayed;
    }

    public void setDelayed(boolean delayed) {
        this.delayed = delayed;
    }

    public boolean isArriving() {
        return arriving;
    }

    public void setArriving(boolean arriving) {
        this.arriving = arriving;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        if (time == 0) {
            setArriving(true);
        } else {
            this.time = time;
        }
    }

    public boolean isNoPrediction() {
        return noPrediction;
    }

    public void setNoPrediction(boolean noPrediction) {
        this.noPrediction = noPrediction;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }
}
