package com.n3fpoly.hotel_rental.common.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n3fpoly.hotel_rental.common.models.CartItem;
import com.n3fpoly.hotel_rental.common.repositories.CartItemRepo;

@Service
public class CartItemSv {

	@Autowired
	CartItemRepo cartItemRepo;
	
	public void save(CartItem cartItem) {
		cartItemRepo.save(cartItem);
	}
	
	public void deleteById(long id) {
		cartItemRepo.deleteById(id);
	}
	
	 public CartItem getCartItemByCartIdAndRoomId(Long cartId, Long roomId) {
	        return cartItemRepo.findByCartIdAndRoomId(cartId, roomId);
	    }
	 public CartItem getCartItemByCartIdAndServiceId(Long cartId, Long serviceId) {
		 return cartItemRepo.findByCartIdAndServiceId(cartId, serviceId);
	 }
	 
	 public List<CartItem> findByCartId(Long cartId){
		 return cartItemRepo.findByCartId(cartId);
	 }
	 
	 public Optional<CartItem> findById(Long id) {
		 return cartItemRepo.findById(id);
	 }
	 
	 public List<CartItem> findByServiceId(Long serviceId){
		 return cartItemRepo.findByServiceId(serviceId);
	 }
	 public List<CartItem> findByRoomId(Long serviceId){
		 return cartItemRepo.findByRoomId(serviceId);
	 }
	 
}
