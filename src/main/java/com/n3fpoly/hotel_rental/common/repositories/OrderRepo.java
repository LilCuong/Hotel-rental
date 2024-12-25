package com.n3fpoly.hotel_rental.common.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.n3fpoly.hotel_rental.common.models.Order;

import jakarta.transaction.Transactional;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findByAccountId(Long accountId);
    @Modifying
    @Transactional
    @Query("DELETE FROM Order o WHERE o.id = :id")
    void deleteById(@Param("id") long id);
}
