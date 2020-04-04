package expression;

import exceptions.ExpressionException;

import static java.util.Objects.hash;

public class Variable implements CommonExpression {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    public int rank() {
        return -1;
    }

    @Override
    public boolean equals(Object comp) {
        if (comp == null || getClass() != comp.getClass()) {
            return false;
        }
        return (this.toString()).equals(comp.toString());
    }

    @Override
    public String toMiniString() {
        return name;
    }

    @Override
    public String toMiniString(boolean f) {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return 307 * hash(name);
    }

    @Override
    public int evaluate(int x, int y, int z) throws ExpressionException {
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
