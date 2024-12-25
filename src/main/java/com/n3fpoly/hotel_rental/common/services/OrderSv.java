package com.n3fpoly.hotel_rental.common.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n3fpoly.hotel_rental.common.models.Order;
import com.n3fpoly.hotel_rental.common.repositories.OrderRepo;

import jakarta.transaction.Transactional;

@Service
public class OrderSv {
    @Autowired
    private OrderRepo orderRepo;

    public Order save(Order object) {
        return orderRepo.save(object);
    }

    public void delete(Order object) {
        orderRepo.delete(object);
    }
    
    public void deleteById(long id) {
        orderRepo.deleteById(id);
    }

    public Order findById(long id) {
        return orderRepo.findById(id).orElse(null);
    }

    public List<Order> findAll() {
        return orderRepo.findAll();
    }
    
    public List<Order> findByAccountId( Long accountId){
    	return orderRepo.findByAccountId(accountId);
    }
}
