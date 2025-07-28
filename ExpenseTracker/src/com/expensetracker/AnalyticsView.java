package com.expensetracker;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AnalyticsView {

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Monthly Expense Analytics");

        ExpenseDAO dao = new ExpenseDAO();
        List<Expense> expenses = dao.getAllExpenses();

        // Step 1: Extract unique years from expenses
        Set<Integer> years = new TreeSet<>();
        for (Expense e : expenses) {
            years.add(e.getExpenseDate().getYear());
        }

        // Step 2: Create year selector
        ComboBox<Integer> yearComboBox = new ComboBox<>();
        yearComboBox.getItems().addAll(years);
        yearComboBox.setValue(LocalDate.now().getYear()); // default to current year

        SwingNode swingNode = new SwingNode();

        // Step 3: Chart rendering logic
        Runnable updateChart = () -> {
            int selectedYear = yearComboBox.getValue();
            Map<Integer, Double> monthTotals = new TreeMap<>();

            // Initialize all 12 months with 0.0
            for (int month = 1; month <= 12; month++) {
                monthTotals.put(month, 0.0);
            }

            // Aggregate expense data
            for (Expense e : expenses) {
                if (e.getExpenseDate().getYear() == selectedYear) {
                    int month = e.getExpenseDate().getMonthValue();
                    monthTotals.put(month, monthTotals.get(month) + e.getAmount());
                }
            }

            // Prepare dataset
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (int month = 1; month <= 12; month++) {
                String monthName = Month.of(month).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
                dataset.addValue(monthTotals.get(month), "Expenses", monthName);
            }

            // Create the chart
            JFreeChart chart = ChartFactory.createBarChart(
                    "Monthly Expenses - " + selectedYear,
                    "Month",
                    "Amount",
                    dataset,
                    PlotOrientation.VERTICAL,
                    false, // no legend
                    true,  // tooltips
                    false  // URLs
            );

            // Style: Rotate month labels
            CategoryPlot plot = chart.getCategoryPlot();
            CategoryAxis domainAxis = plot.getDomainAxis();
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

            // Show chart in panel
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new java.awt.Dimension(1000, 700)); // Larger size

            // Embed into SwingNode on FX thread
            SwingUtilities.invokeLater(() -> swingNode.setContent(chartPanel));
        };

        // Initial chart
        updateChart.run();

        // When year changes, update chart
        yearComboBox.setOnAction(e -> updateChart.run());

        // Layout
        VBox layout = new VBox(10, yearComboBox, swingNode);
        layout.setPadding(new Insets(10));

        // Use full screen size
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(layout, screenBounds.getWidth() * 0.9, screenBounds.getHeight() * 0.9);

        stage.setScene(scene);
        stage.setMaximized(true); // Maximize on open
        stage.show();
    }
}
