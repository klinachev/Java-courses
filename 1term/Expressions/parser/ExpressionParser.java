package parser;

import expression.*;
import exceptions.ParserException;

import java.util.*;

import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isWhitespace;
import static java.lang.Integer.parseInt;

public class ExpressionParser implements Parser {

    private static Set<String> availableVariables = Set.of("x", "y", "z");

    private static Set<Character> availableOperations = Set.of('-', '+', '*', '/');

    private static final Map<String, Integer> unaryOperationNum = Map.of(
            "-", 1,
            "log2", 2,
            "pow2", 3
    );

    private static final Map<String, Integer> operationNum = Map.of(
            "+", 5,
            "-", 5,
            "*",7,
            "/", 7,
            "**", 9,
            "//", 9
    );

    private String st;

    private int it, bracketCount;

    private String currentOperation;

    private boolean haveOperation;

    public ExpressionParser() {
        st = "";
        it = 0;
    }

    @Override
    public CommonExpression parse(String s) throws ParserException {
        st = s;
        it = 0;
        bracketCount = 0;
        haveOperation = false;
        CommonExpression cop = parseString(false, 0);
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
        return (st.charAt(it) <= '9' && st.charAt(it) >= '0');
    }

    private CommonExpression parseConst() throws ParserException {
        if (skipWhitespace()) {
            throw new ParserException("Constant was expected at the end of the expression");
        }
        int start = it;
        if (st.charAt(it) == '-') {
            it++;
        }
        while (it < st.length() && isDigit(it)) {
            it++;
        }
        int c;
        String s = st.substring(start, it);
        try {
            c = parseInt(s);
        } catch (RuntimeException e) {
            throw new ParserException("Constant " + s + " is too large, pos: " + it);
        }
        return new Const(c);
    }

    private CommonExpression parseVariable() throws ParserException {
        if (skipWhitespace()) {
            throw new ParserException("Variable was expected at the end of the expression");
        }
        int start = it;
        while (it < st.length() && (Character.isAlphabetic(st.charAt(it)) || isDigit(it))) {
            it++;
        }
        String a = st.substring(start, it);
        if (!availableVariables.contains(a)) {
            throw new ParserException("Unavailable variable: \"" + a + "\", pos: " + it);
        }
        return new Variable(a);
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

    private CommonExpression combine(CommonExpression c1, String s) throws ParserException {
        int oper = operationNum.get(s);
        switch (s) {
            case "+":
                return new CheckedAdd(c1, parseString(false, oper));
            case "-":
                return new CheckedSubtract(c1, parseString(false, oper));
            case "*":
                return new CheckedMultiply(c1, parseString(false, oper));
            case "/":
                return new CheckedDivide(c1, parseString(false, oper));
            case "**":
                return new CheckedPow(c1, parseString(false, oper));
            case "//":
                return new CheckedLog(c1, parseString(false, oper));
        }
        throw new ParserException("Undefined operation");
    }

    private CommonExpression unaryCombine(CommonExpression c1, int operation) throws ParserException {
        switch (operation) {
            case 1:
                return new CheckedNegate(c1);
            case 2:
                return new CheckedLog2(c1);
            case 3:
                return new CheckedPow2(c1);
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

    private CommonExpression parseUnaryOperation() throws ParserException {
        // System.out.println(it);
        if (skipWhitespace()) {
            throw new ParserException("Number or variable expected at the end of the expression");
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

    private CommonExpression parseString(boolean bracket, int priority) throws ParserException {
        CommonExpression left = parseUnaryOperation();
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
            throw new ParserException("Expected \")\" at the end of the expression");
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
