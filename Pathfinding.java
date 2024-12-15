package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Pathfinding {
    public List<Point> finalPath = new ArrayList<>();
    private List<ChargingPoint> stationPoints = new ArrayList<>();
    public Robot robot;
    public Point start;
    public Point finish;
    public Map map;

    public Pathfinding(Point start, Point finish, int[][] map, Robot robot, List<ChargingPoint> stations) {
        this.map = new Map(map.length, map[0].length);
        this.start = start;
        this.finish = finish;
        this.map.m = map;
        this.robot = robot;
        this.stationPoints.addAll(stations); // Initialize the charging stations list
    }

    public void printMap() {
        char[][] visualMap = new char[map.m.length][map.m[0].length];
        for (int i = 0; i < map.m.length; i++) {
            for (int j = 0; j < map.m[0].length; j++) {
                visualMap[i][j] = map.m[i][j] == 0 ? '0' : '1';
            }
        }

        for (Point p : finalPath) {
            if (!p.equals(start) && !p.equals(finish)) {
                visualMap[p.x][p.y] = 'R';
            }
        }

        for (ChargingPoint cp : stationPoints) {
            visualMap[cp.getPosition().x][cp.getPosition().y] = 'C';
        }

        visualMap[start.x][start.y] = 'S';
        visualMap[finish.x][finish.y] = 'F';

        for (char[] row : visualMap) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    private int calculatePathCost(List<Point> path) {
        int cost = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Point current = path.get(i);
            Point next = path.get(i + 1);
            cost += (current.x == next.x || current.y == next.y) ? 10 : 14;//cost per case
        }
        return cost;
    }

    private int getPathCost(Point start, Point finish) {
        Path path = new Path(map.m);
        List<Point> pathResult = path.findPath(start, finish);
        if (pathResult == null) {
            return Integer.MAX_VALUE;
        }
        return calculatePathCost(pathResult);
    }

    private ChargingPoint getReachableStation(Point current, double charge) {
        List<ChargingPoint> reachableStations = new ArrayList<>();
        for (ChargingPoint station : stationPoints) {
            if (getPathCost(current, station.getPosition()) < charge) {
                reachableStations.add(station);
            }
        }
        reachableStations.sort((cp1, cp2) -> Integer.compare(getPathCost(cp1.getPosition(), finish), getPathCost(cp2.getPosition(), finish)));

        if (reachableStations.isEmpty()) {
            return null;
        }
        return reachableStations.get(0);
    }

    public void findFinalPath() {
        while (true) {
            Path path = new Path(map.m);
            List<Point> pathToFinish = path.findPath(start, finish);

            if (pathToFinish != null && calculatePathCost(pathToFinish) <= robot.batteryLevel) {
                finalPath.addAll(pathToFinish);
                return;
            }

            ChargingPoint nextStation = getReachableStation(start, robot.batteryLevel);
            if (nextStation == null) {
                return;
            }

            Path pathToStation = new Path(map.m);
            List<Point> pathToStationResult = pathToStation.findPath(start, nextStation.getPosition());

            if (pathToStationResult == null || calculatePathCost(pathToStationResult) > robot.batteryLevel) {
                return;
            }

            finalPath.addAll(pathToStationResult);
            nextStation.charge(robot); // Call the `charge` method of ChargingPoint.
            start = nextStation.getPosition();
            stationPoints.remove(nextStation); // Remove the station after use.
        }
    }

    public static void main(String[] args) {
        int[][] map = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        Point start = new Point(1, 1);
        Point finish = new Point(2, 13);
        Robot robot = new Robot(50, start);

        // Define charging stations
        List<ChargingPoint> stations = new ArrayList<>();
        stations.add(new ChargingPoint(new Point(1, 8)));
        stations.add(new ChargingPoint(new Point(2, 2)));

        Pathfinding pathfinding = new Pathfinding(start, finish, map, robot, stations);
        pathfinding.findFinalPath();

    }
}