package com.n3fpoly.hotel_rental.common.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n3fpoly.hotel_rental.common.models.Account;
import com.n3fpoly.hotel_rental.common.models.Cart;
import com.n3fpoly.hotel_rental.common.repositories.AccountRepo;
import com.n3fpoly.hotel_rental.common.repositories.CartRepo;

@Service
public class CartSv {

	@Autowired
	CartRepo cartRepo;
	
	@Autowired
	AccountRepo accountRepo;
	
	public Cart findByAccountId(long accountId) {
		return cartRepo.findByAccountId(accountId);
	}
	
	public void save(Cart cart) {
		cartRepo.save(cart);
	}
	
	public Cart getOrCreateCart(Long accountId) {		
		Cart existingCart = cartRepo.findByAccountId(accountId);	
		Optional<Account> account = accountRepo.findById(accountId);
		if(existingCart == null) {
			Cart newCart= new Cart();
			newCart.setStartDate(null);
			newCart.setEndDate(null);
			newCart.setAccount(account.get());
			cartRepo.save(newCart);
			return newCart;
		}		
		return existingCart;
	}
}
