package com.n3fpoly.hotel_rental.dashboard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.n3fpoly.hotel_rental.common.services.AccountSv;
import com.n3fpoly.hotel_rental.common.services.OrderSv;

@Controller("dashboard.IndexController")
@RequestMapping(path = "/dashboard")
public class IndexController {
    @Autowired
    private AccountSv accountSv;
    @Autowired
    private OrderSv orderSv;

    @GetMapping(path = { "", "/" })
    public ModelAndView getIndex(ModelAndView modelAndView) {
        modelAndView.setViewName("/dashboard/views/index.html");
        modelAndView.addObject("totalAccount", accountSv.findAll().size());
        modelAndView.addObject("totalOrder", orderSv.findAll().size());
		/*
		 * modelAndView.addObject("totalUsedService",
		 * orderSv.findAll().stream().mapToInt(order ->
		 * order.getServices().size()).sum()); modelAndView.addObject("totalIncome",
		 * orderSv.findAll().stream().mapToInt(order -> order.getTotalPrice()).sum());
		 */
        return modelAndView;
    }
}
