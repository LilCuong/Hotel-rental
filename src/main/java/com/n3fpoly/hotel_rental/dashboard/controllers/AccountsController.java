package com.n3fpoly.hotel_rental.dashboard.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.n3fpoly.hotel_rental.common.models.Account;
import com.n3fpoly.hotel_rental.common.services.AccountSv;

@Controller
@RequestMapping(path = "/dashboard/accounts")
public class AccountsController {
    @Autowired
    private AccountSv accountSv;

    @GetMapping(path = "/")
    public ModelAndView getIndex(ModelAndView modelAndView) {
        modelAndView.setViewName("/dashboard/views/accounts/index.html");
        modelAndView.addObject("objects", accountSv.findAll());
        return modelAndView;
    }

    @GetMapping(path = "/add/")
    public ModelAndView getAddNewAccount(ModelAndView modelAndView) {
        modelAndView.setViewName("/dashboard/views/accounts/add.html");
        return modelAndView;
    }

    @PostMapping(path = "/add/")
    public RedirectView postAddNewAccount(RedirectView redirectView, RedirectAttributes redirectAttributes,
            @RequestParam(name = "email") String email, @RequestParam(name = "name") String name,
            @RequestParam(name = "tel") String tel,@RequestParam(name = "cccd") String cccd, @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password, @RequestParam(name = "role") String role) {
        redirectView.setUrl("/dashboard/accounts/add/");
        try {
            Account newAccount = new Account();
            newAccount.setEmail(email);
            newAccount.setName(name);
            newAccount.setTel(tel);
            newAccount.setUsername(username);
            newAccount.setPassword(password);
            newAccount.setRole(role);
            accountSv.save(newAccount);
            redirectAttributes.addFlashAttribute("response_msg", "Thành công");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("response_msg", exception.getMessage());
        }
        return redirectView;
    }

    @GetMapping(path = "/edit/{id}/")
    public ModelAndView getEditAccount(ModelAndView modelAndView,
            @PathVariable(name = "id") Optional<Long> id) {
        modelAndView.setViewName("/dashboard/views/accounts/edit.html");
        if (id.isPresent()) {
            Account account = accountSv.findById(id.get());
            if (account != null) {
                modelAndView.addObject("object", account);
                return modelAndView;
            }
        }
        modelAndView.setViewName("redirect:/dashboard/accounts/");
        return modelAndView;
    }

    @PostMapping(path = "/edit/{id}/")
    public RedirectView postEditAccount(RedirectView redirectView, RedirectAttributes redirectAttributes,
            @ModelAttribute Account account) {
        redirectView.setUrl("/dashboard/accounts/edit/" + account.getId() + "/");
        try {
            accountSv.save(account);
            redirectAttributes.addFlashAttribute("response_msg", "Thành công");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("response_msg", exception.getMessage());
        }
        return redirectView;
    }

    @GetMapping(path = "/delete")
    public RedirectView getDelete(RedirectView redirectView, RedirectAttributes redirectAttributes,
            @RequestParam("id") int id) {
        try {
            accountSv.deleteById(id);
            redirectAttributes.addFlashAttribute("response_msg", "Xóa thành công");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("response_msg", exception.getMessage());
        }
        redirectView.setUrl("/dashboard/accounts/");
        return redirectView;
    }
}
