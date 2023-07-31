package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.calculator.databinding.ActivityMainBinding;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.buttonOne.setOnClickListener(this);
        binding.buttonTwo.setOnClickListener(this);
        binding.buttonThree.setOnClickListener(this);
        binding.buttonFour.setOnClickListener(this);
        binding.buttonFive.setOnClickListener(this);
        binding.buttonSix.setOnClickListener(this);
        binding.buttonSeven.setOnClickListener(this);
        binding.buttonEight.setOnClickListener(this);
        binding.buttonNine.setOnClickListener(this);
        binding.buttonZero.setOnClickListener(this);

        binding.buttonDot.setOnClickListener(this);
        binding.buttonOpenBracket.setOnClickListener(this);
        binding.buttonCloseBracket.setOnClickListener(this);

        binding.buttonC.setOnClickListener(this);
        binding.buttonAC.setOnClickListener(this);
        binding.buttonEqual.setOnClickListener(this);

        binding.buttonPlus.setOnClickListener(this);
        binding.buttonMinus.setOnClickListener(this);
        binding.buttonDiv.setOnClickListener(this);
        binding.buttonMulti.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_one:
                binding.result.setText(binding.result.getText() + "1");
                break;
            case R.id.button_two:
                binding.result.setText(binding.result.getText() + "2");
                break;
            case R.id.button_three:
                binding.result.setText(binding.result.getText() + "3");
                break;
            case R.id.button_four:
                binding.result.setText(binding.result.getText() + "4");
                break;
            case R.id.button_five:
                binding.result.setText(binding.result.getText() + "5");
                break;
            case R.id.button_six:
                binding.result.setText(binding.result.getText() + "6");
                break;
            case R.id.button_seven:
                binding.result.setText(binding.result.getText() + "7");
                break;
            case R.id.button_eight:
                binding.result.setText(binding.result.getText() + "8");
                break;
            case R.id.button_nine:
                binding.result.setText(binding.result.getText() + "9");
                break;
            case R.id.button_zero:
                binding.result.setText(binding.result.getText() + "0");
                break;

            case R.id.button_openBracket:
                binding.result.setText(binding.result.getText() + "(");
                break;
            case R.id.button_closeBracket:
                binding.result.setText(binding.result.getText() + ")");
                break;
            case R.id.button_Dot:
                binding.result.setText(binding.result.getText() + ".");
                break;

            case R.id.button_plus:
                binding.result.setText(binding.result.getText() + "+");
                break;
            case R.id.button_minus:
                binding.result.setText(binding.result.getText() + "-");
                break;
            case R.id.button_multi:
                binding.result.setText(binding.result.getText() + "*");
                break;
            case R.id.button_div:
                binding.result.setText(binding.result.getText() + "/");
                break;

            case R.id.button_C:
                try {
                    binding.result.setText(binding.result.getText().toString().substring(0,
                            binding.result.getText().toString().length()-1));
                } catch (Exception e) {
                    binding.solution.setText("Silinecek öğe yok!");
                }

                break;
            case R.id.button_AC:
                binding.solution.setText("");
                binding.result.setText("");
                break;
            case R.id.button_equal:
                try {
                    DecimalFormatSymbols customSymbols = new DecimalFormatSymbols(Locale.getDefault());
                    customSymbols.setDecimalSeparator('.');
                    DecimalFormat decimalFormat = new DecimalFormat("#.###", customSymbols);

                    binding.solution.setText(binding.result.getText().toString());
                    binding.result.setText(decimalFormat.format(
                            evaluateMathExpression(binding.solution.getText().toString())));
                } catch (Exception e) {
                    binding.solution.setText("Girdide hata var!");
                }

                break;

            default:
                break;

        }
    }

    private static int getPriority(char operator) {
        if (operator == '+' || operator == '-') {
            return 1;
        } else if (operator == '*' || operator == '/') {
            return 2;
        }
        return 0;
    }

    private static double performOperation(double num1, double num2, char operator) {
        switch (operator) {
            case '+':
                return num1 + num2;
            case '-':
                return num1 - num2;
            case '*':
                return num1 * num2;
            case '/':
                return num1 / num2;
            default:
                return 0;
        }
    }

    public static double evaluateMathExpression(String expression) {

        expression = expression.trim();

        if (expression.startsWith("-")) {
            expression = "0" + expression;
        }

        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isDigit(ch) || ch == '.') {
                StringBuilder numBuilder = new StringBuilder();
                numBuilder.append(ch);
                while (i + 1 < expression.length() &&
                        (Character.isDigit(expression.charAt(i + 1)) || expression.charAt(i + 1) == '.')) {
                    numBuilder.append(expression.charAt(i + 1));
                    i++;
                }
                double num = Double.parseDouble(numBuilder.toString());
                numbers.push(num);
            } else if (ch == '(') {
                operators.push(ch);
            } else if (ch == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    double num2 = numbers.pop();
                    double num1 = numbers.pop();
                    char operator = operators.pop();
                    numbers.push(performOperation(num1, num2, operator));
                }
                operators.pop();
            } else {
                while (!operators.isEmpty() && getPriority(operators.peek()) >= getPriority(ch)) {
                    double num2 = numbers.pop();
                    double num1 = numbers.pop();
                    char operator = operators.pop();
                    numbers.push(performOperation(num1, num2, operator));
                }
                operators.push(ch);
            }
        }
        while (!operators.isEmpty()) {
            double num2 = numbers.pop();
            double num1 = numbers.pop();
            char operator = operators.pop();
            numbers.push(performOperation(num1, num2, operator));
        }

        return numbers.pop();
    }

}