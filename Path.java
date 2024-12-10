package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<Point> openList = new ArrayList<>();
    private List<Point> closedList = new ArrayList<>();
    private List<Point> finalPath = new ArrayList<>();
    private Map map;

    public Path(int[][] map) {
        this.map = new Map(map.length, map[0].length);
        this.map.m = map;
    }

    public List<Point> findPath(Point start, Point finish) {
        openList.add(start);

        while (!openList.isEmpty()) {
            Point current = getLowestFCostNode();
            openList.remove(current);
            closedList.add(current);

            if (current.equals(finish)) {
                reconstructPath(current);
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
                    }
                }
            }
        }

        return null;
    }

    private void reconstructPath(Point current) {
        while (current != null) {
            finalPath.add(0, current);
            current = current.parent;
        }
    }

    private Point getLowestFCostNode() {
        return openList.stream().min((p1, p2) -> {
            int comparison = Integer.compare(p1.getFCost(), p2.getFCost());
            return comparison != 0 ? comparison : Integer.compare(p1.hCost, p2.hCost);
        }).orElse(null);
    }

    private List<Point> getNeighbors(Point current) {
        List<Point> neighbors = new ArrayList<>();
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        for (int[] dir : directions) {
            int newX = current.x + dir[0];
            int newY = current.y + dir[1];
            if (newX >= 0 && newX < map.m.length && newY >= 0 && newY < map.m[0].length) {
                neighbors.add(new Point(newX, newY));
            }
        }

        return neighbors;
    }

    private boolean isObstacle(Point p) {
        return map.m[p.x][p.y] == 1;
    }

    private int getDistance(Point a, Point b) {
        int dx = Math.abs(a.x - b.x);
        int dy = Math.abs(a.y - b.y);
        int diagonalSteps = Math.min(dx, dy);
        int straightSteps = Math.abs(dx - dy);
        return diagonalSteps * 14 + straightSteps * 10;
    }
}