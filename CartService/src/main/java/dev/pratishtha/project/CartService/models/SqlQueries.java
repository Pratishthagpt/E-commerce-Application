package dev.pratishtha.project.CartService.models;

public class SqlQueries {

    public static final String GET_ALL_CARTS_IN_DATE_RANGE = "select * from Cart where created_at between :startDate and :endDate";

    public static final String GET_ALL_CARTS_IN_DESC_ORDER_BY_DATE_RANGE_WITHIN_LIMIT = "select * from Cart where created_at between :startDate and :endDate order by id desc limit :limit";

    public static final String GET_ALL_CARTS_IN_ASC_ORDER_BY_DATE_RANGE_WITHIN_LIMIT = "select * from Cart  where created_at between :startDate and :endDate order by id asc limit :limit";

    public static final String GET_ALL_CARTS_IN_DESC_WITH_LIMIT = "select * from Cart order by id desc limit :limit";

    public static final String GET_ALL_CARTS_IN_ASC_WITH_LIMIT = "select * from Cart order by id asc limit :limit";

    public static final String GET_ALL_CARTS_BY_USER_IN_DATE_RANGE = "select * from Cart where user_id = :userId and created_at between :startDate and :endDate";

}
