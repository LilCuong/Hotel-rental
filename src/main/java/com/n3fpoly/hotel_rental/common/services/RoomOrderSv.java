package com.n3fpoly.hotel_rental.common.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n3fpoly.hotel_rental.common.models.RoomOrder;
import com.n3fpoly.hotel_rental.common.repositories.RoomOrderRepo;

@Service
public class RoomOrderSv {

	@Autowired
	RoomOrderRepo roomOrderRepo;
	
	public void save(RoomOrder roomOrder) {
		roomOrderRepo.save(roomOrder);
	}
	
	public List<RoomOrder> findByOrderId(Long orderId){
		return roomOrderRepo.findByOrderId(orderId);
	}
	
	public List<RoomOrder> findByRoomId(Long roomId){
		return roomOrderRepo.findByRoomId(roomId);
	}
	
	public void deleteById(Long id) {
		roomOrderRepo.deleteById(id);
	}
	
}
