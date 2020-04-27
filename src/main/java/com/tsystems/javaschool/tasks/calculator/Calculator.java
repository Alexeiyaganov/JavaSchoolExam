package com.tsystems.javaschool.tasks.calculator;


import java.util.*;

public class Calculator {

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */

    private static String operators = "+-*/";
    private static String separators = "() ";
    public static boolean flag = true;
    private static Map<String, Integer> priorityMap;
    private static Set<String> separatorSet;
    private static Set<String> operatorSet;
    // создание таблиц операторов, разделителей и приоритетов операций
    static {
        priorityMap = new HashMap<>();
        separatorSet = new HashSet<>();
        operatorSet = new HashSet<>();
        priorityMap.put("(", 1);
        priorityMap.put("-", 2);
        priorityMap.put("+", 2);
        priorityMap.put("*", 3);
        priorityMap.put("/", 3);
        for (int i = 0; i < separators.length(); i++) {
            separatorSet.add("" + separators.charAt(i));
        }
        for (int i = 0; i < operators.length(); i++) {
            operatorSet.add("" + operators.charAt(i));
        }
    }

    // проверка текущего символа на разделители
    private static boolean isDelimiter(String sign) {
        return separatorSet.contains(sign);
    }
    // проверка текущего символа на разделители
    private static boolean isOperator(String token) {
        return operatorSet.contains(token);
    }

    // задание приоритетов операций
    private static int priority(String sign) throws CalculatorParseException{
        int priority = priorityMap.get(sign);
        if (sign == null)
            throw new CalculatorParseException("Не возможно задать приоритет этому оператору");
        return priority;
    }

    // Отнесение каждого входного char к типу разделителей, операторов, пробелов или цифр
    private static SignType typeRecognizer(String sign) {
        if (sign.equals(" "))
            return SignType.SPACE;
        if (isOperator(sign))
            return SignType.OPERATOR;
        if (isDelimiter(sign))
            return SignType.SEPARATOR;
        return SignType.NUMERAL;
    }

    private enum SignType{
        SPACE,
        NUMERAL,
        OPERATOR,
        SEPARATOR
    }

    // функция парсинга входящей строки
    public static List<String> parse(String input) throws CalculatorParseException {
        List<String> output = new ArrayList<String>();
        Deque<String> stack = new ArrayDeque<String>(); // очередь(стек)
        StringTokenizer tokenizer = new StringTokenizer(input, separators + operators, true); //анализатор текста, где операторы и скобки являются разделителями
        String prev = "";
        String curr = "";
        while (tokenizer.hasMoreTokens()) {
            curr = tokenizer.nextToken();
            switch (typeRecognizer(curr)) {
                case SPACE:
                    continue;

                case NUMERAL:
                    if (curr.equals(".")&& prev.equals("."))
                        throw new CalculatorParseException("Некорректное использование разделителя в десятичных чисел");
                    output.add(curr);
                    break;

                case SEPARATOR:
                    if (curr.equals("(")) stack.push(curr);
                    else if (curr.equals(")")) {
                        while (!stack.peek().equals("(")) {
                            output.add(stack.pop());
                            if (stack.isEmpty()) {
                                throw new CalculatorParseException("Нету парной закрывающейся скобки");
                            }
                        }
                        stack.pop();
                    }
                    break;

                case OPERATOR:
                    if (!tokenizer.hasMoreTokens()) {
                        throw new CalculatorParseException("Некорректный синтаксис");
                    }
                    if (curr.equals("-") && (prev.equals("") || (isDelimiter(prev)  && !prev.equals(")")))) {
                        output.add("0");
                    }
                    else {
                        while (!stack.isEmpty() && (priority(curr) <= priority(stack.peek()))) {
                            output.add(stack.pop());
                        }

                    }
                    stack.push(curr);
                    break;
            }
            if (isOperator(prev) && isOperator(curr))
                throw new CalculatorParseException("два оператора идут друг за другом");
            prev = curr;
        }

        while (!stack.isEmpty()) {
            if (isOperator(stack.peek())) output.add(stack.pop());
            else {
                throw new CalculatorParseException("Нету парной закрывающейся скобки");
            }
        }
        return output;
    }

    // достаем значения из стека и производим арифметические операции
    public static Double calc(List<String> strOut) throws CalculateException {
        Deque<Double> stack = new ArrayDeque<Double>();  // очередь(стек)
        Double a, b;
        for (String x : strOut) {
            switch (x) {
                case "+":
                    stack.push(stack.pop() + stack.pop());
                    break;

                case "-":
                    b = stack.pop();
                    a = stack.pop();
                    stack.push(a - b);
                    break;

                case "*":
                    stack.push(stack.pop() * stack.pop());
                    break;

                case "/":
                    b = stack.pop();
                    if (b == 0)
                        throw new CalculateException("деление на ноль");
                    a = stack.pop();
                    stack.push(a / b);
                    break;

                default:
                    try {
                        stack.push(Double.parseDouble(x));
                        break;
                    } catch (NumberFormatException e) {
                        throw new CalculateException("Ошибка парсинга элемента");
                    }
            }
        }
        return stack.pop();
    }

        // оценка и проверка результата калькуляции
    public String evaluate(String statement) {
        if (statement == null || statement == "")
            return null;
        String stringResult;
        try {
            Double doubleResult = calc(parse(statement));
            doubleResult*=10000;
            int intres = (int) Math.round(doubleResult);
            doubleResult=(double)intres/10000;
            if (doubleResult == Math.floor(doubleResult)) {
                stringResult = String.valueOf(doubleResult.intValue());
            } else {
                stringResult = String.valueOf(doubleResult);
            }
        } catch (CalculatorParseException cpe) {
            System.out.println(cpe.getMessage());
            return null;
        } catch (CalculateException ce){
            System.out.println(ce.getMessage());
            return null;
        }
        return stringResult;
    }

}
