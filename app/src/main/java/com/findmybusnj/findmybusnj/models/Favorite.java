package com.findmybusnj.findmybusnj.models;

/**
 * Created by davidaghassi on 10/28/16.
 */

public class Favorite {
    // private variables
    private String stop = "";
    private String route = "";

    public Favorite(String stop, String route, int frequency) {
        this.stop = stop;
        this.route = route;
        this.frequency = frequency;
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

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    private int frequency = 0;


}
