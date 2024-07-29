package dev.pratishtha.project.CartService.repositories;

import dev.pratishtha.project.CartService.models.Cart;
import dev.pratishtha.project.CartService.models.SqlQueries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {

    @Query(value = SqlQueries.GET_ALL_CARTS_IN_DESC_WITH_LIMIT, nativeQuery = true)
    List<Cart> findAllByOrderByUuidDescWithLimit(int limit);

    @Query(value = SqlQueries.GET_ALL_CARTS_IN_ASC_WITH_LIMIT, nativeQuery = true)
    List<Cart> findAllByOrderByUuidAscWithLimit(int limit);

    @Query(value = SqlQueries.GET_ALL_CARTS_IN_DATE_RANGE, nativeQuery = true)
    List<Cart> findAllCartsInDateRange(@Param("startDate") Date startDate,@Param("endDate") Date endDate);

    @Query(value = SqlQueries.GET_ALL_CARTS_IN_DESC_ORDER_BY_DATE_RANGE_WITHIN_LIMIT, nativeQuery = true)
    List<Cart> findAllSortedCartsInDateRangeWithinLimitByDescOrder(Date startDate, Date endDate, int limit);

    @Query(value = SqlQueries.GET_ALL_CARTS_IN_ASC_ORDER_BY_DATE_RANGE_WITHIN_LIMIT, nativeQuery = true)
    List<Cart> findAllSortedCartsInDateRangeWithinLimitByAscOrder(Date startDate, Date endDate, int limit);

    List<Cart> findAllByOrderByUuidDesc();

    List<Cart> findAllByUserId(String userId);

    Optional<Cart> findByUuidAndUserId(UUID uuid, String userId);

    @Query(value = SqlQueries.GET_ALL_CARTS_BY_USER_IN_DATE_RANGE, nativeQuery = true)
    List<Cart> findAllByUserIdInDateRange(String userId, Date startDate, Date endDate);
}
