
package com.tenantFlow.pagination;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

public class SortValidator {

    public static void validate(
            Pageable pageable,
            Set<String> allowedFields
    ) {

        for (Sort.Order order : pageable.getSort()) {

            if (!allowedFields.contains(order.getProperty())) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid sort field: " + order.getProperty()
                );
            }
        }
    }
}