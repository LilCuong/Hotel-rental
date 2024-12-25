package com.n3fpoly.hotel_rental.common.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n3fpoly.hotel_rental.common.models.Account;
import com.n3fpoly.hotel_rental.common.repositories.AccountRepo;

@Service
public class AccountSv {
    @Autowired
    private AccountRepo accountRepo;

    public Account save(Account object) {
        return accountRepo.save(object);
    }

    public void delete(Account object) {
        accountRepo.delete(object);
    }

    public void deleteById(long id) {
        accountRepo.deleteById(id);
    }

    public Account findById(long id) {
        return accountRepo.findById(id).orElse(null);
    }

    public List<Account> findAll() {
        return accountRepo.findAll();
    }

    public Account findByUsername(String username) {
        return accountRepo.findByUsername(username).orElse(null);
    }

    public Account findByEmail(String email) {
        return accountRepo.findByEmail(email).orElse(null);
    }

    public Account findByTel(String tel) {
        return accountRepo.findByEmail(tel).orElse(null);
    }
}
