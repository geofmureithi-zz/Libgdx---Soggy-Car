package com.traffic.spilot.entities;

public class TrafficSelection {

    private int characterID;
    private float speed;

    public TrafficSelection() {
    }

    public void setCharacterID(int characterID) {
        this.characterID = characterID;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getCharacterID() {
        return characterID;
    }

    public float getSpeed() {
        return speed;
    }
}
