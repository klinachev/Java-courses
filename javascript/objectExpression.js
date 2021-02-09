"use strict";

function AbstractFactory(create, evaluate, diff, toString, prefix, postfix) {
    function Create() {
        create.call(this, arguments);
        return this;
    }
    Create.prototype.constructor = Create;
    Create.prototype.evaluate = evaluate;
    Create.prototype.diff = diff;
    Create.prototype.toString = toString;
    Create.prototype.prefix = prefix;
    Create.prototype.postfix = postfix;
    return Create;
}

function ArgumentFactory(create, evaluate, diff, toString) {
    return AbstractFactory(create, evaluate, diff, toString, toString, toString);
}

const Const = ArgumentFactory(
    function(arg) {this.val = arg[0]},
    function() {return Number(this.val);},
    function() {return zero;},
    function() {return this.val.toString();}
);

const one = new Const(1);
const zero = new Const(0);
const e = new Const(Math.E);

const availableVariables = {
    "x": 0,
    "y": 1,
    "z": 2
};

const Variable = ArgumentFactory(
    function(arg) {this.name = arg[0]},
    function () {return (arguments[availableVariables[this.name]])},
    function (diffVar) {
        if (diffVar === this.name) {
            return one;
        } else {
            return zero;
        }
    },
    function() {return this.name.toString();}
);

const variables = [new Variable("x"), new Variable("y"), new Variable("z")];

function toPrint(args, f, start, end) {
    return start + args.map(f).join(' ') + end;
}

const Operation = AbstractFactory(
    function (args) {
        this.args = Array.from(args);
    },
    function(...args) {
        let newArgs = [];
        this.args.forEach((argument) => {
            newArgs.push(argument.evaluate(...args));
        });
        return this.calc(...newArgs);
    },
    function(diffVariable) {
        let newArgs = [];
        this.args.forEach((argument) => {
            newArgs.push(argument);
            newArgs.push(argument.diff(diffVariable));
        });
        return this.diffCalc(...newArgs);
    },
    function() {
        return toPrint(this.args, elem => elem.toString(), this.toStr, '');
    },
    function() {
        return toPrint(this.args, elem => elem.prefix(), '(' + this.toStr + ' ', ')');
    },
    function() {
        return toPrint(this.args, elem => elem.postfix(), '(', ' ' + this.toStr + ')');
    }
);

const availableOperation = {};

function OperationFactory(toStr, calc, diffCalc) {
    function Constructor() {
        Operation.apply(this, arguments);
        return this;
    }
    Constructor.prototype = Object.create(Operation.prototype);
    Constructor.prototype.constructor = Constructor;
    Constructor.prototype.calc = calc;
    Constructor.prototype.toStr = toStr;
    Constructor.prototype.diffCalc = diffCalc;
    availableOperation[toStr] = Constructor;
    return Constructor;
}

const Add = OperationFactory(
    '+',
    (a, b) => a + b,
    (a, da, b, db) => new Add(da, db)
);

const Subtract = OperationFactory(
    '-',
    (a, b) => a - b,
    (a, da, b, db) => new Subtract(da, db)
);

const Multiply = OperationFactory(
    '*',
    (a, b) => a * b,
    (a, da, b, db) => new Add(new Multiply(a, db), new Multiply(b, da))
);

const Divide = OperationFactory(
    '/',
    (a, b) => a / b,
    (a, da, b, db) => new Divide(Multiply.prototype.diffCalc(a, da, b, new Negate(db)), new Multiply(b, b))
);

const Negate = OperationFactory(
    'negate',
    (a) => -a,
    (a, da) => new Negate(da)
);

const Log = OperationFactory(
    'log',
    (a, b) => Math.log(Math.abs(b)) / Math.log(Math.abs(a)),
    (a, da, b, db) => Divide.prototype.diffCalc(new Log(e, b), new Divide(db, b), new Log(e, a), new Divide(da, a))
);

const Power = OperationFactory(
    'pow',
    (a, b) => Math.pow(a, b),
    (a, da, b, db) => new Multiply(new Power(a, b), Multiply.prototype.diffCalc(new Log(e, a), new Divide(da, a), b, db))
);

