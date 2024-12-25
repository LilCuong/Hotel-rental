package com.n3fpoly.hotel_rental.dashboard.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.n3fpoly.hotel_rental.common.models.Order;
import com.n3fpoly.hotel_rental.common.models.Room;
import com.n3fpoly.hotel_rental.common.models.RoomOrder;
import com.n3fpoly.hotel_rental.common.models.Service;
import com.n3fpoly.hotel_rental.common.models.ServiceOrder;
import com.n3fpoly.hotel_rental.common.services.OrderSv;
import com.n3fpoly.hotel_rental.common.services.RoomOrderSv;
import com.n3fpoly.hotel_rental.common.services.RoomSv;
import com.n3fpoly.hotel_rental.common.services.ServiceOrderSv;
import com.n3fpoly.hotel_rental.common.services.ServiceSv;

import org.springframework.web.bind.annotation.RequestParam;

@Controller("dashboard.OrdersController")
@RequestMapping(path = "/dashboard/orders")
public class OrdersController {
	@Autowired
	private OrderSv orderSv;
	@Autowired
	private ServiceSv serviceSv;
	@Autowired
	private RoomOrderSv roomOrderSv;
	@Autowired
	private ServiceOrderSv serviceOrderSv;
	@Autowired
	RoomSv roomSv;

	@GetMapping(path = "/")

	public ModelAndView getIndex(ModelAndView modelAndView) {
		modelAndView.setViewName("/dashboard/views/orders/index.html");
		modelAndView.addObject("objects", orderSv.findAll());
		modelAndView.setViewName("redirect:./pending/");
		return modelAndView;
	}

	@GetMapping("/pending/")
	public ModelAndView getPending(ModelAndView modelAndView, Model model) {
		modelAndView.setViewName("/dashboard/views/orders/pending.html");

		List<Order> orders = orderSv.findAll().stream().filter(order -> order.getStatus().equalsIgnoreCase("WAITING"))
				.collect(Collectors.toList());
		model.addAttribute("orders", orders);
		List<RoomOrder> roomOrders = new ArrayList<RoomOrder>();
		List<ServiceOrder> serviceOrders = new ArrayList<ServiceOrder>();
		for (Order order : orders) {
			roomOrders = roomOrderSv.findByOrderId(order.getId());
			serviceOrders = serviceOrderSv.findByOrderId(order.getId());
		}

		model.addAttribute("roomOrders", roomOrders);
		model.addAttribute("serviceOrders", serviceOrders);

		return modelAndView;
	}

	@GetMapping(path = "/edit/{id}/")
	public ModelAndView getDetail(ModelAndView modelAndView, @PathVariable(name = "id") int id, Model model) {
		modelAndView.setViewName("/dashboard/views/orders/edit.html");
		Order order = orderSv.findById(id);
		modelAndView.addObject("object", order);

		List<RoomOrder> roomOrders = roomOrderSv.findByOrderId(order.getId());
		List<ServiceOrder> serviceOrders = serviceOrderSv.findByOrderId(order.getId());

		model.addAttribute("roomOrders", roomOrders);
		model.addAttribute("serviceOrders", serviceOrders);
		return modelAndView;
	}

	@PostMapping(path = "/edit/{id}/")
	public ModelAndView postEdit(ModelAndView modelAndView, @RequestParam(name = "id") int id,
			@RequestParam(name = "orderStatus", defaultValue = "WAITING") String orderStatus, Model model) {
		modelAndView.setViewName("/dashboard/views/orders/edit.html");

		try {
			Order order = orderSv.findById(id);
			order.setStatus(orderStatus);
			
			if(orderStatus.equals("EXPIRED")) {
				List<RoomOrder> roomOrders = roomOrderSv.findByOrderId(order.getId());
				for(RoomOrder roomOrder : roomOrders) {
					Room room = roomSv.findById(roomOrder.getRoom().getId());
					room.setStatus("EMPTY");
					roomSv.save(room);
				}
			}
			
			modelAndView.addObject("object", order);
			orderSv.save(order);

			List<RoomOrder> roomOrders = roomOrderSv.findByOrderId(order.getId());
			List<ServiceOrder> serviceOrders = serviceOrderSv.findByOrderId(order.getId());

			model.addAttribute("roomOrders", roomOrders);
			model.addAttribute("serviceOrders", serviceOrders);
			modelAndView.addObject("response_msg", "Thành công");
		} catch (Exception exception) {
			modelAndView.addObject("response_msg", exception.getMessage());
		}
		return modelAndView;
	}

