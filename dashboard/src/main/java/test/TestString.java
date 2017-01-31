/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.google.common.base.CaseFormat;

/**
 *
 * @author user
 */
public class TestString {
    public static void main(String argv[]) {
        System.out.println(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "THIS_IS_AN_EXAMPLE_STRING"));
    }
}
