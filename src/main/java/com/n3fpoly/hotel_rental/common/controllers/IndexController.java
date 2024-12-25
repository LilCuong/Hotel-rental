package com.n3fpoly.hotel_rental.common.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpSession;

@Controller("common.IndexController")
public class IndexController {
    @GetMapping(path = { "", "/", "/*/" })
    public RedirectView getIndex(RedirectView redirectView, HttpSession httpSession) {
        if(httpSession.getAttribute("loggedInAccount") != null) {
            redirectView.setUrl("/client/");
        } else {
            redirectView.setUrl("/auth/login/");
        }
        return redirectView;
    }

}
