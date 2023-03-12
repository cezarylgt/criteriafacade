package com.cezarylgt.java.criteriafacade;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
class InvalidCriteriaValueTypeException extends Exception {
    public InvalidCriteriaValueTypeException(String value, Class<?> klazz) {
        super(String.format("Provided value (%s) cannot be parsed to required type: %s", value, klazz.getTypeName()));
    }
}


public class GenericSpecificationBuilder<T> implements ISpecificationBuilder<T> {
    @Override
    public Specification<T> build(List<SearchCriteria> criteriaList) {
        if (criteriaList.size() == 0)
            return null;
        List<Specification<?>> specs = criteriaList.stream().map(this::getSpecification).collect(Collectors.toList());
        Specification result = specs.get(0);
        for (int i = 1; i < criteriaList.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }


    protected Specification<T> getSpecification(SearchCriteria criteria) {
        Specification<T> specification = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return buildPredicates(criteria, root, criteriaBuilder);
            }
        };
        return specification;
    }

    private Path<?> getPath(Root<?> root, String attributeName) {
        Path<?> path = root;
        for (String part : attributeName.split("\\.")) {
            path = path.get(part);
        }
        return path;
    }

    @SneakyThrows
    protected Predicate buildPredicates(SearchCriteria criteria, Root root, CriteriaBuilder builder) {
        Path<?> key = getPath(root, criteria.getKey());
        Class<?> klazz = key.getJavaType();

        IOperatorResolver operationResolver = ComparisonOperator.valueOfLabel(criteria.getOperator()).create();
        if (klazz == boolean.class || klazz == Boolean.class) {
            criteria = resolveBoolean(criteria, root);
        }
        if (klazz == LocalDate.class || klazz == LocalDateTime.class) {
            criteria = resolveDate(criteria, root);
        }
        return operationResolver.resolve(key, criteria.getValue(), builder);
    }

    protected SearchCriteria resolveBoolean(SearchCriteria criteria, Root<?> root) throws InvalidCriteriaValueTypeException {
        Class<?> criteriaValueClass = criteria.getValue().getClass();
        if (criteriaValueClass == Boolean.class) {
            return criteria;
        }
        if (criteriaValueClass == String.class || criteriaValueClass == Integer.class) {
            criteria.setValue(Boolean.parseBoolean((String) criteria.getValue()));
        }
        throw new InvalidCriteriaValueTypeException(criteria.getValue().toString(), Boolean.class);
    }

    protected SearchCriteria resolveDate(SearchCriteria criteria, Root<?> root) throws InvalidCriteriaValueTypeException {
        Class<?> criteriaRootClass = root.get(criteria.getKey()).getJavaType();
        Class<?> criteriaValueClass = criteria.getValue().getClass();
        // when root is LocalDateTime
        if (criteriaRootClass == LocalDateTime.class) {

            if (criteriaValueClass == String.class) {
                LocalDateTime value = null;
                if (((String) criteria.getValue()).length() == 10)
                    value = LocalDate.parse((String) criteria.getValue()).atStartOfDay();
                else
                    value = LocalDateTime.parse((String) criteria.getValue());
                criteria.setValue(value);
                return criteria;
            }
            if (criteriaValueClass == LocalDate.class) {
                LocalDateTime value = ((LocalDate) criteria.getValue()).atStartOfDay();
                criteria.setValue(value);
                return criteria;
            }

            if (criteriaValueClass == LocalDateTime.class)
                return criteria;
            throw new InvalidCriteriaValueTypeException(criteria.getValue().toString(), LocalDateTime.class);
        }
        // when root is LocalDate
        if (criteriaRootClass == LocalDate.class) {
            if (criteriaValueClass == String.class) {
                LocalDate value = LocalDate.parse((String) criteria.getValue());
                criteria.setValue(value);
                return criteria;
            }
            if (criteriaValueClass == LocalDate.class) {
                return criteria;
            }

            if (criteriaValueClass == LocalDateTime.class) {
                LocalDate value = ((LocalDateTime) criteria.getValue()).toLocalDate();
                criteria.setValue(value);
                return criteria;
            }
            throw new InvalidCriteriaValueTypeException(criteria.getValue().toString(), LocalDate.class);
        }
        throw new RuntimeException();
    }
}
