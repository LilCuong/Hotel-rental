package com.n3fpoly.hotel_rental.common.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n3fpoly.hotel_rental.common.models.ServiceOrder;
import com.n3fpoly.hotel_rental.common.repositories.ServiceOrderRepo;

@Service
public class ServiceOrderSv {

	@Autowired
	ServiceOrderRepo serviceOrderRepo;
	
	public void save(ServiceOrder serviceOrder) {
		serviceOrderRepo.save(serviceOrder);
	}
	
	public Optional<ServiceOrder> findById(Long id){
		return serviceOrderRepo.findById(id);
	}
	
	public List<ServiceOrder> findByOrderId(Long orderId){
		return serviceOrderRepo.findByOrderId(orderId);
	}
	
	public ServiceOrder findByOrderIdAndServiceId(Long orderId,Long serviceId) {
		return serviceOrderRepo.findByOrderIdAndServiceId(orderId ,serviceId);
	}
	
	public List<ServiceOrder> findByServiceId(Long serviceId){
		return serviceOrderRepo.findByServiceId(serviceId);
	}
	
	public void deleteById(Long id) {
		serviceOrderRepo.deleteById(id);
	}
}
