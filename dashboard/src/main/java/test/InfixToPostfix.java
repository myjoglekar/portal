/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

/**
 *
 * @author user
 */
public class InfixToPostfix {

    private List<String> opStack = new ArrayList<>();
    private List<String> output = new ArrayList<>();
    private List<String> infixStack = new ArrayList<>();

    public static void infixToPostFix(String filterStr) {

    }

    void populateInfixStack(String filterStr) {
        // String filterStr = "((firstname = john AND Lastname = Eleven) OR (salary = 15000 AND location = Mexico OR (firstname = mathew AND lastname = Thirteen)))";
        //filterStr = filterStr.replaceAll("AND", ")AND(").replaceAll("OR", ")OR(");
        //filterStr = "(" + filterStr + ")";
        System.out.println(filterStr);
        String spl[] = filterStr.replaceAll("([\\(\\)])", " $1 ").trim().split("\\s+");
        infixStack = Arrays.asList(spl);
        for (Iterator<String> iterator = infixStack.iterator(); iterator.hasNext();) {
            String infix = iterator.next();
            Boolean isOperator = isOperator(infix);
            if (isOperator) {
                opStack.add(infix);
            } else if (infix.equalsIgnoreCase("(")) {
                opStack.add(infix);
            } else if (infix.equalsIgnoreCase(")")) {
                while (true) {
                    String popOp = opStack.remove(opStack.size() - 1);
                    if (popOp.equalsIgnoreCase("(")) {
                        break;
                    } else {
                    }
                    output.add(popOp);
                }
            } else {
                output.add(infix);
            }
        }
        while (true) {
            if (opStack.isEmpty()) {
                break;
            }
            String popOp = opStack.remove(opStack.size() - 1);
            output.add(popOp);
        }
        System.out.println(output);

    }

    public static Boolean isOperator(String operator) {
        if (operator.equalsIgnoreCase("=") || operator.equalsIgnoreCase("and") || operator.equalsIgnoreCase("or")) {
            return true;
        }
        return false;
    }

    public static void main(String argv[]) {
        String filterStr = "((firstname = john AND Lastname = Eleven) OR (salary = 15000 AND location = Mexico OR (firstname = mathew AND lastname = Thirteen)))";
        InfixToPostfix i = new InfixToPostfix();
        i.populateInfixStack(filterStr);
    }
}
