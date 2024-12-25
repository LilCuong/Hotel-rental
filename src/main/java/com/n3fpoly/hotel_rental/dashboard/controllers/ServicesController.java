package com.n3fpoly.hotel_rental.dashboard.controllers;

import java.util.List;
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

import com.n3fpoly.hotel_rental.common.models.CartItem;
import com.n3fpoly.hotel_rental.common.models.Service;
import com.n3fpoly.hotel_rental.common.models.ServiceOrder;
import com.n3fpoly.hotel_rental.common.services.CartItemSv;
import com.n3fpoly.hotel_rental.common.services.ServiceOrderSv;
import com.n3fpoly.hotel_rental.common.services.ServiceSv;

@Controller("dashboard.ServicesController")
@RequestMapping(path = "/dashboard/services")
public class ServicesController {

    @Autowired
    private ServiceSv serviceSv;
    @Autowired
    private CartItemSv cartItemSv;
    @Autowired
    ServiceOrderSv serviceOrderSv;

    @GetMapping(path = "/")
    public ModelAndView getIndex(ModelAndView modelAndView) {
        modelAndView.setViewName("/dashboard/views/services/index.html");
        modelAndView.addObject("objects", serviceSv.findAll());
        return modelAndView;
    }

    @GetMapping(path = "/add/")
    public ModelAndView getAddNewService(ModelAndView modelAndView) {
        modelAndView.setViewName("/dashboard/views/services/add.html");
        return modelAndView;
    }

    @PostMapping(path = "/add/")
    public RedirectView postAddNewService(RedirectView redirectView, RedirectAttributes redirectAttributes,
            @RequestParam(name = "name") String name, @RequestParam(name = "price") int price) {
        redirectView.setUrl("/dashboard/services/add/");
        try {
            Service newService = new Service();
            newService.setName(name);
            newService.setPrice(price);
            serviceSv.save(newService);
            redirectAttributes.addFlashAttribute("response_msg", "Thành công");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("response_msg", exception.getMessage());
        }
        return redirectView;
    }

    @GetMapping(path = "/edit/{id}/")
    public ModelAndView getEditService(ModelAndView modelAndView,
            @PathVariable(name = "id") Optional<Long> id) {
        modelAndView.setViewName("/dashboard/views/services/edit.html");
        if (id.isPresent()) {
            Service service = serviceSv.findById(id.get());
            if (service != null) {
                modelAndView.addObject("object", service);
                return modelAndView;
            }
        }
        modelAndView.setViewName("redirect:/dashboard/services/");
        return modelAndView;
    }

    @PostMapping(path = "/edit/{id}/")
    public RedirectView postEditService(RedirectView redirectView, RedirectAttributes redirectAttributes,
            @ModelAttribute Service service) {
        redirectView.setUrl("/dashboard/services/edit/" + service.getId() + "/");
        try {
            serviceSv.save(service);
            redirectAttributes.addFlashAttribute("response_msg", "Thành công");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("response_msg", exception.getMessage());
        }
        return redirectView;
    }
    
    
    @GetMapping("/delete/{id}")
    public Object deleteService(@PathVariable int id, RedirectView redirectView, RedirectAttributes redirectAttributes) {
    	redirectView.setUrl("/dashboard/services/");
    	
        try {
        	Service service = serviceSv.findById(id);
        	
        	List<CartItem> cartItems = cartItemSv.findByServiceId(service.getId());
        	System.out.println(cartItems.size());
        	for(CartItem cartItem : cartItems) {
        		cartItemSv.deleteById(cartItem.getId());
        	}
        	List<ServiceOrder> serviceOrders = serviceOrderSv.findByServiceId(service.getId());
        	System.out.println("service: " + serviceOrders.size());
        	for(ServiceOrder serviceOrder : serviceOrders) {
        		
        		serviceOrder.setService(null);
        		serviceOrderSv.save(serviceOrder);
        	}
        	serviceSv.deleteById(service.getId());
        	redirectAttributes.addFlashAttribute("response_msg", "Xóa thành công");
        }catch (Exception e) {
			// TODO: handle exception
        	redirectAttributes.addFlashAttribute("response_msg", "Xóa thất bại");
        	e.printStackTrace();
		}
        
        return redirectView;
    }
    
}
