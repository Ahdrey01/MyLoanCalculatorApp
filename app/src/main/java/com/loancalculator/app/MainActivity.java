package com.loancalculator.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;
import android.text.InputType;

public class MainActivity extends Activity {

    EditText amount, interest, years;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(35, 40, 35, 35);
        layout.setBackgroundColor(Color.WHITE);

        TextView title = new TextView(this);
        title.setText("MY LOAN CALCULATOR");
        title.setTextSize(25);
        title.setTextColor(Color.rgb(20, 60, 120));
        title.setGravity(Gravity.CENTER);
        layout.addView(title);

        amount = addInput(layout, "Loan Amount");
        interest = addInput(layout, "Annual Interest Rate (%)");
        years = addInput(layout, "Loan Term (Years)");

        Button calculate = new Button(this);
        calculate.setText("CALCULATE");
        layout.addView(calculate);

        result = new TextView(this);
        result.setTextSize(18);
        result.setTextColor(Color.BLACK);
        result.setPadding(0, 30, 0, 0);
        layout.addView(result);

        calculate.setOnClickListener(v -> calculateLoan());

        ScrollView scroll = new ScrollView(this);
        scroll.addView(layout);
        setContentView(scroll);
    }

    private EditText addInput(LinearLayout layout, String hint) {
        EditText input = new EditText(this);
        input.setHint(hint);
        input.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(input);
        return input;
    }

    private void calculateLoan() {
        try {
            double principal = Double.parseDouble(
                    amount.getText().toString());
            double rate = Double.parseDouble(
                    interest.getText().toString());
            int term = Integer.parseInt(
                    years.getText().toString());

            if (principal <= 0 || rate < 0 || term <= 0) {
                result.setText("Please enter valid values.");
                return;
            }

            int months = term * 12;
            double monthlyRate = rate / 1200.0;

            double payment = monthlyRate == 0
                    ? principal / months
                    : principal * monthlyRate /
                    (1 - Math.pow(1 + monthlyRate, -months));

            double total = payment * months;

            result.setText(String.format(
                    java.util.Locale.US,
                    "Monthly Payment: %.2f\n\nTotal Payment: %.2f\n\nTotal Interest: %.2f",
                    payment, total, total - principal));

        } catch (Exception e) {
            result.setText("Please enter all details correctly.");
        }
    }
}
