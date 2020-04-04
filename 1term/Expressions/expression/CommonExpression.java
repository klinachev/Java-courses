package expression;

import exceptions.ArgumentException;
import exceptions.ExpressionException;

public interface CommonExpression extends TripleExpression {
    boolean equals(Object comp);
    String toMiniString();
    String toMiniString(boolean f);
    String toString();
    int evaluate(int x, int y, int z);
    int rank();
}
