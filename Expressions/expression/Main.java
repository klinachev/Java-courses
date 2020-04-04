package expression;

import parser.ExpressionParser;

import java.util.Scanner;

public class Main  {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //String s = scanner.nextLine();

        ExpressionParser p = new ExpressionParser();
        TripleExpression ad = p.parse("(2+2)");
        System.out.println(ad.toString());
        System.out.println(ad.evaluate(569022217, -516482536, -244927408));

        //-1983039341
        /*
        String[] a = new String[]{"hard"};
        new expression.ExpressionTest(expression.ExpressionTest.mode(a)).run();
        new expression.DoubleExpressionTest(expression.DoubleExpressionTest.mode(a)).run();
        new expression.TripleExpressionTest(expression.TripleExpressionTest.mode(a)).run();

         */

    }

}
