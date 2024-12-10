package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<Point> openList = new ArrayList<>();
    private List<Point> closedList = new ArrayList<>();
    public List<Point> finalPath = new ArrayList<>();
    public List<Point> tabstation= new ArrayList<>();
    public Map map1;

    public Path(int[][] map) {
        this.map1=new Map(map.length,map[0].length);
        this.map1.m = map;
    }
    public List<Point> findPath(Point start, Point finish) {
        openList.add(start);
        while (!openList.isEmpty()) {
            Point current = getLowestFCostNode(openList);
            openList.remove(current);
            closedList.add(current);
            if (current.equals(finish)) {
                reconstructPath(current);
                //openList.clear();
                //closedList.clear();
                return finalPath;
            }
            for (Point neighbor : getNeighbors(current)) {
                if (isObstacle(neighbor) || closedList.contains(neighbor)) continue;

                int tentativeGCost = current.gCost + getDistance(current, neighbor);

                if (!openList.contains(neighbor) || tentativeGCost < neighbor.gCost) {
                    neighbor.gCost = tentativeGCost;
                    neighbor.hCost = getDistance(neighbor, finish);
                    neighbor.parent = current;
                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }}}
        }
        //openList.clear();
        //closedList.clear();
        return null; //NO PATH POSSIBLE
    }
    private void reconstructPath(Point current) {
        while (current != null) {
            finalPath.add(current);
            current = current.parent;
        }
        //List<Point> reverse=new ArrayList<>();
        //for(int i = finalPath.size()-1;i>0;i--) {
        //reverse.add(finalPath.get(i));
        //}
        //finalPath.clear();
        //finalPath.addAll(reverse);
    }

    private Point getLowestFCostNode(List<Point> list) {
        Point lowest = list.get(0);
        for (Point p : list) {
            if (p.getFCost() < lowest.getFCost() || (p.getFCost() == lowest.getFCost() && p.hCost < lowest.hCost)) {
                lowest = p;
            }
        }
        return lowest;
    }
    private List<Point> getNeighbors(Point current) {
        List<Point> neighbors = new ArrayList<>();
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},//direction direct
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // direction diagonal
        };
        for (int[] dir : directions) {
            int newX = current.x + dir[0];
            int newY = current.y + dir[1];
            if (newX >= 0 && newX < map1.m.length && newY >= 0 && newY < map1.m[0].length) {
                neighbors.add(new Point(newY,newX));
            }
        }

        return neighbors;
    }

    private boolean isObstacle(Point p) {
        return map1.m[p.x][p.y] == 1;
    }

    private int getDistance(Point a, Point b) {
        int dx = Math.abs(a.x - b.x);
        int dy = Math.abs(a.y - b.y);
        int diagonalSteps = Math.min(dx, dy);
        int straightSteps = Math.abs(dx - dy);
        return diagonalSteps * 14 + straightSteps * 10;
    }
}