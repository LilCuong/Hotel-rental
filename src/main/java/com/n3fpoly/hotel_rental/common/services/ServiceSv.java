package com.n3fpoly.hotel_rental.common.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n3fpoly.hotel_rental.common.repositories.ServiceRepo;

@Service
public class ServiceSv {
    @Autowired
    private ServiceRepo serviceRepo;

    public com.n3fpoly.hotel_rental.common.models.Service save(com.n3fpoly.hotel_rental.common.models.Service object) {
        return serviceRepo.save(object);
    }

    public void delete(com.n3fpoly.hotel_rental.common.models.Service object) {
        serviceRepo.delete(object);
    }

    public void deleteById(long id) {
        serviceRepo.deleteById(id);
    }

    public com.n3fpoly.hotel_rental.common.models.Service findById(long id) {
        return serviceRepo.findById(id).orElse(null);
    }

    public List<com.n3fpoly.hotel_rental.common.models.Service> findAll() {
        return serviceRepo.findAll();
    }
}
