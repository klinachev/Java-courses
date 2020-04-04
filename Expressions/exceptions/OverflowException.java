package exceptions;

public class OverflowException extends ExpressionException {
    public OverflowException(String arg) {
        super("Overflow: " + arg);
    }
}
