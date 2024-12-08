package com.example.demo;

public class Obstacle extends MapObjects{
    public Obstacle(Point position){
        super(position);
    }
    @Override
    public Point getPosition() {
        return super.getPosition();
    }
    public void setPosition(Point position) {
        super.setPosition(position);
    }
    @Override
    public String getType() {
        return "obstacle";
    }
}
