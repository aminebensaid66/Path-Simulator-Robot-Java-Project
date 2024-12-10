package com.example.demo;
import java.util.ArrayList;
import java.util.List;

public class Pathfinding {
    public List<Point> finalp = new ArrayList<>();
    public List<ChargingPoint> tableofstations = new ArrayList<>();
    private List<Point> tabstation = new ArrayList<>();
    private List<Point> duplicatetab= new ArrayList<>();
    public Robot robot = new Robot();
    public Point start;
    public Point robotstart;
    public Point finish;
    public Map map1;

    public Pathfinding(Point start , Point finish ,int[][] map,Robot robot) {//constructor
        this.map1 = new Map(map.length,map[0].length);
        this.start = start;
        this.robotstart =start;//el function finalpathfinder tbadel el start benesba lihaa tal9a charging station valable (3malet variable jdida bech na3mel save lel position bte3 el start)
        this.finish = finish;
        this.map1.m = map;
        this.robot = robot;
    }


    public void printMap(List<Point> tabstation) {
        char[][] visualMap = new char[map1.m.length][map1.m[0].length];//print el map format matrice bte3 char
        for (int i = 0; i < map1.m.length; i++) {
            for (int j = 0; j < map1.m[0].length; j++) {
                if (map1.m[i][j] == 0) {
                    visualMap[i][j] = '0';
                } else {
                    visualMap[i][j] = '1';
                }
            }
        }
//print el path eli mche fih el robot
        for (Point p : finalp) {
            System.out.println("(" + p.x + ", " + p.y + " )");
            if (!p.equals(start) && !p.equals(finish)) {
                visualMap[p.x][p.y] = 'R';
            }
        }
        //affichage el charging stations
        for (Point p : this.duplicatetab) {
            visualMap[p.x][p.y] = 'C';
        }
        visualMap[robotstart.x][robotstart.y] = 'S';//affichage finish
        visualMap[finish.x][finish.y] = 'F';//affichage start
        for (int i = 0; i < map1.m.length; i++) {
            for (int j = 0; j < map1.m[0].length; j++) {
                System.out.print(visualMap[i][j]+" ");
            }
            System.out.println();
        }
    }

    int pathcost(Point start, Point finish) {
        Path path = new Path(this.map1.m);//new path instance bech neb3dou 3al inteference btw other paths
        List<Point> pathResult = path.findPath(start, finish);
        if (pathResult == null) {//case mafamech path
            System.out.println("No path found from (" + start.x + ", " + start.y + ") to (" + finish.x + ", " + finish.y + ")");//print no path found
            return Integer.MAX_VALUE; //raja3 valeur max (el function lezem traja3 int
        }
        int cost = calculatecost(pathResult);
        System.out.println("Path cost from (" + start.x + ", " + start.y + ") to (" + finish.x + ", " + finish.y + ") is " + cost);//cost bte3 el path (for debuging)
        return cost;
    }

