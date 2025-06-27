package application;

import java.util.List;
import java.util.ArrayList;

public class MonthlySavings {

    private double monthlySaving;
    private double interestRate;

    public MonthlySavings(double monthlySaving, double interestRate) {
        this.monthlySaving = monthlySaving;
        this.interestRate = interestRate;
    }

    public List<Double> drawList() {
        List<Double> savings = new ArrayList<>();
        double savingStack = 0;
        for (int i = 0; i < 31; i++) {
            if (i == 0) {
                savings.add(0.0);
            } else {
                savingStack += this.monthlySaving * 12;
                savings.add(savingStack);
            }
        }
        return savings;
    }

    public List<Double> drawListWithInterestRate() {
        List<Double> savings = new ArrayList<>();
        double total = 0;
        for (int year = 0; year <= 30; year++) {
            if (year == 0) {
                savings.add(0.0);
            } else {
                total = savings.get(year - 1) * (1 + this.interestRate / 100.0)
                        + this.monthlySaving * 12;
                savings.add(total);
            }
        }
        return savings;
    }
}
