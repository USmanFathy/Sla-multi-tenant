package com.tenantFlow.specification;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FilterCriteria {

    private final String field;

    private final Object value;

    private final FilterOperation operation;
}