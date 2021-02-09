"use strict";
const availableVariables = {
    "x": 0,
    "y": 1,
    "z": 2
};

const availableConstant = {
    "pi" : Math.PI,
    "e" : Math.E,
};


const cnst = val => () => {
    return val;
};

const variable = varName => (...args) => {
    return args[availableVariables[varName]];
};

const Operation = f => (...args) => (...variables) => {
    let argumentsArray = [];
    for (let i = 0; i < args.length; i++) {
        argumentsArray.push(args[i](...variables))
    }
    return f(...argumentsArray);
};

const add = Operation((a, b) => { return a + b });
const subtract = Operation((a, b) => { return a - b });
const multiply = Operation((a, b) => { return a * b });
const divide = Operation((a, b) => { return a / b });
const negate = Operation((a) => { return -a });
const avg5 = Operation((...args) => {
    let sum = 0;
    for (let i = 0; i < 5; i++) {
        sum += args[i];
    }
    return sum / 5;
});
const med3 = Operation( (a, b, c) => {
    if (a >= b && b >= c || a <= b && b <= c) {
        return b;
    }
    if (b >= a && a >= c || b <= a && a <= c) {
        return a;
    }
    return c;
});


const pi = cnst(Math.PI);
const e = cnst(Math.E);

const availableOperation = {
    "+" : add,
    "-" : subtract,
    "*" : multiply,
    "/" : divide,
    "negate": negate,
    "avg5" : avg5,
    "med3" : med3
};

const argumentsCount = {
    "+" : 2,
    "-" : 2,
    "*" : 2,
    "/" : 2,
    "negate": 1,
    "avg5" : 5,
    "med3" : 3
};

function parse(expression) {
    let stack = [];
    (expression.split(" ").filter(s => s.length > 0)).forEach((element) => {
        if (element in availableOperation) {
            let args = [];
            for (let i = 0; i < argumentsCount[element]; i++) {
                args.push(stack.pop());
            }
            args.reverse();
            stack.push(availableOperation[element](...args));
        } else if (element in availableVariables) {
            stack.push(variable(element));
        } else if (element in availableConstant) {
            stack.push(cnst(availableConstant[element]));
        } else {
            stack.push(cnst(Number(element)));
        }
    });
    return stack.pop();
}
