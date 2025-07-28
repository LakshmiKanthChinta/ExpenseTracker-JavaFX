package com.expensetracker;

import java.time.LocalDate;

public class Expense {
    private int id;
    private String description;
    private double amount;
    private Category category;
    private LocalDate expenseDate;

    // Constructors
    public Expense() {}

    public Expense(String description, double amount, Category category, LocalDate expenseDate) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.expenseDate = expenseDate;
    }

    public Expense(int id, String description, double amount, Category category, LocalDate expenseDate) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.expenseDate = expenseDate;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public LocalDate getExpenseDate() { return expenseDate; }
    public void setExpenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; }

    @Override
    public String toString() {
        return id + " | " + description + " | ₹" + amount + " | " +
               (category != null ? category.getName() : "No Category") +
               " | " + (expenseDate != null ? expenseDate.toString() : "No Date");
    }

	
}
