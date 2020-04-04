package expression;

import static java.util.Objects.hash;

public class Const implements CommonExpression {
    private Number val;

    public Const(int val) {
        this.val = val;
    }

    public Const(double val) {
        this.val = val;
    }

    public int rank() {
        return -1;
    }

    @Override
    public boolean equals(Object comp) {
        if (comp == null || getClass() != comp.getClass()) {
            return false;
        }
        return (this.val).equals(((Const)comp).val);
    }

    @Override
    public String toMiniString() {
        return val.toString();
    }

    @Override
    public String toMiniString(boolean f) {
        return val.toString();
    }

    @Override
    public String toString() {
        return val.toString();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return val.intValue();
    }

    @Override
    public int hashCode() {
        return hash(val);
    }


}
