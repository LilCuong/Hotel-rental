package com.n3fpoly.hotel_rental.client.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.n3fpoly.hotel_rental.common.models.Cart;
import com.n3fpoly.hotel_rental.common.models.Order;
import com.n3fpoly.hotel_rental.common.services.CartSv;
import com.n3fpoly.hotel_rental.common.services.OrderSv;
import com.n3fpoly.hotel_rental.common.services.RoomSv;
import com.n3fpoly.hotel_rental.common.utils.Session;

@Controller
@RequestMapping(path = "/client/rooms")
public class RoomsController {
    @Autowired
    RoomSv roomSv;
    @Autowired OrderSv orderSv;
    @Autowired
    Session session;
    @Autowired
	CartSv cartSv;


    @GetMapping(path = { "", "/" })
    public Object getIndex(ModelAndView modelAndView, RedirectView redirectView, Model model) {
        if (!session.isLoggedIn()) {
            redirectView.setUrl("/auth/login/");
            return redirectView;
        }
        Cart getCart = cartSv.findByAccountId(session.getLoggedInAccount().getId());
        if(getCart == null) {
        	Cart cart = new Cart(); 
        	model.addAttribute("cart", cart);
        }else {
        	model.addAttribute("cart", getCart);
		}
        
        modelAndView.addObject("rooms", roomSv.findAll());
        modelAndView.setViewName("/client/views/rooms.html");
        return modelAndView;
    }
    
    

	/*
	 * @GetMapping(path = "/pick/{id}/") public Object getPick(ModelAndView
	 * modelAndView, RedirectView redirectView,
	 * 
	 * @PathVariable(name = "id") Optional<Integer> id) { if (!session.isLoggedIn())
	 * { redirectView.setUrl("/auth/login/"); return redirectView; } if
	 * (id.isPresent()) { Order order = new Order();
	 * order.setAccount(session.getLoggedInAccount());
	 * order.setRoom(roomSv.findById(id.get())); order.setStatus("PENDING"); try {
	 * orderSv.save(order); redirectView.setUrl("/client/payment/"); return
	 * redirectView; } catch(Exception ex) { System.out.println(ex.getMessage());
	 * redirectView.setUrl("/client/"); return redirectView; } } else {
	 * redirectView.setUrl("/client/"); return redirectView; } }
	 */
}
