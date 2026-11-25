package com.tenantFlow.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GenericSpecification {

    public static <T> Specification<T> filters(
            List<FilterCriteria> filters
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            for (FilterCriteria filter : filters) {

                Object value = filter.getValue();

                if (value == null ||
                        (value instanceof String s && s.isBlank())) {
                    continue;
                }

                switch (filter.getOperation()) {

                    case EQUAL -> predicates.add(
                            cb.equal(
                                    root.get(filter.getField()),
                                    value
                            )
                    );

                    case LIKE -> predicates.add(
                            cb.like(
                                    cb.lower(
                                            root.<String>get(filter.getField())
                                    ),
                                    "%" + value.toString()
                                            .toLowerCase(Locale.ROOT) + "%"
                            )
                    );
                }
            }

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}