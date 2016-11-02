package com.findmybusnj.findmybusnj.models;

/**
 * Created by davidaghassi on 10/28/16.
 */

public class Favorite {
    // private variables
    private String stop = "";
    private String route = "";
    private int frequency = 0;

    // Constructors
    public Favorite() {
        this.stop = "";
        this.route = "";
        this.frequency = 0;
    }

    public Favorite(String stop, String route, int frequency) {
        this.stop = stop;
        this.route = route;
        this.frequency = frequency;
    }

    // Getter and Setters
    public String generatePrimaryKey() {
        return this.stop + this.route;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) { this.frequency = frequency; }

    public void incrementFrequency() {
        this.frequency++;
    }
}
