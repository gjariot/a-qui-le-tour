package com.aquiletour.aquiletour.entity;

public class Activity {
    private long id;

    private String label;

    public long getId() {
        return id;
    }

    public Activity setId(long id) {
        this.id = id;

        return this;
    }

    public String getLabel() {
        return label;
    }

    public Activity setLabel(String label) {
        this.label = label;

        return this;
    }

    public String toString() {
        return label;
    }
}
