package com.expensetracker;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVExporter {

    public static void exportExpensesToCSV(List<Expense> expenses) {
    	String fileName = "expenses_export.csv";
    	
        try (FileWriter writer = new FileWriter(fileName)) {
            // Updated Header
            writer.write("Description,Amount,Category,Date\n");

            // Data rows
            for (Expense e : expenses) {
                writer.write(
                    escapeCSV(e.getDescription()) + "," +
                    e.getAmount() + "," +
                    escapeCSV(e.getCategory().getName()) + "," +
                    e.getExpenseDate() + "\n"
                );
            }

            System.out.println("Expenses exported to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handles commas, quotes etc.
    private static String escapeCSV(String value) {
        if (value.contains(",") || value.contains("\"")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
}
