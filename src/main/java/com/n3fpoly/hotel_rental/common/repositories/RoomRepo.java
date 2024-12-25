package com.n3fpoly.hotel_rental.common.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.n3fpoly.hotel_rental.common.models.Room;

@Repository
public interface RoomRepo extends JpaRepository<Room, Long> {
}
