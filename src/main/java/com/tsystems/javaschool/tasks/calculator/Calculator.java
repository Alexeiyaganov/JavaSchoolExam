package com.tsystems.javaschool.tasks.calculator;

import java.util.Stack;

public class EvaluateString
{
    public static double evaluate(String expression)
    {
        char[] tokens = expression.toCharArray();

        Stack<Double> value_st = new Stack<Double>();  // стек для чисел

        Stack<Character> oper_st = new Stack<Character>();   //  cтек для операторов

        for (int i = 0; i < tokens.length; i++)
        {
            if (tokens[i] == ' ')   // текущий символ-пробел, пропускаем
                continue;

            if (tokens[i] >= '0' && tokens[i] <= '9')
            {
                StringBuffer sbuf = new StringBuffer();
                while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9')   // учитываем многозначные числа
                    sbuf.append(tokens[i++]);
                value_st.push(Double.parseDouble(sbuf.toString()));  // преобразуем в Integer и заносим в стек число
            }

            else if (tokens[i] == '(')   // открывающуюся скобку заносим в стек операторов
                oper_st.push(tokens[i]);

            else if (tokens[i] == ')')  // при текущей закрывающейся скобки-решаем все, что находится между открывающейся и этой
            {
                while (oper_st.peek() != '(')
                    value_st.push(applyOp(oper_st.pop(), value_st.pop(), value_st.pop())); // cама операция
                oper_st.pop();
            }

            // текущий символ-оператор
            else if (tokens[i] == '+' || tokens[i] == '-' ||
                    tokens[i] == '*' || tokens[i] == '/')
            {

                // пока оператор в стеке имеет болший приоритет, чем текущий-
                // используем его с двумя числами в вершине стека чисел
                while (!oper_st.empty() && hasPrecedence(tokens[i], oper_st.peek()))
                    value_st.push(applyOp(oper_st.pop(), value_st.pop(), value_st.pop()));

                oper_st.push(tokens[i]);   //Заносим текущий оператор в стек операторов
            }
        }


        while (!oper_st.empty())
            value_st.push(applyOp(oper_st.pop(), value_st.pop(), value_st.pop()));  // производим операции, пока стек операторов не станет пустым

        // В конце в концов в стеке значений останется результат-возвращаем его
        return value_st.pop();
    }


    // Возвращает true, если второй оператор имеет высший приоритет, иначе false
    public static boolean hasPrecedence(char op1, char op2)
    {
        if (op2 == '(' || op2 == ')')
            return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
            return false;
        else
            return true;
    }

    // Процесс операции с двумя числами
    public static double applyOp(char op, double b, double a)
    {
        switch (op)
        {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new
                            UnsupportedOperationException("Cannot divide by zero");
                return a / b;
        }
        return 0;
    }

    public static void main(String[] args)
    {
        System.out.println(EvaluateString.evaluate("( 1 + 38 ) * 4 - 5"));
        System.out.println(EvaluateString.evaluate("7 * 6 / 2 + 8"));
        System.out.println(EvaluateString.evaluate("1 / 0"));
    }
}
