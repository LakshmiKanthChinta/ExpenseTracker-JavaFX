package com.expensetracker;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class EditExpenseView {

    private final Expense expense;
    private final ExpenseDAO expenseDAO;
    private final Runnable onUpdate;

    public EditExpenseView(Expense expense, ExpenseDAO expenseDAO, Runnable onUpdate) {
        this.expense = expense;
        this.expenseDAO = expenseDAO;
        this.onUpdate = onUpdate;
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Edit Expense");

        // UI Controls
        Label descriptionLabel = new Label("Description:");
        TextField descriptionField = new TextField(expense.getDescription());

        Label amountLabel = new Label("Amount:");
        TextField amountField = new TextField(String.valueOf(expense.getAmount()));

        Label categoryLabel = new Label("Category:");
        ComboBox<Category> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll(expenseDAO.getAllCategories());
        categoryCombo.setValue(expense.getCategory());

        Label dateLabel = new Label("Date:");
        DatePicker datePicker = new DatePicker(expense.getExpenseDate());

        Button updateBtn = new Button("Update");
        updateBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        updateBtn.setOnAction(e -> {
            try {
                expense.setDescription(descriptionField.getText());
                expense.setAmount(Double.parseDouble(amountField.getText()));
                expense.setCategory(categoryCombo.getValue());
                expense.setExpenseDate(datePicker.getValue());

                expenseDAO.updateExpense(expense);
                stage.close();
                onUpdate.run(); // refresh table
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input.");
                alert.showAndWait();
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.add(descriptionLabel, 0, 0);
        grid.add(descriptionField, 1, 0);
        grid.add(amountLabel, 0, 1);
        grid.add(amountField, 1, 1);
        grid.add(categoryLabel, 0, 2);
        grid.add(categoryCombo, 1, 2);
        grid.add(dateLabel, 0, 3);
        grid.add(datePicker, 1, 3);
        grid.add(updateBtn, 1, 4);

        stage.setScene(new Scene(grid, 400, 250));
        stage.show();
    }
}
