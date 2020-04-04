package expression.generic;

import expression.exceptions.ParserException;
import expression.expression.MyTripleExpression;
import expression.parser.MyExpressionParser;
import expression.types.*;

import java.util.Map;

public class GenericTabulator implements Tabulator {
    private Map<String, typeMath<? extends Number>> type = Map.of(
            "i", new IntMath(),
            "d", new DoubleMath(),
            "bi", new BigIntegerMath(),
            "u", new UncheckedIntMath(),
            "s", new ShortMath(),
            "l", new LongMath()
    );

    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        return makeTable(type.get(mode), expression, x1, x2, y1, y2, z1, z2);
    }

    private <T extends Number> Object[][][] makeTable(typeMath<T> math, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        MyExpressionParser<T> parser = new MyExpressionParser<>(math);
        MyTripleExpression<T> expr;
        try {
            expr = parser.parse(expression);
        } catch (ParserException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't parse expressions.expressions.generic.expression");
        }
        Object[][][] table = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                for (int k = z1; k <= z2; k++) {
                    try {
                        table[i - x1][j - y1][k - z1] = expr.evaluate(math.parseString(Integer.toString(i)),
                                math.parseString(Integer.toString(j)), math.parseString(Integer.toString(k)));
                    } catch (RuntimeException e) {
                        table[i - x1][j - y1][k - z1] = null;
                    }
                }
            }
        }
        return table;
    }
}
