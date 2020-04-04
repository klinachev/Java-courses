package expression.expression;

import expression.exceptions.ExpressionException;

public class Variable<T extends Number> implements MyTripleExpression<T> {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public T evaluate(T x, T y, T z) throws ExpressionException {
        if (name.equals("x")) {
            return x;
        }
        if (name.equals("y")) {
            return y;
        }
        if (name.equals("z")){
            return z;
        }
        throw new ExpressionException("Unexpected variable:" + name);
    }
}
