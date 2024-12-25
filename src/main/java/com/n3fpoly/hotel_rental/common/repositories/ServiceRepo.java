package com.n3fpoly.hotel_rental.common.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.n3fpoly.hotel_rental.common.models.Service;
import com.n3fpoly.hotel_rental.common.models.ServiceOrder;

@Repository
public interface ServiceRepo extends JpaRepository<Service, Long> {
	
}
