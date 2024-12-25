package com.n3fpoly.hotel_rental.common.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.n3fpoly.hotel_rental.common.models.Cart;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long>{
	Cart findByAccountId(long accountId);
}
