package com.n3fpoly.hotel_rental.common.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.n3fpoly.hotel_rental.common.models.Account;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {
    public Optional<Account> findByUsername(String username);

    public Optional<Account> findByEmail(String email);

    public Optional<Account> findByTel(String tel);
}