const Sumexp = OperationFactory(
    'sumexp',
    (...m) => {
        let sum = 0;
        m.forEach( argument => {
            sum += Math.pow(e, argument);
        });
        return sum;
    },
    (...args) => {
        if (args.length === 0) {
            return zero;
        }
        if (args.length === 2) {
            let da = args.pop(), a = args.pop();
            return Power.prototype.diffCalc(e, zero, a, da);
        }
        let da = args.pop(), a = args.pop();
        return new Add(Power.prototype.diffCalc(e, zero, a, da), Sumexp.prototype.diffCalc(...args));
    }
);

const Softmax = OperationFactory(
    'softmax',
    (...m) => {
        return (Math.pow(e, m[0]) / Sumexp.prototype.calc(...m));
    },
    (...args) => {
        if (args.length === 0) {
            return zero;
        }
        let first = [];
        for (let i = 0; i < args.length; i += 2) {
            first.push(args[i]);
        }
        return Divide.prototype.diffCalc(new Power(e, args[0]), Power.prototype.diffCalc(e, zero, args[0], args[1]),
                                        new Sumexp(...first), Sumexp.prototype.diffCalc(...args));
    }
);

function ParseErrorFactory(name) {
    function ParseError(message, pos) {
        this.message = message + ", pos: " + pos;
    }
    ParseError.prototype = Object.create(Error.prototype);
    ParseError.prototype.constructor = ParseError;
    ParseError.prototype.name = name;
    return ParseError;
}

const ParseError = ParseErrorFactory("ParseError");

function parsePrefix(expression) {
    return expressionParser(expression, arg => arg, pos => pos, "(", ")");
}

function parsePostfix(expression) {
    return expressionParser(expression, arg => arg.reverse(), pos => expression.length - 1 - pos, ")", "(");
}

function expressionParser(expression, argFunc, position, bracket1, bracket2) {
    let pos = 0;
    if (expression.length === 0) {
        throw new ParseError("Empty expression", 0);
    }
    let ans = takeArgument();
    if (pos < expression.length) {
        throw new ParseError("Unused arguments in expression", position(pos - 1));
    }
    return ans;
    function takeArgument() {
        skipWhitespace();
        if (expression[position(pos)] === bracket1) return takeOperation();
        let token = takeToken();
        if (token in availableVariables) {
            skipWhitespace();
            return variables[availableVariables[token]];
        } else if (!isNaN(token)) {
            skipWhitespace();
            return new Const(token);
        }
        throw new ParseError("Variavle, number or " + bracket1 + " expected, taken: " + token, position(pos - 1));
    }
    function takeOperation() {
        pos++;
        let token = takeToken();
        if (!(token in availableOperation)) throw new ParseError("Operation expected, taken: " + token , position(pos - 1));
        skipWhitespace();
        let stack = [];
        while (pos < expression.length && expression[position(pos)] !== bracket2) {
            stack.push(takeArgument());
        }
        if (pos >= expression.length) throw new ParseError(bracket2 + " expected after operation " + token, position(pos - 1));
        pos++;
        skipWhitespace();
        if (availableOperation[token].prototype.calc.length === 0
            || stack.length === availableOperation[token].prototype.calc.length) {
            return new availableOperation[token](...argFunc(stack));
        }
        throw new ParseError(token + " must have " + availableOperation[token].prototype.calc.length
            + " arguments, taken " + stack.length, position(pos - 1));
    }
    function skipWhitespace() {
        while (pos < expression.length && expression[position(pos)] === ' ') pos++;
    }
    function takeToken() {
        skipWhitespace();
        let start = pos, ans;
        while (pos < expression.length && expression[position(pos)] !== ' '
                && expression[position(pos)] !== '(' && expression[position(pos)] !== ')') pos++;
        if (position(start) > position(pos)) {
            ans = expression.substring(position(start) + 1, position(pos) + 1);
        } else {
            ans = expression.substring(position(start), position(pos));
        }
        return ans;
    }
}
