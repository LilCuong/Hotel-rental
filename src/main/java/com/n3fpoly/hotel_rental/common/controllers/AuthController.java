package com.n3fpoly.hotel_rental.common.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.n3fpoly.hotel_rental.common.models.Account;
import com.n3fpoly.hotel_rental.common.services.AccountSv;
import com.n3fpoly.hotel_rental.common.services.CartSv;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping(path = "/auth")
public class AuthController {

    @Autowired
    private AccountSv accountSv;
    
    @Autowired
    private CartSv cartSv;

    @GetMapping(path = "/login/")
    public ModelAndView getLogin(ModelAndView modelAndView) {
        modelAndView.setViewName("/client/views/auth/login.html");
        return modelAndView;
    }

    @PostMapping(path = "/login/")
    public Object postLogin(ModelAndView modelAndView, RedirectView redirectView, HttpSession httpSession,
            @RequestParam(name = "username") Optional<String> username,
            @RequestParam(name = "password") Optional<String> password) {
        modelAndView.setViewName("/client/views/auth/login.html");
        redirectView.setUrl("/client/");
        if (username.isEmpty() || password.isEmpty()) {
            modelAndView.addObject("message", "Vui lòng nhập đủ thông tin");
            return modelAndView;
        }

        Account account = null;
        {
            account = accountSv.findByUsername(username.get());
            if (account == null)
                account = accountSv.findByEmail(username.get());
            if (account == null)
                account = accountSv.findByTel(username.get());
        }
        if (account == null)
            modelAndView.addObject("message", "Không tìm thấy tài khoản");
        else if (!account.getPassword().equals(password.get()))
            modelAndView.addObject("message", "Mật khẩu không chính xác");
        else {
        	cartSv.getOrCreateCart(account.getId());
            httpSession.setAttribute("loggedInAccount", account);
            return redirectView;
        }
        return modelAndView;
    }

    @GetMapping(path = "/register/")
    public ModelAndView getRegister(ModelAndView modelAndView) {
        modelAndView.setViewName("/client/views/auth/register.html");
        return modelAndView;
    }

    @PostMapping(path = "/register/")
    public ModelAndView postRegister(ModelAndView modelAndView, RedirectView redirectView, HttpSession httpSession,
            @RequestParam(name = "name") Optional<String> name,
            @RequestParam(name = "email") Optional<String> email,
            @RequestParam(name = "tel") Optional<String> tel,
            @RequestParam(name = "cccd") Optional<String> cccd,
            @RequestParam(name = "username") Optional<String> username,
            @RequestParam(name = "password") Optional<String> password,
            @RequestParam(name = "confirm_password") Optional<String> confirm_password) {
        modelAndView.setViewName("/client/views/auth/register.html");
        redirectView.addStaticAttribute(null, password);
        if (name.isEmpty() || email.isEmpty() || tel.isEmpty() || username.isEmpty() || password.isEmpty() || confirm_password.isEmpty()) {
            modelAndView.addObject("message", "Vui lòng nhập đủ thông tin");
            return modelAndView;
        }

        if (!password.equals(confirm_password)) {
            modelAndView.addObject("message", "Mật khẩu xác nhận không khớp");
            return modelAndView;
        }

        Account account = null;
        {
            account = accountSv.findByUsername(username.get());
            if (account == null)
                account = accountSv.findByEmail(username.get());
            if (account == null)
                account = accountSv.findByTel(username.get());
        }
        if (account != null)
            modelAndView.addObject("message", "Thông tin đăng ký đã tồn tại");
        else {
            account = new Account();
            account.setName(name.get());
            account.setEmail(email.get());
            account.setTel(tel.get());
            account.setCccd(cccd.get());
            account.setUsername(username.get());
            account.setPassword(password.get());
            
            try {
                account = accountSv.save(account);
                cartSv.getOrCreateCart(account.getId());
                httpSession.setAttribute("loggedInAccount", account);
                modelAndView.setViewName("redirect:/");
            } catch(Exception ex) {
                modelAndView.addObject("message", ex.getMessage());
            }
        }
        return modelAndView;
    }

    @GetMapping(path = "/logout/")
    public Object getLogout(RedirectView redirectView, HttpSession httpSession) {
        httpSession.invalidate();
        redirectView.setUrl("/auth/login/");
        return redirectView;
    }

}
