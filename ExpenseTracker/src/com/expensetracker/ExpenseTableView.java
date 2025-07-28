package com.expensetracker;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ExpenseTableView {

    private TableView<Expense> table;
    private ObservableList<Expense> data;
    private final ExpenseDAO expenseDAO = new ExpenseDAO();
    
    public void show() {
        Stage stage = new Stage();
        stage.setTitle("View Expenses");

        table = new TableView<>();

        TableColumn<Expense, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Expense, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Expense, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createStringBinding(
                () -> cellData.getValue().getCategory().getName()
            )
        );

        TableColumn<Expense, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("expenseDate"));

        TableColumn<Expense, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox pane = new HBox(10, editBtn, deleteBtn);

            {
                editBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

                editBtn.setOnAction(e -> {
                    Expense expense = getTableView().getItems().get(getIndex());
                    new EditExpenseView(expense, expenseDAO, ExpenseTableView.this::refreshTable).show();
                });

                deleteBtn.setOnAction(e -> {
                    Expense expense = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure you want to delete this expense?",
                            ButtonType.YES, ButtonType.NO);
                    confirm.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            expenseDAO.deleteExpense(expense.getId());
                            refreshTable();
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        table.getColumns().addAll(descCol, amountCol, categoryCol, dateCol, actionCol);

        refreshTable();  // load data initially

        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(15));
        layout.setCenter(table);

        stage.setScene(new Scene(layout, 800, 400));
        stage.show();
    }

    private void refreshTable() {
        List<Expense> expenseList = expenseDAO.getAllExpenses();
        data = FXCollections.observableArrayList(expenseList);
        table.setItems(data);
    }
}
