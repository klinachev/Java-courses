package expression.expression;

public interface MyTripleExpression<T extends Number> {
    T evaluate(T x, T y, T z);
}
