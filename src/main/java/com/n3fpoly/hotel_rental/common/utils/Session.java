package com.n3fpoly.hotel_rental.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.n3fpoly.hotel_rental.common.models.Account;

import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;

@Component
@Setter
@Getter
public class Session {
    @Autowired
    private HttpSession httpSession;

    public Account getLoggedInAccount() {
        if (isLoggedIn())
            return ((Account) httpSession.getAttribute("loggedInAccount"));
        else
            return null;
    }

    public boolean isLoggedIn() {
        if (httpSession.getAttribute("loggedInAccount") != null)
            return true;
        else
            return false;
    }

    public boolean isDirector() {
        if (isLoggedIn() && getLoggedInAccount().getRole().equalsIgnoreCase("director"))
            return true;
        else
            return false;
    }
}
