package com.n3fpoly.hotel_rental.common.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.n3fpoly.hotel_rental.common.models.ServiceOrder;

@Repository
public interface ServiceOrderRepo extends JpaRepository<ServiceOrder, Long> {
	List<ServiceOrder> findByOrderId(Long orderId);
	ServiceOrder findByOrderIdAndServiceId(Long orderId ,Long serviceId);
	List<ServiceOrder> findByServiceId(Long serviceId);
}