    private int calculatecost(List<Point> path) {
        int cost = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Point current = path.get(i);
            Point next = path.get(i + 1);
            cost += (current.x == next.x || current.y == next.y) ? 10 : 14;
        }//function bech ne7seb biha el bo3ed bte3 kol station 3ala el finish in case of a possible path (bech te5tar a7sen station)
        return cost;
    }
    Point reachablestation(Point current, double charge) {
        List<Point> rechablestations = new ArrayList<>();
        for(Point p : tabstation) {
            if(pathcost(current,p)<charge)
                rechablestations.add(p);
        }
        for (int i = 0; i < rechablestations.size()-1; i++) {//sort el stations based 3al charging cost bech toslelhoum w 3ala el distance bte3houm lel finish
            for (int j = 0; j <rechablestations.size()-1 - i; j++) {
                if (pathcost(rechablestations.get(j),this.finish) >pathcost(rechablestations.get(j+1),this.finish)) {
                    Point temp = rechablestations.get(j);
                    rechablestations.set(j,rechablestations.get(j+1));
                    rechablestations.set(j+1, temp);
                }}}
        if(rechablestations.size()==0) {
            System.out.println("No reachable station (" + current.x + ", " + current.y + ") with charge: " + charge);
            return null;
        }else {
            return rechablestations.get(0);
        }

    }

    void finalpathfinder() {
        for(ChargingPoint p : tableofstations) {
            Point point = new Point(p.getPosition().y,p.getPosition().x);
            tabstation.add(point);
        }
        this.duplicatetab.addAll(tabstation);//kol station na3mlelha visit na7iha fel tabstation bech to avoid infinte loop walit 3malet tabstation jdid menou houwa bech nsawrou el map fel le5er
        while (true) {//badelet recursivite b while true (w 3malet des conditions d'arret fel les cas el robot ma3andouch charge(case1) w mafamech station w ela mafamech path possible(case 2)
            System.out.println("Current (" + start.x + ", " + start.y + "), Charge: " + robot.batteryLevel);//printing charge and current position (for debugging)
            Path path = new Path(this.map1.m);//3malet class jdida bech matsirech interference bin path 9dim w jdid (el fcost w gcost w hcost bte3 nodes ya3mlou reset kol mara)
            List<Point> pathToFinish = path.findPath(start, finish);//el object jdid ymechi ylawej 3ala path lel finish
            if (pathToFinish != null && calculatecost(pathToFinish) <= robot.batteryLevel) {//path found to finish wa9et el charge akber mel charge required bech ya5let lel finish
                System.out.println("Path to finish is reachable. Completing path...");
                finalp.addAll(pathToFinish);
                return;
            }
            Point nextStation = reachablestation(start, robot.batteryLevel);//charge matekfich nemchou nlawjou 3ala station (el function findstation tal9alek a7sen choice bte3 station based 3ala bo3dhoum 3lik w 3ala bo3dhoum 3ala el finish(kima el A*) w 3ala charge zeda
            if (nextStation == null) {//mafamech station possible robot ye9ef win houwa
                System.out.println("No reachable stations. Stopping at: (" + start.x + ", " + start.y + ")");
                return;//exit el code kamel mafamech solution befch na5letou lel finish
            }
            Path path1 = new Path(this.map1.m);//3malet object jdid bech matsirech interference bin el search bte3 el path eli fet weli nech ysir tawa
            List<Point> pathToStation = path1.findPath(start, nextStation);
            System.out.println("Start (" + start.x + ", " + start.y + ")");//start hiya el current station eli fiha el robot(7sebet el start  station)
            System.out.println("finish (" + nextStation.x + ", " + nextStation.y + ")");//el finish bima enou el charge matwaselech el finish bech tkoun a7sen station najmou nousloulha
            if (pathToStation == null || calculatecost(pathToStation) > robot.batteryLevel) {//case eli el robot mal9ach station ynajem yemchilah bech ychargi
                System.out.println("Station unreachable. Stopping at: (" + start.x + ", " + start.y + ")");

                return;//exit mel function
            }
            System.out.println("Recharging at station: (" + nextStation.x + ", " + nextStation.y + ")");//best case scenario station reachable (charge ykafi bech yousel el robot)
            finalp.addAll(pathToStation);//nzidou el path jdid lel somme bte3 el paths el 9dom

            robot.batteryLevel = 100;//robot fel charging station --> charge max
            start = nextStation;//el station eli houwa fiha walet el start bte3 el recherche jdid bech yal9a path lel finish (basically principe el recursivity ama iterative)
            tabstation.remove(nextStation);//na7iw station eli visited men 9bal bech maysirech boucle infinie

        }

    }


    public static void main(String[] args) {
        int[][] map = {
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1},
                {1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1},
                {1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };

        Point start = new Point(10, 10);//debut
        Point finish = new Point(19, 2);//fin
        Robot robot  = new Robot(1000,start);
        int robotcharge = 90;//el charge eli yabda biha el robot
        robot.batteryLevel=1000;;
        Pathfinding pathfinding = new Pathfinding(finish,start,map,robot);//instance bte3 el object pathfinding
        pathfinding.finalpathfinder();
        pathfinding.printMap(pathfinding.tabstation);
        for (Point p : pathfinding.finalp) {
            System.out.println("(" + p.x + ", " + p.y + " )");
        }
    }
}