package com.cezarylgt.java.criteriafacade;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class OperatorResolvers {
    /**
     * Returns all values exactly matching passed value
     */
    public static class EqualOperatorResolver implements IOperatorResolver {

        @Override
        public Predicate resolve(Path<?> key, Object value, CriteriaBuilder builder) {
            return builder.equal(key, value);
        }
    }

    /**
     * Returns all values not equal to passed value
     */
    public static class NotEqualOperatorResolver implements IOperatorResolver {

        @Override
        public Predicate resolve(Path<?> key, Object value, CriteriaBuilder builder) {
            return builder.notEqual(key, value);
        }
    }

    /**
     * Use this when expected return values are Strings and values can contain passed value, i.g.:
     * passed value is equal to 'bar'; query returns values containing 'bar' -> 'foobar', 'bar'.
     * Uses equality operator when queried field is not of type String.
     */

    public static class ContainsOperatorResolver implements IOperatorResolver {

        @Override
        public Predicate resolve(Path<?> key, Object value, CriteriaBuilder builder) {
            if (key.getJavaType() == String.class)
                return builder.like((Expression<String>) key, "%" + value + "%");
            return builder.equal(key, value);
        }
    }

    /**
     * Use this when passed value represents collection of expected values, just like with SQL IN clause
     */
    public static class InOperatorResolver implements IOperatorResolver {

        @Override
        public Predicate resolve(Path<?> key, Object value, CriteriaBuilder builder) {
            if (value instanceof List)
                return key.in(((List<?>) value).toArray());
            return key.in(((String) value).split(","));
        }
    }

    public static class GreaterEqualOperatorResolver implements IOperatorResolver {

        @Override
        public Predicate resolve(Path<?> key, Object value, CriteriaBuilder builder) {
            if (key.getJavaType() == LocalDateTime.class)
                return builder.greaterThanOrEqualTo((Expression<LocalDateTime>) key, (LocalDateTime) value );
            if (key.getJavaType() == LocalDate.class)
                return builder.greaterThanOrEqualTo((Expression<LocalDate>) key, (LocalDate) value );

            return builder.greaterThanOrEqualTo((Expression<String>) key, value.toString() );

        }
    }

    public static class LessEqualOperatorResolver implements IOperatorResolver {

        @Override
        public Predicate resolve(Path<?> key, Object value, CriteriaBuilder builder) {
            if (key.getJavaType() == LocalDateTime.class)
                return builder.lessThanOrEqualTo((Expression<LocalDateTime>) key, (LocalDateTime) value );
            if (key.getJavaType() == LocalDate.class)
                return builder.lessThanOrEqualTo((Expression<LocalDate>) key, (LocalDate) value );

            return builder.lessThanOrEqualTo((Expression<String>) key, value.toString() );

        }
    }

    public static class GreaterOperatorResolver implements IOperatorResolver {

        @Override
        public Predicate resolve(Path<?> key, Object value, CriteriaBuilder builder) {
            if (key.getJavaType() == LocalDateTime.class)
                return builder.greaterThan((Expression<LocalDateTime>) key, (LocalDateTime) value );
            if (key.getJavaType() == LocalDate.class)
                return builder.greaterThan((Expression<LocalDate>) key, (LocalDate) value );

            return builder.greaterThan((Expression<String>) key, value.toString() );

        }
    }

    public static class LessOperatorResolver implements IOperatorResolver {

        @Override
        public Predicate resolve(Path<?> key, Object value, CriteriaBuilder builder) {
            if (key.getJavaType() == LocalDateTime.class)
                return builder.lessThan((Expression<LocalDateTime>) key, (LocalDateTime) value );
            if (key.getJavaType() == LocalDate.class)
                return builder.lessThan((Expression<LocalDate>) key, (LocalDate) value );

            return builder.lessThan((Expression<String>) key, value.toString() );

        }
    }

}
