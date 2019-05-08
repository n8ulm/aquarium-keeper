package com.n8ulm.aquariumkeeper.data;

public class Aquarium {

    private String title;
    private int volume;
    private String unit;
    private String type;

    public Aquarium() {
    }

    public Aquarium(String title, int volume, String unit, String type) {
        this.title = title;
        this.volume = volume;
        this.unit = unit;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public int getVolume() {
        return volume;
    }

    public String getUnit() {
        return unit;
    }

    public String getType() {
        return type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setType(String type) {
        this.type = type;
    }
}
