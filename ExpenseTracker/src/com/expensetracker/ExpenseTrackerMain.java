package com.expensetracker;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ExpenseTrackerMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Expense Tracker - JavaFX Version");

        Button addExpenseBtn = new Button("Add Expense");
        Button viewExpensesBtn = new Button("View Expenses");
        Button viewAnalyticsBtn = new Button("View Analytics");
        Button exportButton = new Button("Export to CSV");

        VBox layout = new VBox(10);
        layout.setStyle(
        	    "-fx-background-color: linear-gradient(to bottom, #2c3e50, #4ca1af);" +
        	    "-fx-padding: 40;" +
        	    "-fx-alignment: center;"
        	);

        	String buttonStyle = "-fx-font-size: 14px;" +
        	                     "-fx-background-color: #1abc9c;" +
        	                     "-fx-text-fill: white;" +
        	                     "-fx-padding: 10 20 10 20;" +
        	                     "-fx-background-radius: 5;";

        	addExpenseBtn.setStyle(buttonStyle);
        	viewExpensesBtn.setStyle(buttonStyle);
        	viewAnalyticsBtn.setStyle(buttonStyle);
        	exportButton.setStyle(buttonStyle);

        	Label titleLabel = new Label("Expense Tracker");
        	titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #2c3e50;");
         
        layout.getChildren().addAll(addExpenseBtn, viewExpensesBtn, viewAnalyticsBtn, exportButton); 

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

        addExpenseBtn.setOnAction(e -> {
            AddExpenseView view = new AddExpenseView();
            view.show();
        });

        viewExpensesBtn.setOnAction(e -> {
            ExpenseTableView view = new ExpenseTableView();
            view.show();
        });

        viewAnalyticsBtn.setOnAction(e -> {
            AnalyticsView view = new AnalyticsView();
            view.show();
        });

        exportButton.setOnAction(e -> {
            ExpenseDAO dao = new ExpenseDAO();
            List<Expense> expenses = dao.getAllExpenses();
            CSVExporter.exportExpensesToCSV(expenses);
            
         // Show confirmation alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Complete");
            alert.setHeaderText(null);
            alert.setContentText("Expenses exported to expenses.csv successfully!");
            alert.showAndWait();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
