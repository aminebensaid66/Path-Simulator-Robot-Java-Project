package com.example.demo;
public abstract class Robot {
    protected double batteryLevel;
    protected Point position;
    protected String state;
    protected double conso;//consommation par case
    public Robot(double batteryLevel, Point position,double conso) {
        this.batteryLevel = batteryLevel;
        this.position = position;
        this.state = "Inactif";
        this.conso=conso;
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
    public abstract void moveTo(Point destination);




}


