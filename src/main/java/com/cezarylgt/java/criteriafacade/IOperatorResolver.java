package com.cezarylgt.java.criteriafacade;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

public interface IOperatorResolver {

    /**
     *
     * @param key path to object field ( dot notation for nested objects is supported e.g.: address.street -> corresponds to 'street' property of nested object stored in 'address' field)
     * @param value a value that will be matched with desired comparison method
     * @param builder CriteriaBuilder
     * @return Predicate with desired comparison
     */
    Predicate resolve(Path<?> key, Object value, CriteriaBuilder builder);
}
