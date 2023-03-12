package com.cezarylgt.java.criteriafacade;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ComparisonOperatorTest {

    @Test
    public void testValueOfLabel_shouldReturnDesiredClass() throws UnrecognizedComparisionOperator {
        assertEquals(OperatorResolvers.EqualOperatorResolver.class, ComparisonOperator.valueOfLabel("=").create().getClass());
        assertEquals(OperatorResolvers.NotEqualOperatorResolver.class, ComparisonOperator.valueOfLabel("!=").create().getClass());
        assertEquals(OperatorResolvers.ContainsOperatorResolver.class, ComparisonOperator.valueOfLabel("~").create().getClass());
        assertEquals(OperatorResolvers.InOperatorResolver.class, ComparisonOperator.valueOfLabel("|").create().getClass());
        assertEquals(OperatorResolvers.GreaterEqualOperatorResolver.class, ComparisonOperator.valueOfLabel(">=").create().getClass());
        assertEquals(OperatorResolvers.GreaterOperatorResolver.class, ComparisonOperator.valueOfLabel(">").create().getClass());

        assertEquals(OperatorResolvers.LessEqualOperatorResolver.class, ComparisonOperator.valueOfLabel("<=").create().getClass());
        assertEquals(OperatorResolvers.LessOperatorResolver.class, ComparisonOperator.valueOfLabel("<").create().getClass());


    }

    @Test
    public void testValueOfLabel_shouldThrowException_whenInvalidOperator() {
        UnrecognizedComparisionOperator thrown = assertThrows(UnrecognizedComparisionOperator.class, () -> {
            ComparisonOperator.valueOfLabel("InvalidLabel").create();
        });

    }

}