	@GetMapping("/living/")
	public ModelAndView getLiving(ModelAndView modelAndView, Model model) {
		modelAndView.setViewName("/dashboard/views/orders/living.html");
		List<Order> orders = orderSv.findAll().stream().filter(order -> order.getStatus().equalsIgnoreCase("LIVING"))
				.collect(Collectors.toList());
		model.addAttribute("orders", orders);

		List<Service> services = serviceSv.findAll();
		model.addAttribute("services", services);

		List<RoomOrder> roomOrders = new ArrayList<RoomOrder>();
		List<ServiceOrder> serviceOrders = new ArrayList<ServiceOrder>();
		for (Order order : orders) {
			roomOrders = roomOrderSv.findByOrderId(order.getId());
			serviceOrders = serviceOrderSv.findByOrderId(order.getId());
		}

		model.addAttribute("roomOrders", roomOrders);
		model.addAttribute("serviceOrders", serviceOrders);

		return modelAndView;
	}

	@GetMapping(path = "/{id}/services/")
	public ModelAndView getAddService(ModelAndView modelAndView, @PathVariable(name = "id") int id) {
		modelAndView.setViewName("/dashboard/views/orders/services.html");
		Order order = orderSv.findById(id);
		modelAndView.addObject("object", order);
		modelAndView.addObject("services", serviceSv.findAll());
		return modelAndView;
	}

	@PostMapping("/add-service/")
	public Object servicePay(@RequestParam("orderId") Long orderId,
			@RequestParam(name = "selectedServices", required = false) List<Long> selectedServiceIds,
			@RequestParam Map<String, String> quantities, ModelAndView modelAndView, RedirectView redirectView,
			Model model, RedirectAttributes redirectAttributes) {

		Map<Long, Integer> selectedServices = new HashMap<>();

		if (selectedServiceIds != null && !selectedServiceIds.isEmpty()) {
			for (Long serviceId : selectedServiceIds) {
				String quantityStr = quantities.get("quantities[" + serviceId + "]");

				if (quantityStr != null && !quantityStr.isEmpty()) {
					try {
						int quantity = Integer.parseInt(quantityStr);
						if (quantity > 0) {
							selectedServices.put(serviceId, quantity);
						}
					} catch (Exception e) {
						redirectView.setUrl("/dashboard/orders/living/");
						redirectView.addStaticAttribute("response_msg", "Số lượng không hợp lệ!");
						return redirectView;
					}
				}
			}
		}

		if (selectedServices.isEmpty()) {
			redirectView.setUrl("/dashboard/orders/living/");
			redirectView.addStaticAttribute("response_msg", "Bạn phải chọn ít nhất 1 dịch vụ!");
			return redirectView;
		}

		Order order = orderSv.findById(orderId);

		int serviceTotal = 0;

		for (Map.Entry<Long, Integer> entry : selectedServices.entrySet()) {
			Long serviceId = entry.getKey();
			Integer quantity = entry.getValue();
			Service service = serviceSv.findById(serviceId);
			ServiceOrder serviceOrder = serviceOrderSv.findByOrderIdAndServiceId(orderId, serviceId);
			if (serviceOrder != null) {
				serviceOrder.setQuantity(serviceOrder.getQuantity() + quantity);
				serviceOrderSv.save(serviceOrder);
			} else {
				ServiceOrder newServiceOrder = new ServiceOrder();
				newServiceOrder.setName(service.getName());
				newServiceOrder.setOrder(order);
				newServiceOrder.setPrice(service.getPrice());
				newServiceOrder.setQuantity(quantity);
				newServiceOrder.setService(service);
				serviceOrderSv.save(newServiceOrder);
			}
			serviceTotal = serviceTotal + (service.getPrice() * quantity);
		}

		order.setTotal(order.getTotal() + serviceTotal);
		orderSv.save(order);

		
		redirectView.setUrl("/dashboard/orders/living/");
		redirectAttributes.addFlashAttribute("response_msg", "Thêm dịch vụ thành công");
		return redirectView;

	}

//	@PostMapping(path = "/{id}/services/")
//	public RedirectView postAddService(RedirectView redirectView,
//
//			@PathVariable(name = "id") int orderId, @RequestParam("service_id") int serviceId) {
//		Order order = orderSv.findById(orderId);
//		List<Service> services = order.getServices();
//		Service service = serviceSv.findById(serviceId);
//		services.add(service);
//		order.setServices(services);
//		orderSv.save(order);
//		redirectView.setUrl("/dashboard/orders/" + orderId + "/services/");
//		return redirectView;
//	}
//
//	@GetMapping(path = "/{id}/services/delete")
//	public RedirectView getDeleteService(RedirectView redirectView,
//
//			@PathVariable(name = "id") int orderId, @RequestParam("service_id") int serviceId) {
//		Order order = orderSv.findById(orderId);
//		List<Service> services = order.getServices();
//		Service service = serviceSv.findById(serviceId);
//		services.removeIf(sv -> sv.getId() == service.getId());
//		order.setServices(services);
//		orderSv.save(order);
//		redirectView.setUrl("/dashboard/orders/" + orderId + "/services/");
//		return redirectView;
//	}

