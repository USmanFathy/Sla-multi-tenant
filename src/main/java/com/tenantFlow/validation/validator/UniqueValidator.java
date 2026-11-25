package com.tenantFlow.validation.validator;

import com.tenantFlow.validation.annotation.Unique;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueValidator
        implements ConstraintValidator<Unique, Object> {

    @PersistenceContext
    private EntityManager entityManager;

    private Class<?> entity;

    private String field;

    @Override
    public void initialize(Unique annotation) {

        this.entity = annotation.entity();

        this.field = annotation.field();
    }

    @Override
    public boolean isValid(
            Object value,
            ConstraintValidatorContext context
    ) {

        if (value == null) {
            return true;
        }

        CriteriaBuilder cb =
                entityManager.getCriteriaBuilder();

        CriteriaQuery<Integer> query =
                cb.createQuery(Integer.class);

        Root<?> root = query.from(entity);

        query.select(cb.literal(1));

        query.where(
                cb.equal(root.get(field), value)
        );

        return entityManager.createQuery(query)
                .setMaxResults(1)
                .getResultList()
                .isEmpty();
    }
}