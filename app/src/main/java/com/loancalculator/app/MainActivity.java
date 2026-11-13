
package com.loancalculator.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.text.InputType;
import java.util.Locale;

public class MainActivity extends Activity {

    EditText monthlyPayment, loanTerm, interestRate;
    EditText processingRate, serviceCharge, notarialFee;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 35, 30, 30);
        layout.setBackgroundColor(Color.WHITE);

        TextView title = new TextView(this);
        title.setText("LOAN COMPUTATION");
        title.setTextSize(25);
        title.setTextColor(Color.rgb(20, 60, 120));
        title.setGravity(Gravity.CENTER);
        layout.addView(title);

        monthlyPayment = addInput(layout, "Monthly Payment", "3000");
        loanTerm = addInput(layout, "Loan Term (Months)", "12");
        interestRate = addInput(layout, "Interest Rate (%)", "3.7");
        processingRate = addInput(layout, "Processing Fee Rate (%)", "50.4");
        serviceCharge = addInput(layout, "Service Charge", "420");
        notarialFee = addInput(layout, "Notarial Fee", "350");

        Button calculate = new Button(this);
        calculate.setText("CALCULATE LOAN");
        layout.addView(calculate);

        result = new TextView(this);
        result.setTextSize(16);
        result.setTextColor(Color.BLACK);
        result.setPadding(0, 25, 0, 20);
        layout.addView(result);

        calculate.setOnClickListener(v -> calculateLoan());

        ScrollView scroll = new ScrollView(this);
        scroll.addView(layout);
        setContentView(scroll);

        calculateLoan();
    }

    private EditText addInput(
            LinearLayout layout, String hint, String value) {

        TextView label = new TextView(this);
        label.setText(hint);
        label.setTextSize(14);
        label.setTextColor(Color.DKGRAY);
        layout.addView(label);

        EditText input = new EditText(this);
        input.setSingleLine(true);
        input.setInputType(
            InputType.TYPE_CLASS_NUMBER |
            InputType.TYPE_NUMBER_FLAG_DECIMAL
        );
        input.setText(value);
        layout.addView(input);

        return input;
    }

    private String peso(double amount) {
        return String.format(Locale.US, "₱%,.2f", amount);
    }

    private void calculateLoan() {
        try {
            double payment = Double.parseDouble(
                monthlyPayment.getText().toString());

            int months = Integer.parseInt(
                loanTerm.getText().toString());

            double rate = Double.parseDouble(
                interestRate.getText().toString());

            double processing = Double.parseDouble(
                processingRate.getText().toString());

            double service = Double.parseDouble(
                serviceCharge.getText().toString());

            double notarial = Double.parseDouble(
                notarialFee.getText().toString());

            if (payment <= 0 || months <= 0 ||
                rate < 0 || processing < 0 ||
                service < 0 || notarial < 0) {

                result.setText("Please enter valid values.");
                return;
            }

            // STEP 1: Gross
            double gross = payment * months;

            // STEP 2: Total Interest
            double interest = (rate / 100.0) *
                payment * (months * (months + 1.0) / 2.0);

            // STEP 3: Processing Fee
            double processingFee =
                payment * (processing / 100.0);

            // STEP 4: Total Deductions
            double deductions = interest +
                processingFee + service + notarial;

            // STEP 5: Net Amount
            double net = gross - deductions;

            String output =
                "LOAN COMPUTATION SUMMARY\n\n" +
                "Monthly Payment: " + peso(payment) + "\n" +
                "Loan Term: " + months + " months\n" +
                "Interest Rate: " + rate + "%\n\n" +

                "GROSS: " + peso(gross) + "\n\n" +

                "Total Interest: (" + peso(interest) + ")\n" +
                "Processing Fee: (" + peso(processingFee) + ")\n" +
                "Service Charge: (" + peso(service) + ")\n" +
                "Notarial Fee: (" + peso(notarial) + ")\n\n" +

                "TOTAL DEDUCTIONS: " + peso(deductions) +
                "\n\n" +

                "FINAL NET AMOUNT\n" + peso(net);

            result.setText(output);

        } catch (Exception e) {
            result.setText("Please complete all fields correctly.");
        }
    }
}
