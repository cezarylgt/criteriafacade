package com.cezarylgt.java.criteriafacade;

import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
class UnrecognizedComparisionOperator extends Exception {
    public UnrecognizedComparisionOperator(String value) {super(value);}
}

public enum ComparisonOperator {

    EQUAL("=") {
        @Override
        public IOperatorResolver create() {return new OperatorResolvers.EqualOperatorResolver();}
    },
    NOT_EQUAL("!=") {
        @Override
        public IOperatorResolver create() {return new OperatorResolvers.NotEqualOperatorResolver();}
    },
    IN("|") {
        @Override
        public IOperatorResolver create() {return new OperatorResolvers.InOperatorResolver();}
    },
    CONTAINS("~") {
        @Override
        public IOperatorResolver create() {return new OperatorResolvers.ContainsOperatorResolver();}
    },
    GREATER_EQUAL(">=") {
        @Override
        public IOperatorResolver create() {return new OperatorResolvers.GreaterEqualOperatorResolver();}
    },
    LESS_EQUAL("<=") {
        @Override
        public IOperatorResolver create() {return new OperatorResolvers.LessEqualOperatorResolver();}
    },
    GREATER(">") {
        @Override
        public IOperatorResolver create() {return new OperatorResolvers.GreaterOperatorResolver();}
    },
    LESS("<") {
        @Override
        public IOperatorResolver create() {return new OperatorResolvers.LessOperatorResolver();}
    }
    ;
    public final  String label;

    private static final Map<String, ComparisonOperator> BY_LABEL = new HashMap<>();

    static {
        for (ComparisonOperator e: values()) {
            BY_LABEL.put(e.label, e);
        }
    }

    public static ComparisonOperator valueOfLabel(String label) throws UnrecognizedComparisionOperator {
        if (BY_LABEL.get(label) != null)
            return BY_LABEL.get(label);
        throw new UnrecognizedComparisionOperator(String.format("Operator: %s is not valid operator", label));
    }

    ComparisonOperator(String label) {
        this.label = label;

    }

    public abstract IOperatorResolver create();



}
