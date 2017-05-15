/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.collections.ArrayStack;

public class ShuntingYard {

    private enum Operator {

        ADD(1), SUBTRACT(2), MULTIPLY(3), DIVIDE(4), EQUALS(8), AND(6), OR(7);
        final int precedence;

        Operator(int p) {
            precedence = p;
        }
    }

    private static Map<String, Operator> ops = new HashMap<String, Operator>() {
        {
            put("+", Operator.ADD);
            put("-", Operator.SUBTRACT);
            put("*", Operator.MULTIPLY);
            put("/", Operator.DIVIDE);
            put("AND", Operator.AND);
            put("OR", Operator.OR);
            put("=", Operator.EQUALS);
        }
    };

    private static boolean isHigerPrec(String op, String sub) {
        return (ops.containsKey(sub) && ops.get(sub).precedence >= ops.get(op).precedence);
    }

    public static String postfix(String infix) {
        StringBuilder output = new StringBuilder();
        List<String> outputList = new ArrayList<>();
        Deque<String> stack = new LinkedList<>();

        for (String token : infix.split("\\s")) {
            // operator
            if (ops.containsKey(token)) {
                while (!stack.isEmpty() && isHigerPrec(token, stack.peek())) {
                    String popedOperator = stack.pop();
                    output.append(popedOperator).append(' ');
                    outputList.add(popedOperator);
                }
                stack.push(token);

                // left parenthesis
            } else if (token.equals("(")) {
                stack.push(token);

                // right parenthesis
            } else if (token.equals(")")) {
                while (!stack.peek().equals("(")) {
                    String popedOperator = stack.pop();
                    output.append(popedOperator).append(' ');
                    outputList.add(popedOperator);
                }
                stack.pop();

                // digit
            } else {
                output.append(token).append(' ');
                outputList.add(token);
            }
        }

        while (!stack.isEmpty()) {
            String popedOperator = stack.pop();
            output.append(stack.pop()).append(' ');
            outputList.add(popedOperator);
        }
        System.out.println(outputList);
        return output.toString();
    }

    public static void main(String argv[]) {
        String[] firstnames = {"john", "david", "mathew", "john", "jerry", "Uffe", "Sekar", "Suresh", "Ramesh", "Raja"};
        String[] secondnames = {"Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen", "Twenty"};
        String[] salary = {"10000", "20000", "15000", "5323", "2000", "5346", "1000", "4889", "7854", "2438"};
        String[] location = {"India", "Iceland", "Mexico", "Slovenia", "Poland", "Australia", "1000", "USA", "England", "Canada"};

        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("firstname", firstnames[i]);
            dataMap.put("secondname", secondnames[i]);
            dataMap.put("salary", salary[i]);
            dataMap.put("location", location[i]);
            list.add(dataMap);
        }
        String postfixExpr = postfix("( ( field.firstname = john AND field.secondname = Eleven ) OR ( field.salary = 15000 AND field.location = Mexico OR ( field.firstname = mathew AND field.secondname = Thirteen ) ) )");
        System.out.println(filter(list, postfixExpr));
    }

    public static List<Map<String, Object>> filter(List<Map<String, Object>> list, String postFixRules) {
        List<Map<String, Object>> filtered = list.stream()
                .filter(p -> checkFilter(p, postFixRules)).collect(Collectors.toList());
        return filtered;

    }

    public static Boolean checkFilter(Map<String, Object> mapData, String postFixRules) {
        // Apply condition here and return true or false
        // return (mapData.get("firstname") + "").equalsIgnoreCase("john");
        String[] postfixRulesList = postFixRules.trim().split("\\s+");
        Deque<String> stack = new LinkedList<>();
        for (int i = 0; i < postfixRulesList.length; i++) {
            String postFixToken = postfixRulesList[i];
            if (ops.containsKey(postFixToken)) {
                String operator = postFixToken;
                String operand1 = getOperand(stack.pop(), mapData);
                String operand2 = getOperand(stack.pop(), mapData);
                stack.push(calculate(operand1, operand2, operator)+"");
                
            } else {
                stack.push(postFixToken);
            }
        }
        System.out.println("OUTPUT ===> " + stack);
        return stack.pop().equalsIgnoreCase("true");
    }

    private static Boolean calculate(String operand1, String operand2, String operator) {
        if (operator.trim().equalsIgnoreCase("=")) {
            return operand1.equalsIgnoreCase(operand2);
        }
        if (operator.trim().equalsIgnoreCase("and")) {
            return operand1.equalsIgnoreCase("true") && operand2.equalsIgnoreCase("true");
        }
        if (operator.trim().equalsIgnoreCase("OR")) {
            return (operand1.equalsIgnoreCase("true") ? true : operand2.equalsIgnoreCase("true"));
        }
        return false;
    }

    private static String getOperand(String operand, Map<String, Object> mapData) {
        if (operand.startsWith("field.")) {
            String fieldName = operand.split("\\.")[1];
            return mapData.get(fieldName) + "";
        }
        return operand;
    }

}
