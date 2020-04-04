package parser;

import exceptions.OverflowException;
import expression.*;
import exceptions.ParserException;

import java.util.*;

import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isWhitespace;
import static java.lang.Integer.parseInt;

public class ExpressionParserNorec implements Parser {

    private static Set<String> availableVariables = Set.of("x", "y", "z");

    private static Set<Character> availableOperations = Set.of('-', '+', '*', '/');

    private static final Map<String, Integer> unaryOperationNum = Map.of(
            "-", 1,
            "log2", 2,
            "pow2", 3
            //"abs", 4,
            //"square", 5
    );

    private static final Map<String, Integer> operationNum = Map.of(
            "+", 5,
            "-", 6,
            "*",7,
            "/", 8,
            "**", 9,
            "//", 10
            //">>", 4,
            //"<<", 3
    );

    private static final Map<Integer, Integer> order = Map.of(
            3, 4,
            4, 4,
            5, 6,
            6, 6,
            7, 8,
            8, 8,
            9, 10,
            10, 10,
            0, 0
    );

    private String st;

    private int it;

    public ExpressionParserNorec() {
        st = "";
        it = 0;
    }

    @Override
    public CommonExpression parse(String s) {
        st = s;
        it = 0;
        return parseString(false);
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

    private CommonExpression parseConst() {
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
            throw new OverflowException("Constant " + s + " is too large, pos: " + it);
        }
        return new Const(c);
    }

    private CommonExpression parseVariable() {
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

    private int parseOperation() {
        skipWhitespace();
        int last = it;
        if (it >= st.length()) {
            return 0;
        }
        if (st.charAt(it) == '-') {
            it++;
            return operationNum.get("-");
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
            return operationNum.get(s);
        }
        last = it;
        while (last < st.length() && !isWhitespace(st.charAt(last))) {
            last++;
        }
        s = st.substring(it, last);
        throw new ParserException("Operation expected, received: \"" + s + "\", pos: " + it);
    }

    private CommonExpression combine(CommonExpression c1, CommonExpression c2, int operation) {
        switch (operation) {
            case 5:
                return new CheckedAdd(c1, c2);
            case 6:
                return new CheckedSubtract(c1, c2);
            case 7:
                return new CheckedMultiply(c1, c2);
            case 8:
                return new CheckedDivide(c1, c2);
            case 9:
                return new CheckedPow(c1, c2);
            case 10:
                return new CheckedLog(c1, c2);
        }
        throw new ParserException("Undefined operation");
    }

    private CommonExpression unaryCombine(CommonExpression c1, int operation) {
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

    private void combineAll(int[] operation, int cnt, CommonExpression[] m) {
        for (int k = cnt; k > 0;) {
            int start = k - 1;
            while (start > 0 && (order.get(operation[start - 1]).equals(order.get(operation[start])))) {
                start--;
            }
            for (int end = start + 1; end <= k; end++) {
                m[start] = combine(m[start], m[end], operation[end - 1]);
            }
            k = start;
        }
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

    private List<Integer> parseUnaryOperation() {
        List<Integer> mas = new ArrayList<>();
        while (true) {
            if (skipWhitespace()) {
                return mas;
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
                mas.add(unaryOperationNum.get(s));
                it = last;
            } else {
                if (it >= st.length() || Character.isAlphabetic(st.charAt(it))
                        || isDigit(it) || (st.charAt(it) == '-' && it + 1 < st.length() && isDigit(it + 1)) || st.charAt(it) == '(') {
                    return mas;
                } else {
                    last = it;
                    while (st.length() > last && !isWhitespace(st.charAt(last))) {
                        last++;
                    }
                    s = st.substring(it, last);
                    throw new ParserException("UnaryOperation, number or variable expected, received: \"" + s + "\", pos: " + it);
                }
            }
        }
    }

    private CommonExpression parseString(boolean bracket) {
        int[] operation = new int[6];
        int i = 0, cnt = 0;
        CommonExpression[] m = new CommonExpression[6];
        while (it < st.length()) {
            List<Integer> unaryOperation = parseUnaryOperation();
            if (it >= st.length()) {
                if (unaryOperation.size() > 0) {
                    throw new ParserException("Number or variable was expected after the unary operation, pos: " + it);
                }
                continue;
            }
            if (isDigit(it) || ((it + 1 < st.length()) && isDigit(it + 1) && st.charAt(it) == '-')
                    || Character.isAlphabetic(st.charAt(it)) || st.charAt(it) == '(') {
                if (isDigit(it) || ((it + 1 < st.length()) && isDigit(it + 1) && st.charAt(it) == '-')) {
                    m[i] = parseConst();
                } else {
                    if (it < st.length() && Character.isAlphabetic(st.charAt(it))) {
                        m[i] = parseVariable();
                    } else {
                        if (it < st.length() && st.charAt(it) == '(') {
                            it++;
                            m[i] = parseString(true);
                        }
                    }
                }
                for (int j = unaryOperation.size() - 1; j >= 0; j--) {
                    m[i] = unaryCombine(m[i], unaryOperation.get(j));
                }
                i++;

                int j = 0;
                while (j + 1 < cnt) {
                    j = 0;
                    while (j + 1 < cnt && (order.get(operation[j]) < order.get(operation[j + 1]))) {
                        j++;
                    }
                    if (j + 1 < cnt) {
                        m[j] = combine(m[j], m[j + 1], operation[j]);
                        for (int k = j; k < cnt; k++) {
                            operation[k] = operation[k + 1];
                            m[k + 1] = m[k + 2];
                        }
                        cnt--;
                        operation[cnt] = 0;
                        i--;
                        m[i] = null;
                        j--;
                    }
                }
                skipWhitespace();
                if (it < st.length() && st.charAt(it) == ')') {
                    if (!bracket) {
                        throw new ParserException("unexpected \")\", pos: " + it);
                    }
                    combineAll(operation, cnt, m);
                    it++;
                    return m[0];
                }
                if (it >= st.length()) {

                    if (bracket) {
                        throw new ParserException("expected \")\", pos: " + it);
                    }
                    combineAll(operation, cnt, m);
                    return m[0];
                }
                operation[cnt++] = parseOperation();
                skipWhitespace();
                continue;
            }
            if (unaryOperation.size() > 0) {
                throw new ParserException("Number or variable was expected after the unary operation");
            }
            if (st.charAt(it) == ')') {
                if (!bracket) {
                    throw new ParserException("unexpected \")\", pos: " + it);
                }
                if (operation[0] > 0) {
                    m[0] = combine(m[0], m[1], operation[0]);
                }
                return m[0];
            }
            operation[cnt++] = parseOperation();
            skipWhitespace();
        }

        if (cnt > 0 && operation[cnt - 1] != 0) {
            throw new ParserException("Number or variable was expected at the end of the expression");
        }
        if (bracket) {
            throw new ParserException("expected \")\", pos: " + it);
        }
        combineAll(operation, cnt, m);
        return m[0];
    }
}
