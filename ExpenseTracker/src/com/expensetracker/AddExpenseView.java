package com.expensetracker;

import java.time.LocalDate;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AddExpenseView {

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Add Expense");

        // UI Controls
        Label dateLabel = new Label("Date:");
        DatePicker datePicker = new DatePicker(LocalDate.now());

        Label amountLabel = new Label("Amount:");
        TextField amountField = new TextField();

        Label categoryLabel = new Label("Category:");
        ComboBox<Category> categoryBox = new ComboBox<>();

        // Load categories dynamically from DB
        ExpenseDAO dao = new ExpenseDAO();
        List<Category> categories = dao.getAllCategories();
        categoryBox.getItems().addAll(categories);

        Label descLabel = new Label("Description:");
        TextField descField = new TextField();

        Button submitBtn = new Button("Add Expense");

        // Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(dateLabel, 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(amountLabel, 0, 1);
        grid.add(amountField, 1, 1);
        grid.add(categoryLabel, 0, 2);
        grid.add(categoryBox, 1, 2);
        grid.add(descLabel, 0, 3);
        grid.add(descField, 1, 3);
        grid.add(submitBtn, 1, 4);

        // Button action
        submitBtn.setOnAction(e -> {
            try {
                LocalDate date = datePicker.getValue();
                String amountText = amountField.getText().trim();
                String title = descField.getText().trim();
                Category category = categoryBox.getValue();

                if (date == null || amountText.isEmpty() || title.isEmpty() || category == null) {
                    showAlert(Alert.AlertType.ERROR, "All fields are required.");
                    return;
                }

                double amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Amount must be positive.");
                    return;
                }

                Expense expense = new Expense(title, amount, category, date);
                dao.addExpense(expense);

                showAlert(Alert.AlertType.INFORMATION, "Expense added successfully.");
                stage.close();
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid amount.");
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error adding expense.");
            }
        });

        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
