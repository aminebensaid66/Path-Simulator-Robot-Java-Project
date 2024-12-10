package com.example.demo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.List;

public class Controller {
    int w=0;
    @FXML
    private TextField rowsField;

    @FXML
    private TextField colsField;
    @FXML
    private GridPane gridPane;
    private Map map;

    @FXML
    private void handleAddObstacle() {
        if (map == null) {
            showError("Please set the map dimensions first.");
            return;
        }

        w=1;
        System.out.println("You are now adding obstacles.");

    }

    @FXML
    private void handleAddChargingPoint() {
        if (map == null) {
            showError("Please set the map dimensions first.");
            return;
        }

       w=2;
        System.out.println("You are now adding CharginPoints.");
    }
Robot robot;

    @FXML
    private void initialize() {
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void handleCellClick(int x, int y) {
        if (map == null) {
            System.out.println("Map is not initialized.");
        }
        if (w==0) {
            System.out.println("Please select an action first (Obstacle or Charging Point).");
        }
        else if (w==1) {
            addObstacle(x, y);
        } else if (w==2) {
            addChargingPoint(x, y);
        }
        else if (w==3) {
            Point newPosition = new Point(y, x);
                if (robot == null){
                    robot=new Robot(100, newPosition);
                    System.out.println("Robot position set to: (" + x + ", " + y + ")");
                    updateCellButton(x,y); // Refresh the grid to show the robot in the new position
                    w=0; // Disable the mode after setting
                }
                else {
                    System.out.println("deja il y a un robot");
                }
            }

        else if(w==4){
            if(ok==0) {
                Point newPosition = new Point(y, x);
                map.addDestionationPoint(newPosition);
                System.out.println("Destination Point set to: (" + x + ", " + y + ")");
                updateCellButton(x, y);
                ok = 1;
                w=0 ;
            }
            else{
                System.out.println("deja il y a une point de destination");

            }

        }


    }

    private void addObstacle(int x, int y) {
        Obstacle obstacle = new Obstacle(new Point(y, x));
        map.addObstacle(obstacle);
        System.out.println("Obstacle added at (" + x + ", " + y + ")");
        updateCellButton(x, y);
    }
    private void addChargingPoint(int x, int y) {
        ChargingPoint chargingPoint = new ChargingPoint(new Point(y, x));
        map.addCharginPoint(new Point(y, x));
        System.out.println("Charging point added at (" + x + ", " + y + ")");
        updateCellButton(x, y);
    }
    private void updateCellButton(int x, int y) {
        Button button = getButtonFromGrid(x, y);
        if (button != null) {
            if (w==1) {
                button.setStyle("-fx-background-color: #000000;");
            } else if (w==2) {
                button.setStyle("-fx-background-color: #7de77d;");
            }
            else if(w==3){
                button.setStyle("-fx-background-color: #48a1d9;");
            }
            else if(w==4){
                button.setStyle("-fx-background-color: #b548d9;");
            }

        }
    }
    private Button getButtonFromGrid(int x, int y) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == x && GridPane.getColumnIndex(node) == y) {
                return (Button) node;
            }
        }
        return null;
    }
    @FXML
    private void createGrid(int rows, int cols) {
        w=0;
        ok=0;
        robot=null;
        gridPane.getChildren().clear();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                Button cellButton = new Button();
                cellButton.setMinSize(15, 15);
                cellButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black; -fx-font-size: 12; -fx-font-weight: bold;");


                cellButton.setShape(new javafx.scene.shape.Rectangle(20, 20)); // Rounded corners with radius 20
                cellButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black; -fx-font-size: 12; -fx-font-weight: bold; -fx-background-radius: 15;");


                int finalI = i;
                int finalJ = j;
                cellButton.setOnAction(event -> handleCellClick(finalI, finalJ));
                w=1;
                gridPane.add(cellButton, j, i);
                if(i==0 || j==0|| i==rows-1||j==cols-1 ){
                    addObstacle(i,j);
                    updateCellButton(i,j);
                }
                w=0;
            }

    }}
    @FXML
    private void handleAddRobot() {
        w=3;
        System.out.println("Click on a cell to set the robot's position.");
    }
    @FXML
    private void handleSetMapDimensions() {
        try {
            int rows = Integer.parseInt(rowsField.getText());
            int cols = Integer.parseInt(colsField.getText());
            map = new Map(rows, cols);
            System.out.println("Map created with dimensions: " + rows + "x" + cols);
            createGrid(rows, cols);
        } catch (NumberFormatException e) {
            showError("Please enter valid integers for rows and columns.");
        }
    }
    int ok=0;
    private void updateCellStyle(int x, int y, String color) {
        Button button = getButtonFromGrid(x, y);
        if (button != null) {
            button.setStyle("-fx-background-color: " + color + ";");
        }
    }
    public void simulatePath(List<Point> path) {
        if (path == null || path.isEmpty()) {
            System.out.println("Path is empty or null.");
            return;
        }
        System.out.println("Starting simulation at: " + path.get(0));
        Timeline timeline = new Timeline();
        final int[] step = {0};

        KeyFrame moveFrame = new KeyFrame(Duration.seconds(1), event -> {
            if (step[0] < path.size()) {
                Point currentPoint = path.get(step[0]);

                // Highlight the current position
                updateCellStyle(currentPoint.y, currentPoint.x, "#48a1d9"); // Robot's color

                // Clear the previous position after moving
                if (step[0] > 0) {
                    Point prevPoint = path.get(step[0] - 1);
                    if (!prevPoint.equals(currentPoint)) { // Avoid clearing if still at the same charging point
                        updateCellStyle(prevPoint.y, prevPoint.x, "lightgray"); // Reset to default style
                    }
                }

                // Check if the robot is at a charging station
                if (step[0] > 0 && currentPoint.equals(path.get(step[0] - 1))) {
                    // Robot stays at the charging station for 5 seconds
                    timeline.pause();
                    System.out.println("Charging at: " + currentPoint);

                    // Resume after 5 seconds
                    new Timeline(new KeyFrame(Duration.seconds(5), e -> {
                        step[0]++;
                        timeline.play();
                    })).play();
                    return; // Exit this frame early to avoid incrementing step again
                }

                step[0]++; // Move to the next step if not charging
            } else {
                timeline.stop(); // Stop the timeline when the path is complete
            }
        });

        timeline.getKeyFrames().add(moveFrame);
        timeline.setCycleCount(Animation.INDEFINITE); // Set to indefinite to manually control stopping
        timeline.play();
    }
    @FXML
    private void handleSetDestination(){
        w=4;
        System.out.println("Click on a cell to set the robot's destination.");
    }
    @FXML
    private void handleStartSimulation() {
        if (map == null) {
            showError("Please set the map dimensions first.");
            return;
        }
        else{
            w=0;
            System.out.println("Simulation started!");
            robot.batteryLevel=100;
            Pathfinding pathfinding = new Pathfinding(robot.getPosition(),map.getDestination(),map.m,robot,map.getCharginStations());
            pathfinding.findFinalPath();

            for(Point p:pathfinding.finalPath){
                System.out.println("("+p.y+";"+p.x+")");//cos
            }

            simulatePath(pathfinding.finalPath);
            if(pathfinding.finalPath.size()==0){
                showError("impossible de parcourir a la destinations");
            }
        }
    }
        }




