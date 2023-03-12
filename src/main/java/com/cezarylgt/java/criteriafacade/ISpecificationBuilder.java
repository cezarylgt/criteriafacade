package com.cezarylgt.java.criteriafacade;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;


public interface ISpecificationBuilder<T> {
    Specification<T> build(List<SearchCriteria> criteriaList);

}
