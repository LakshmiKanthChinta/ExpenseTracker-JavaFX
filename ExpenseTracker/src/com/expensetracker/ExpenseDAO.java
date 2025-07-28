package com.expensetracker;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    // Add new expense
    public void addExpense(Expense expense) {
        String sql = "INSERT INTO expenses (description, amount, category_id, expense_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, expense.getDescription());
            stmt.setDouble(2, expense.getAmount());
            stmt.setInt(3, expense.getCategory().getId());
            stmt.setDate(4, Date.valueOf(expense.getExpenseDate()));

            stmt.executeUpdate();
            System.out.println("Expense added successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all expenses with category names
    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT e.*, c.name AS category_name FROM expenses e JOIN categories c ON e.category_id = c.id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"));
                Expense expense = new Expense(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getDouble("amount"),
                        category,
                        rs.getDate("expense_date").toLocalDate()
                );
                expenses.add(expense);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expenses;
    }

    // Get all categories
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Category category = new Category(rs.getInt("id"), rs.getString("name"));
                categories.add(category);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    // Update an existing expense
    public void updateExpense(Expense expense) {
        String sql = "UPDATE expenses SET description = ?, amount = ?, category_id = ?, expense_date = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, expense.getDescription());
            stmt.setDouble(2, expense.getAmount());
            stmt.setInt(3, expense.getCategory().getId());
            stmt.setDate(4, Date.valueOf(expense.getExpenseDate()));
            stmt.setInt(5, expense.getId());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Expense updated successfully.");
            } else {
                System.out.println("No expense found with ID: " + expense.getId());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete an expense by ID
    public void deleteExpense(int id) {
        String sql = "DELETE FROM expenses WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Expense deleted successfully.");
            } else {
                System.out.println("No expense found with that ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
