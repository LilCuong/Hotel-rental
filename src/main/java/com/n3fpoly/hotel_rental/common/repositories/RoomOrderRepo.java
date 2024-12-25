package com.n3fpoly.hotel_rental.common.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.n3fpoly.hotel_rental.common.models.RoomOrder;

@Repository
public interface RoomOrderRepo extends JpaRepository<RoomOrder, Long> {
	List<RoomOrder> findByOrderId(Long orderId);
	List<RoomOrder> findByRoomId(Long roomId);
}
