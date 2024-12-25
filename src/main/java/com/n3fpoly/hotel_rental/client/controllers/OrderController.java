package com.n3fpoly.hotel_rental.client.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.n3fpoly.hotel_rental.common.models.Account;
import com.n3fpoly.hotel_rental.common.models.CartItem;
import com.n3fpoly.hotel_rental.common.services.AccountSv;
import com.n3fpoly.hotel_rental.common.services.CartItemSv;
import com.n3fpoly.hotel_rental.common.services.CartSv;
import com.n3fpoly.hotel_rental.common.services.OrderSv;

@Controller
@RequestMapping("/client/order")
public class OrderController {

	@Autowired
	OrderSv orderSv;
	
	@Autowired
	CartSv cartSv;
	
	@Autowired
	CartItemSv cartItemSv;
	
	@Autowired
	AccountSv accountSv;
	
	
}
