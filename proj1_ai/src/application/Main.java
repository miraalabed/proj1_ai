package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    private VBox chartContainer;
    private SimulatedAnnealing.Result latestResult;
    private LineChart<Number, Number> lineChart;

    @Override
    public void start(Stage primaryStage) {
        chartContainer = new VBox();
        chartContainer.setAlignment(Pos.CENTER);
        chartContainer.setPrefHeight(500);
        chartContainer.setStyle("-fx-background-color: #F0F8FF;");
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Iteration");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Best Value");
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Rastrigin Function Optimization");
        lineChart.setLegendVisible(false);
        lineChart.setCreateSymbols(false);
        lineChart.setAnimated(false);
        lineChart.setStyle("-fx-background-color: #F0F8FF;");
        lineChart.getData().clear();
        chartContainer.getChildren().add(lineChart);
        Button startButton = new Button("Start");
        styleButton(startButton);
        startButton.setOnAction(e -> {
            SimulatedAnnealing saInstance = new SimulatedAnnealing();
            latestResult = saInstance.run();
            displayConvergenceChart(latestResult);
        });
        Button showResultButton = new Button("Show Result");
        styleButton(showResultButton);
        showResultButton.setOnAction(e -> {
            if (latestResult != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Best Solution Found:\n");
                for (double x : latestResult.solution) {
                    sb.append(String.format("%.4f ", x ));
                }
                sb.append("\n\nFinal Value: ");
                sb.append(String.format("%.4f", latestResult.value));
                sb.append("\n\nRuntime: ").append(latestResult.runtimeMs).append(" ms");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle(" ");
                alert.setContentText(sb.toString());
                alert.getDialogPane().setStyle("-fx-font-size: 14px; -fx-font-family: 'Courier New'; -fx-background-color: #F0F8FF; ");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setTitle(" ");
                alert.setContentText("Please run the algorithm first by clicking Start.");
                alert.getDialogPane().setStyle("-fx-font-size: 14px; -fx-font-family: 'Courier New'; -fx-background-color: #F0F8FF; ");
                alert.showAndWait();
            }
        });
        HBox buttonBox = new HBox(10, startButton, showResultButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        VBox centerBox = new VBox(10, chartContainer, buttonBox);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20));
        BorderPane root = new BorderPane(centerBox);
        root.setBackground(new Background(new BackgroundFill(Color.web("#800000"), CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(root, 700, 500);
        primaryStage.setTitle("Simulated Annealing ");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void displayConvergenceChart(SimulatedAnnealing.Result result) {
        lineChart.getData().clear();

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < result.convergence.length; i++) {
            series.getData().add(new XYChart.Data<>(i, result.convergence[i]));
        }

        lineChart.getData().add(series);
    }
    private void styleButton(Button button) {
        button.setStyle("-fx-font-size: 20px; "
                + "-fx-background-color: linear-gradient(#F0F8FF , #00BFFF); "
                + "-fx-font-weight: bold; "
                + "-fx-text-fill: #800020; "
                + "-fx-cursor: hand; "
                + "-fx-border-width: 2px; "
                + "-fx-padding: 2px 2px;"
                + "-fx-border-radius: 10px;");

        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 22px; "
                + "-fx-background-color: linear-gradient(#00BFFF , #F0F8FF); "
                + "-fx-font-weight: bold; "
                + "-fx-text-fill: #800020; "
                + "-fx-cursor: hand; "
                + "-fx-border-width: 2px; "
                + "-fx-padding: 2px 2px;"
                + "-fx-border-radius: 10px;"));

        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 20px; "
                + "-fx-background-color: linear-gradient(#F0F8FF , #00BFFF); "
                + "-fx-font-weight: bold; "
                + "-fx-text-fill: #800020; "
                + "-fx-cursor: hand; "
                + "-fx-border-width: 2px; "
                + "-fx-padding: 2px 2px;"
                + "-fx-border-radius: 10px;"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
