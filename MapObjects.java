package com.example.demo;

public abstract class MapObjects {
    private Point position;
    public MapObjects(Point pos) {
        position=pos;
    }
    public Point getPosition() {
        return position;
    }
    public void  setPosition(Point pos) {
        position=pos;
    }
    public abstract String getType();
}

