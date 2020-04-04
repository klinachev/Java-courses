package expression.parser;

import expression.exceptions.ParserException;
import expression.expression.*;
import expression.types.typeMath;

import java.util.*;

import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isWhitespace;

public class MyExpressionParser<T extends Number> {
    protected typeMath<T> math;

    private static Set<String> availableVariables = Set.of("x", "y", "z");

    private static Set<Character> availableOperations = Set.of('-', '+', '*', '/');

    private static final Map<String, Integer> unaryOperationNum = Map.of(
            "-", 1,
            "log2", 2,
            "pow2", 3,
            "count", 4
    );

    private static final Map<String, Integer> operationNum = Map.of(
            "+", 5,
            "-", 5,
            "*",7,
            "/", 7,
            "**", 9,
            "//", 9,
            "min", 1,
            "max", 1
    );

    private String st;

    private int it, bracketCount;

    private String currentOperation;

    private boolean haveOperation;

    public MyExpressionParser(typeMath<T> mode) {
        st = "";
        it = 0;
        math = mode;
    }

    public MyTripleExpression<T> parse(String s) throws ParserException {
        st = s;
        it = 0;
        bracketCount = 0;
        haveOperation = false;
        MyTripleExpression<T> cop = parseString(false, 0);
        if (it < st.length()) {
            throw new ParserException("Unexpected \")\", pos: " + it);
        }
        return cop;
    }

    private boolean skipWhitespace() {
        while (it < st.length() && isWhitespace(st.charAt(it))){
            it++;
        }
        return it >= st.length();
    }

    private boolean isDigit(int it) {
        return ((st.charAt(it) <= '9' && st.charAt(it) >= '0') || st.charAt(it) == '.');
    }

    private MyTripleExpression<T> parseConst() throws ParserException {
        if (skipWhitespace()) {
            throw new ParserException("Constant was expected at the end of the expressions.expressions.generic.expression");
        }
        int start = it;
        if (st.charAt(it) == '-') {
            it++;
        }
        while (it < st.length() && isDigit(it)) {
            it++;
        }
        T c;
        String s = st.substring(start, it);
        try {
            c = math.parseString(s);
        } catch (RuntimeException e) {
            throw new ParserException("Constant " + s + " is too large, pos: " + it);
        }
        return new Const<>(c);
    }

    private MyTripleExpression<T> parseVariable() throws ParserException {
        if (skipWhitespace()) {
            throw new ParserException("Variable was expected at the end of the expressions.expressions.generic.expression");
        }
        int start = it;
        while (it < st.length() && (Character.isAlphabetic(st.charAt(it)) || isDigit(it))) {
            it++;
        }
        String a = st.substring(start, it);
        if (!availableVariables.contains(a)) {
            throw new ParserException("Unavailable variable: \"" + a + "\", pos: " + it);
        }
        return new Variable<>(a);
    }

    private String parseOperation() throws ParserException {
        skipWhitespace();
        int last = it;
        if (st.charAt(it) == '-') {
            it++;
            return "-";
        }
        if (availableOperations.contains(st.charAt(last))) {
            last++;
            while (last < st.length() && st.charAt(last) == st.charAt(last - 1)) {
                last++;
            }
        }
        if (st.charAt(it) == 'm') {
            if (expectString("ax", it)) {
                last = it + 3;
            }
            if (expectString("in", it)) {
                last = it + 3;
            }
        }
        String s = st.substring(it, last);
        if (operationNum.containsKey(s)) {
            it = last;
            return s;
        }
        last = it;
        while (last < st.length() && !isWhitespace(st.charAt(last))) {
            last++;
        }
        s = st.substring(it, last);
        throw new ParserException("Operation expected, received: \"" + s + "\", pos: " + it);
    }

    private MyTripleExpression<T> combine(MyTripleExpression<T> c1, String s) throws ParserException {
        int oper = operationNum.get(s);
        switch (s) {
            case "+":
                return new Add<>(c1, parseString(false, oper), math);
            case "-":
                return new Subtract<>(c1, parseString(false, oper), math);
            case "*":
                return new Multiply<>(c1, parseString(false, oper), math);
            case "/":
                return new Divide<>(c1, parseString(false, oper), math);
            case "min":
                return new Min<>(c1, parseString(false, oper), math);
            case "max":
                return new Max<>(c1, parseString(false, oper), math);
        }
        throw new ParserException("Undefined operation");
    }

    private MyTripleExpression<T> unaryCombine(MyTripleExpression<T> c1, int operation) throws ParserException {
        switch (operation) {
            case 1:
                return new Negate<>(c1, math);
            case 4:
                return new Count<>(c1, math);
        }
        throw new ParserException("Undefined unaryoperation");
    }

    private boolean expectString(String s, int it) {
        if (s.length() + it > st.length()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != st.charAt(it + i + 1)) {
                return false;
            }
        }
        if (s.length() + it + 1 < st.length() &&
                (isDigit(s.length() + it + 1) || isAlphabetic(st.charAt(s.length() + it + 1)))) {
            return false;
        }
        return true;
    }

    private MyTripleExpression<T> parseUnaryOperation() throws ParserException {
        // System.out.println(it);
        if (skipWhitespace()) {
            throw new ParserException("Number or variable expected at the end of the expressions.expressions.generic.expression");
        }
        int last = it;
        if (st.charAt(it) == '-' && it + 1 < st.length() && !isDigit(it + 1)) {
            last++;
        } else {
            if (st.charAt(it) == 'p') {
                if (expectString("ow2", it)) {
                    last = it + 4;
                }
            } else if (st.charAt(it) == 'l') {
                if (expectString("og2", it)) {
                    last = it + 4;
                }
            } else if (st.charAt(it) == 'c') {
                if (expectString("ount", it)) {
                    last = it + 5;
                }
            }
        }
        String s = st.substring(it, last);
        if (unaryOperationNum.containsKey(s)) {
            it = last;
            return unaryCombine(parseUnaryOperation(), unaryOperationNum.get(s));
        } else {
            if (Character.isAlphabetic(st.charAt(it))) {
                return parseVariable();
            }
            if (isDigit(it) || (st.charAt(it) == '-' && it + 1 < st.length() && isDigit(it + 1))) {
                return parseConst();
            }
            if (st.charAt(it) == '(') {
                bracketCount++;
                it++;
                return parseString(true, 0);
            }
            while(last < st.length() && !isWhitespace(st.charAt(last))) {
                last++;
            }
            throw new ParserException("Number or variable expected, received: \""
                    + st.substring(it, last) + "\", pos: " + it);
        }
    }

    private MyTripleExpression<T> parseString(boolean bracket, int priority) throws ParserException {
        MyTripleExpression<T> left = parseUnaryOperation();
        skipWhitespace();
        while (it < st.length() && (st.charAt(it) != ')')) {
            if (!haveOperation) {
                currentOperation = parseOperation();
                haveOperation = true;
            }
            if (operationNum.get(currentOperation) > priority) {
                haveOperation = false;
                left = combine(left, currentOperation);
            } else {
                return left;
            }
            skipWhitespace();
        }
        if (it >= st.length() && bracket) {
            throw new ParserException("Expected \")\" at the end of the expressions.expressions.generic.expression");
        }
        if (haveOperation) {
            throw new ParserException("Unexpected \")\" after operation, pos: " + it);
        }
        if (it < st.length() && st.charAt(it) == ')' && bracket) {
            bracketCount--;
            if (bracketCount < 0) {
                throw new ParserException("Unexpected \")\", pos: " + it);
            }
            it++;
        }
        return left;
    }

}
