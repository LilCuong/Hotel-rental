package com.n3fpoly.hotel_rental.client.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/client/contact")
public class ContactController {
    @GetMapping(path = { "", "/" })
    public ModelAndView getIndex(ModelAndView modelAndView) {
        modelAndView.setViewName("/client/views/contact.html");
        return modelAndView;
    }
}
