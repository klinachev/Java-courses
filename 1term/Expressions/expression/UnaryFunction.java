package expression;

import exceptions.ArgumentException;
import exceptions.ExpressionException;

import static java.util.Objects.hash;

public abstract class UnaryFunction implements CommonExpression {
    protected CommonExpression first;

    public UnaryFunction() {
        first = null;
    }

    @Override
    public String toMiniString() {
        return null;
    }

    @Override
    public String toMiniString(boolean f) {
        return null;
    }

    @Override
    public String toString() {
        return "(" + symbol() + first.toString() + ')';
    }

    @Override
    public int evaluate(int x, int y, int z){
        return calc(first.evaluate(x, y, z));
    }

    @Override
    public boolean equals(Object comp) {
        return false;
    }

    @Override
    public int rank() {
        return 1;
    }

    @Override
    public int hashCode() {
        return 1907 * first.hashCode() + hash(symbol());
    }
    protected abstract String symbol();
    protected abstract int calc(int x);
}
