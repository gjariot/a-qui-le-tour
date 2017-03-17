package com.aquiletour.aquiletour.entity;

import java.io.Serializable;
import java.util.List;

public class Activity implements Serializable {
    private long id;

    private String label;

    private List<Participant> participants;

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

    public List<Participant> getParticipants() {
        return participants;
    }

    public Activity setParticipants(List<Participant> participants) {
        this.participants = participants;
        return this;
    }

    public String toString() {
        return label;
    }
}
