package com.aquiletour.aquiletour.entity;

public class Participant {
    private long id;

    private String name;

    public long getId() {
        return id;
    }

    public Participant setId(long id) {
        this.id = id;

        return this;
    }

    public String getName() {
        return name;
    }

    public Participant setName(String name) {
        this.name = name;

        return this;
    }

    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        Participant participant = (Participant) obj;
        return this.id == participant.getId();
    }
}
