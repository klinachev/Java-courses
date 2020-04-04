package expression;

import exceptions.ExpressionException;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface TripleExpression<T extends Number> {
    int evaluate(T x, T y, T z);
}
