package application;

import java.util.List;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.geometry.Insets;
import javafx.scene.chart.XYChart;

public class SavingsCalculatorApplication extends Application {

    @Override
    public void start(Stage stage) {
        //First slider, labels
        Label monthlySavingsLabel = new Label("Monthly savings");
        Slider monthlySavingsSlider = new Slider(25, 250, 25);
        Label monthlySavingsIndicator = new Label("25");
        monthlySavingsSlider.setShowTickMarks(true);
        monthlySavingsSlider.setShowTickLabels(true);
        monthlySavingsSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            monthlySavingsIndicator.setText(String.format("%.1f", newValue.doubleValue()));
        });

        //Second slider, labels
        Label yearlyInterestLabel = new Label("Yearly interest rate");
        Slider yearlyInterestSlider = new Slider(0, 10, 0);
        Label yearlyInterestIndicator = new Label("0");
        yearlyInterestSlider.setShowTickMarks(true);
        yearlyInterestSlider.setShowTickLabels(true);
        yearlyInterestSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            yearlyInterestIndicator.setText(String.format("%.0f", newValue.doubleValue()));
        });

        //Line chart
        NumberAxis xAxis = new NumberAxis(0, 30, 1);
        NumberAxis yAxis = new NumberAxis(0, 125000, 25000);
        LineChart lineChart = new LineChart(xAxis, yAxis);
        lineChart.setTitle("Savings calculator");

        //Layout
        BorderPane layout = new BorderPane();
        BorderPane upperSliderLayout = new BorderPane();
        BorderPane lowerSliderLayout = new BorderPane();
        Insets sliderInsets = new Insets(10, 20, 20, 10);
        upperSliderLayout.setPadding(sliderInsets);
        lowerSliderLayout.setPadding(sliderInsets);
        upperSliderLayout.setLeft(monthlySavingsLabel);
        upperSliderLayout.setCenter(monthlySavingsSlider);
        upperSliderLayout.setRight(monthlySavingsIndicator);
        lowerSliderLayout.setLeft(yearlyInterestLabel);
        lowerSliderLayout.setCenter(yearlyInterestSlider);
        lowerSliderLayout.setRight(yearlyInterestIndicator);
        VBox sliderLayout = new VBox();
        sliderLayout.getChildren().addAll(upperSliderLayout, lowerSliderLayout);
        layout.setTop(sliderLayout);
        layout.setCenter(lineChart);

        XYChart.Series<Number, Number> noInterestSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> withInterestSeries = new XYChart.Series<>();

        for (int i = 0; i <= 30; i++) {
            noInterestSeries.getData().add(new XYChart.Data<>(i, 0));
            withInterestSeries.getData().add(new XYChart.Data<>(i, 0));
        }

        lineChart.getData().addAll(noInterestSeries, withInterestSeries);

        //"Real time" update
        updateChart(noInterestSeries, withInterestSeries,
                monthlySavingsSlider.getValue(),
                yearlyInterestSlider.getValue());

        monthlySavingsSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            monthlySavingsIndicator.setText(String.format("%.1f", newVal.doubleValue()));
            updateChart(noInterestSeries, withInterestSeries,
                    newVal.doubleValue(),
                    yearlyInterestSlider.getValue());
        });

        yearlyInterestSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            yearlyInterestIndicator.setText(String.format("%.0f", newVal.doubleValue()));
            updateChart(noInterestSeries, withInterestSeries,
                    monthlySavingsSlider.getValue(),
                    newVal.doubleValue());
        });

        lineChart.setLegendVisible(false);

        //Finalizing
        Scene view = new Scene(layout);
        stage.setScene(view);
        stage.show();
    }

    private void updateChart(XYChart.Series<Number, Number> noInterestSeries,
            XYChart.Series<Number, Number> withInterestSeries,
            double monthlySaving,
            double yearlyInterest) {

        MonthlySavings ms = new MonthlySavings(monthlySaving, yearlyInterest);
        List<Double> noInterest = ms.drawList();
        List<Double> withInterest = ms.drawListWithInterestRate();

        for (int i = 0; i <= 30; i++) {
            noInterestSeries.getData().get(i).setYValue(noInterest.get(i));
            withInterestSeries.getData().get(i).setYValue(withInterest.get(i));
        }
    }

    public static void main(String[] args) {
        launch(SavingsCalculatorApplication.class);
    }
}
