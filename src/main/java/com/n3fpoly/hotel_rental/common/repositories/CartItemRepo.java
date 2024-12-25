package com.n3fpoly.hotel_rental.common.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.n3fpoly.hotel_rental.common.models.CartItem;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Long> {
	CartItem findByCartIdAndRoomId(Long cartId, Long roomId);
	CartItem findByCartIdAndServiceId(Long cartId, Long serviceId);
	List<CartItem> findByCartId(Long cartId);
	List<CartItem> findByServiceId(Long serviceId);
	List<CartItem> findByRoomId(Long roomId);
	
}
