package calcfx.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CalculatorController {

    @FXML
    private TextField display;

    private final ArrayList<String> operators = new ArrayList<>(Arrays.asList("+", "-", "*", "/"));


    public void onClickInputButton(ActionEvent event) {
        String buttonText = ((javafx.scene.control.Button) event.getSource()).getText();
        display.appendText(buttonText);
    }

    public void onClickOperator(ActionEvent event) {
        String operator = ((javafx.scene.control.Button) event.getSource()).getText();
        String text = display.getText();
        String[] operatorArr = new String[operators.size()];
        operators.toArray(operatorArr);
        boolean endsWithOp = Arrays.stream(operatorArr).anyMatch(text::endsWith);

        if (text.isEmpty() || endsWithOp) {
            return;
        }
        display.appendText(operator);
    }

    public void onClickPeriod(ActionEvent event) {
        String text = display.getText();
        String[] slices = text.split("[+\\-*/]");
        String currentScope = slices.length > 0 ? slices[slices.length - 1] : "";

        if (currentScope.contains(".")) {
            return;
        }

        display.appendText(".");
    }

    public void onClickClear(ActionEvent event) {
        display.clear();
    }

    public void onClickDelete(ActionEvent event) {
        String text = display.getText();
        if (text.isEmpty()) {
            return;
        }

        display.deleteText(text.length() - 1, text.length());
    }

    // Must follow BODMAS/BIDMAS rule
    // Implementing a two-pass algorithm to handle multiplication and division first
    // and then addition and subtraction
    private String evaluate(String expression) {
        // First pass: Handle multiplication and division
        List<String> tokens = new ArrayList<>();
        int i = 0;
        while (i < expression.length()) {
            StringBuilder sb = new StringBuilder();
            // Collecting digits and '.' for numbers
            if (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.') {
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i));
                    i++;
                }
                tokens.add(sb.toString());
            } 
            // Handle operators
            else {
                char operator = expression.charAt(i);
                if (operator == '*' || operator == '/') {
                    double num1 = Double.parseDouble(tokens.remove(tokens.size() - 1)); // last number
                    i++; // move past the operator
                    sb.setLength(0); // reset StringBuilder
                    // Collect next number
                    while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                        sb.append(expression.charAt(i));
                        i++;
                    }
                    double num2 = Double.parseDouble(sb.toString());
                    
                    // Perform multiplication or division
                    if (operator == '*') {
                        tokens.add(String.valueOf(num1 * num2));
                    } else {
                        tokens.add(String.valueOf(num1 / num2));
                    }
                } else {
                    // For addition and subtraction, just add them to the token list
                    tokens.add(String.valueOf(operator));
                    i++;
                }
            }
        }

        // Second pass: Handle addition and subtraction
        double result = Double.parseDouble(tokens.get(0));
        i = 1;
        while (i < tokens.size()) {
            String operator = tokens.get(i);
            double nextNum = Double.parseDouble(tokens.get(i + 1));

            // Perform addition or subtraction
            if (operator.equals("+")) {
                result += nextNum;
            } else if (operator.equals("-")) {
                result -= nextNum;
            }
            i += 2; // Move to next operator and number
        }

        return String.valueOf(result);
    }


    public void onClickEqual(ActionEvent event) {
        String text = display.getText();
        String[] operatorArr = new String[operators.size()];
        operators.toArray(operatorArr);
        boolean endsWithOp = Arrays.stream(operatorArr).anyMatch(text::endsWith);
        boolean endsWithPeriod = text.endsWith(".");

        if (text.isEmpty() || endsWithOp || endsWithPeriod) {
            return;
        }

        String result = evaluate(text);
        display.setText(result);
    }
}
