package com.n3fpoly.hotel_rental.client.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.n3fpoly.hotel_rental.common.models.Cart;
import com.n3fpoly.hotel_rental.common.services.CartSv;
import com.n3fpoly.hotel_rental.common.utils.Session;


@Controller("client.IndexController")
@RequestMapping(path = "/client")
public class IndexController {
    @Autowired Session session;
    
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
        
        
        modelAndView.setViewName("/client/views/index.html");
        return modelAndView;
    }

}
