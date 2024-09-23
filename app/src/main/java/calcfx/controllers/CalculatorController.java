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
        boolean endsWithPeriod = text.endsWith(".");

        if (text.isEmpty() || endsWithOp || endsWithPeriod) {
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

    private String evaluate(String expression) {
        List<String> tokens = new ArrayList<>();
        int i = 0;

        /**
         * Evaluating the multiplication and division operators first and saving
         * their results in the tokens
         *
         * Then, iterating over all the tokens only for addition and subtraction
         */
        while (i < expression.length()) {
            StringBuilder subExpression = new StringBuilder();
            boolean isCurrentCharNumber = Character.isDigit(expression.charAt(i));
            boolean isCurrentCharPeriod = expression.charAt(i) == '.';

            if (isCurrentCharNumber || isCurrentCharPeriod) {
                while (i < expression.length() && (isCurrentCharNumber || isCurrentCharPeriod)) {
                    subExpression.append(expression.charAt(i));
                    i++;
                }
                tokens.add(subExpression.toString());
            } else {
                char operator = expression.charAt(i);
                if (operator == '*' || operator == '/') {
                    double lastNum = Double.parseDouble(tokens.remove(tokens.size() - 1));
                    i++;
                    subExpression.setLength(0);
                    while (
                        i < expression.length() && 
                        (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')
                    ) {
                        subExpression.append(expression.charAt(i));
                        i++;
                    }
                    double subExpressionNum = Double.parseDouble(subExpression.toString());

                    if (operator == '*') {
                        tokens.add(String.valueOf(lastNum * subExpressionNum));
                    } else {
                        tokens.add(String.valueOf(lastNum / subExpressionNum));
                    }
                } else {
                    tokens.add(String.valueOf(operator));
                    i++;
                }
            }
        }

        double result = Double.parseDouble(tokens.get(0));
        i = 1;
        while (i < tokens.size()) {
            String operator = tokens.get(i);
            double nextNum = Double.parseDouble(tokens.get(i + 1));

            if (operator.equals("+")) {
                result += nextNum;
            } else if (operator.equals("-")) {
                result -= nextNum;
            }
            i += 2;
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
