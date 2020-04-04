package expression;

import exceptions.ExpressionException;

import static java.util.Objects.hash;

public abstract class BinaryFunction implements CommonExpression {
    protected CommonExpression first, second;

    protected BinaryFunction() {
        first = null;
        second = null;
    }

    @Override
    public String toMiniString() {
        return toMiniString(false);
    }

    public String toMiniString(boolean need) {
        StringBuilder sb = new StringBuilder();
        if (need) {
            sb.append('(');
        }
        if (this.rank() > 2 && first.rank() < 3) {
            sb.append(first.toMiniString(true));
        } else {
            sb.append(first.toMiniString(false));
        }
        sb.append(' ').append(symbol()).append(' ');
        if (rank() == 4 || (rank() == 3 && second.rank() == 4) || (rank() != 1 && second.rank() < 3)) {
            sb.append(second.toMiniString(true));
        } else {
            sb.append(second.toMiniString(false));
        }
        if (need) {
            sb.append(')');
        }
        return sb.toString();
    }


    @Override
    public String toString() {
        return '(' + first.toString() + ' ' + symbol() + ' ' + second.toString() + ')';
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return calc(first.evaluate(x, y, z), second.evaluate(x, y, z));
    }

    @Override
    public boolean equals(Object comp) {
        if (comp == null || getClass() != comp.getClass()) {
            return false;
        }
        BinaryFunction func = (BinaryFunction) comp;
        return (first).equals(func.first) && (second).equals(func.second);
    }

    @Override
    public int hashCode() {
        return 4000 * first.hashCode() + 80 * second.hashCode() + hash(symbol());
    }

    public abstract int rank();
    protected abstract String symbol();
    protected abstract int calc(int x, int y);
}
