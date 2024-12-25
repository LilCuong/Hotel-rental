package com.n3fpoly.hotel_rental.common.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n3fpoly.hotel_rental.common.models.Room;
import com.n3fpoly.hotel_rental.common.repositories.RoomRepo;

@Service
public class RoomSv {
    @Autowired
    private RoomRepo roomRepo;

    public Room save(Room object) {
        return roomRepo.save(object);
    }

    public void delete(Room object) {
        roomRepo.delete(object);
    }

    public void deleteById(long id) {
        roomRepo.deleteById(id);
    }

    public Room findById(long id) {
        return roomRepo.findById(id).orElse(null);
    }

    public List<Room> findAll() {
        return roomRepo.findAll();
    }
    
    
}
