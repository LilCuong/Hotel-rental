package com.n3fpoly.hotel_rental.client.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.n3fpoly.hotel_rental.common.models.CartItem;
import com.n3fpoly.hotel_rental.common.models.Order;
import com.n3fpoly.hotel_rental.common.models.RoomOrder;
import com.n3fpoly.hotel_rental.common.models.Service;
import com.n3fpoly.hotel_rental.common.models.ServiceOrder;
import com.n3fpoly.hotel_rental.common.services.OrderSv;
import com.n3fpoly.hotel_rental.common.services.RoomOrderSv;
import com.n3fpoly.hotel_rental.common.services.ServiceOrderSv;
import com.n3fpoly.hotel_rental.common.services.ServiceSv;
import com.n3fpoly.hotel_rental.common.utils.Session;

@Controller
@RequestMapping("/client/logs")
public class LogsController {
    @Autowired
    Session session;
    @Autowired
    OrderSv orderSv;
    @Autowired
    RoomOrderSv roomOrderSv;
    @Autowired
    ServiceOrderSv serviceOrderSv;
    
    @Autowired
    ServiceSv serviceSv;

    @GetMapping("/")
    public Object getIndex(ModelAndView modelAndView,Model model, RedirectView redirectView) {
        if (!session.isLoggedIn()) {
            redirectView.setUrl("/auth/login/");
            return redirectView;
        }
        modelAndView.setViewName("/client/views/logs.html");
        List<Order> orders = orderSv.findByAccountId(session.getLoggedInAccount().getId());
        model.addAttribute("orders", orders);
        List<RoomOrder> roomOrders = new ArrayList<RoomOrder>();
        List<ServiceOrder> serviceOrders = new ArrayList<ServiceOrder>();
        for(Order order : orders) {
        	roomOrders = roomOrderSv.findByOrderId(order.getId());
        	serviceOrders = serviceOrderSv.findByOrderId(order.getId());
        }
        
        model.addAttribute("roomOrders", roomOrders);
       model.addAttribute("serviceOrders", serviceOrders);
        
       List<Service> services = serviceSv.findAll();
       model.addAttribute("services", services);
       
       
        
        return modelAndView;
    }
    
    
    @PostMapping("/add-service-to-order")
    public Object addServiceToOrder(@RequestParam("orderId") Long orderId ,@RequestParam(name = "selectedServices", required = false) List<Long> selectedServiceIds, @RequestParam Map<String, String> quantities ,ModelAndView modelAndView, RedirectView redirectView, Model model) {
    	
    	Map<Long, Integer> selectedServices = new HashMap<>();
    	
    	if(selectedServiceIds != null && !selectedServiceIds.isEmpty()) {
    		for(Long serviceId : selectedServiceIds) {
    			String quantityStr = quantities.get("quantities[" + serviceId + "]");
    			
			if(quantityStr != null && !quantityStr.isEmpty()) {
				try {
					int quantity  = Integer.parseInt(quantityStr);
					if(quantity > 0) {
						selectedServices.put(serviceId, quantity);
						System.out.println(selectedServices);
					}
				}catch (Exception e) {
					redirectView.setUrl("/client/logs/");
					redirectView.addStaticAttribute("danger", "Số lượng không hợp lệ!");
					return redirectView;
				}
			}
    		}
    	}
    	
    	if(selectedServices.isEmpty()) {
    		redirectView.setUrl("/client/logs/");
    		redirectView.addStaticAttribute("danger", "Bạn phải chọn ít nhất 1 dịch vụ!");
    	}
    	model.addAttribute("orderId", orderId);
    	model.addAttribute("selectedServices", selectedServices);
    	modelAndView.setViewName("/client/views/service-payment.html");
    	return modelAndView;
    }
    
    @PostMapping("/update-service-note")
	public Object updateServiceNote(@RequestParam("note") String note, @RequestParam("id") Long serviceOrderId, RedirectAttributes redirectAttributes, RedirectView redirectView) {
		redirectView.setUrl("/client/logs/");
		Optional<ServiceOrder> serviceOrder = serviceOrderSv.findById(serviceOrderId);
		if(serviceOrder != null) {
			try {
				serviceOrder.get().setNote(note);
				serviceOrderSv.save(serviceOrder.get());
				redirectAttributes.addFlashAttribute("success", "Lưu ghi chú thành công!");
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("danger", "Lưu ghi chú thất bại!");
				
			}
		}else {
			redirectAttributes.addFlashAttribute("danger", "Không tìm thấy dịch vụ!");
		}
		
		return redirectView;
	}

}
