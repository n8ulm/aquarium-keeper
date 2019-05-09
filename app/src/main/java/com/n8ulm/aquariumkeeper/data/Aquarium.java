package com.n8ulm.aquariumkeeper.data;

public class Aquarium {

    private String id;
    private String title;
    private String volume;
    private String unit;
    private String type;

    public Aquarium() {
    }

    public Aquarium(String id, String title, String volume, String unit, String type) {
        this.id = id;
        this.title = title;
        this.volume = volume;
        this.unit = unit;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getVolume() {
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

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getID() {
        return id;
    }
}
