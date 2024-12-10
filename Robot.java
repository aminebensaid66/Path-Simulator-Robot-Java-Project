package com.example.demo;

public class Robot {
    public double batteryLevel;
    public Point position;
    public String state;
    public Robot() {

    }

    public Robot(double batteryLevel, Point position) {
        this.batteryLevel = batteryLevel;
        this.position = position;
        this.state = "Inactif";
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}