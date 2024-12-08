package com.example.demo;

public class ChargingPoint extends MapObjects {
    public ChargingPoint(Point position){
        super(position);
    }

    public void charge(Robot rob) {
        System.out.println("Charging robot at " + super.getPosition() + "...");
        rob.setBatteryLevel(100);
    }
    @Override
    public String getType() {
        return "chargingPoint";
    }



}