	@GetMapping("/logs/")
	public ModelAndView getLogs(ModelAndView modelAndView, Model model) {
		modelAndView.setViewName("/dashboard/views/orders/logs.html");
		List<Order> orders = orderSv.findAll().stream().filter(order -> order.getStatus().equalsIgnoreCase("EXPIRED"))
				.collect(Collectors.toList());
		model.addAttribute("orders", orders);

		List<Service> services = serviceSv.findAll();
		model.addAttribute("services", services);

		List<RoomOrder> roomOrders = new ArrayList<RoomOrder>();
		List<ServiceOrder> serviceOrders = new ArrayList<ServiceOrder>();
		for (Order order : orders) {
			roomOrders = roomOrderSv.findByOrderId(order.getId());
			serviceOrders = serviceOrderSv.findByOrderId(order.getId());
		}

		model.addAttribute("roomOrders", roomOrders);
		model.addAttribute("serviceOrders", serviceOrders);
		return modelAndView;
	}
	
	
	
	@GetMapping("/delete/{id}")
	public Object delteOrder(@PathVariable int id, RedirectView redirectView) {
		
		Order order = orderSv.findById(id);
		
		 if (order == null) {
		        redirectView.addStaticAttribute("response_msg", "Không tìm thấy đơn hàng");
		        return redirectView;
		    }

		try {
			
			orderSv.deleteById(order.getId());
			redirectView.addStaticAttribute("response_msg", "Đã xóa thành công");
		}catch (Exception e) {
			// TODO: handle exception
			 e.printStackTrace();
			redirectView.addStaticAttribute("response_msg", "Xóa thất bại");
		}
		redirectView.setUrl("/dashboard/orders/logs/");
		return redirectView;
	}
	

	// @GetMapping("/actions/")
	// public RedirectView getActions(RedirectView redirectView, RedirectAttributes
	// redirectAttributes,
	// @RequestParam(name = "type") String type, @RequestParam(name = "id") int id)
	// {
	// Order order = orderSv.findById(id);
	// redirectView.setUrl("/dashboard/orders/");
	// try {
	// switch (type.toLowerCase()) {
	// case "to-living":
	// order.setStatus("LIVING");
	// orderSv.save(order);
	// break;
	// case "to-expired":
	// order.setStatus("EXPIRED");
	// orderSv.save(order);
	// break;
	// case "remove":
	// orderSv.deleteById(id);
	// break;
	// default:
	// break;
	// }
	// redirectAttributes.addFlashAttribute("response_msg", "Thành công");
	// } catch (Exception exception) {
	// redirectAttributes.addFlashAttribute("response_msg", exception.getMessage());
	// }
	// return redirectView;
	// }

}
