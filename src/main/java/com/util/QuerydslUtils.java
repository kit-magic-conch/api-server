package com.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.data.domain.Pageable;

public abstract class QuerydslUtils {
    public static OrderSpecifier[] ordersFromPageable(Pageable pageable, Path parent) {
        return pageable.getSort()
                .stream()
                .map(order -> new OrderSpecifier(
                        order.isAscending() ? Order.ASC : Order.DESC,
                        Expressions.path(Object.class, parent, order.getProperty())))
                .toArray(OrderSpecifier[]::new);
    }
}
