package com.example.demo;
public class Point {
    int x,y;
    int hCost,gCost;
    Point parent;
    public boolean  equals(Point p) {
        return x==p.x && y==p.y;
    }
    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }
    public int getFCost() {
        return gCost + hCost;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point point = (Point) obj;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return x * 31 + y;
    }
}

