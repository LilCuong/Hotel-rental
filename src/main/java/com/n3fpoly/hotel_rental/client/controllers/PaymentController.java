package com.n3fpoly.hotel_rental.client.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.n3fpoly.hotel_rental.common.models.Cart;
import com.n3fpoly.hotel_rental.common.models.CartItem;
import com.n3fpoly.hotel_rental.common.models.Order;
import com.n3fpoly.hotel_rental.common.models.Room;
import com.n3fpoly.hotel_rental.common.models.RoomOrder;
import com.n3fpoly.hotel_rental.common.models.Service;
import com.n3fpoly.hotel_rental.common.models.ServiceOrder;
import com.n3fpoly.hotel_rental.common.services.CartItemSv;
import com.n3fpoly.hotel_rental.common.services.CartSv;
import com.n3fpoly.hotel_rental.common.services.OrderSv;
import com.n3fpoly.hotel_rental.common.services.RoomOrderSv;
import com.n3fpoly.hotel_rental.common.services.RoomSv;
import com.n3fpoly.hotel_rental.common.services.ServiceOrderSv;
import com.n3fpoly.hotel_rental.common.services.ServiceSv;
import com.n3fpoly.hotel_rental.common.utils.Session;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/client/payment")
public class PaymentController {

	@Autowired
	CartSv cartSv;
	@Autowired
	CartItemSv cartItemSv;
	@Autowired
	RoomOrderSv roomOrderSv;
	@Autowired
	ServiceOrderSv serviceOrderSv;
	@Autowired
	OrderSv orderSv;
	@Autowired
	Session session;
	@Autowired
	ServiceSv serviceSv;
	@Autowired
	RoomSv roomSv;

	@GetMapping("/")
	public ModelAndView getIndex(ModelAndView modelAndView) {
		modelAndView.setViewName("/client/views/payment.html");
		return modelAndView;
	}

	@GetMapping("/to-payment")
	public Object toPay(ModelAndView modelAndView, RedirectAttributes redirectAttributes, RedirectView redirectView) {
		Cart cart = cartSv.findByAccountId(session.getLoggedInAccount().getId());
		List<CartItem> cartItems = cartItemSv.findByCartId(cart.getId());

		for (CartItem cartItem : cartItems) {
			if (cartItem.getRoom() != null) {

				Room room = roomSv.findById(cartItem.getRoom().getId());

				if (room.getStatus().equals("NOT_EMPTY")) {
					redirectAttributes.addFlashAttribute("danger", "Phòng " + room.getName() + " đang không trống!");
					redirectView.setUrl("/client/cart/");
					return redirectView;
				}

			}
		}

		modelAndView.setViewName("/client/views/payment.html");
		return modelAndView;
	}

	@GetMapping("/payment")
	public RedirectView payment(RedirectView redirectView, RedirectAttributes redirectAttributes) {
		Cart cart = cartSv.findByAccountId(session.getLoggedInAccount().getId());
		List<CartItem> cartItems = cartItemSv.findByCartId(cart.getId());

		int total = 0;
		for (CartItem cartItem : cartItems) {
			if (cartItem.getRoom() != null) {
				total = total + (cartItem.getRoom().getPrice() * cartItem.getQuantity());
			}
			if (cartItem.getService() != null) {
				total = total + (cartItem.getService().getPrice() * cartItem.getQuantity());
			}

		}

		if (cartItems.size() == 0) {
			redirectAttributes.addFlashAttribute("danger", "Không tìm thấy đơn hàng nào để thanh toán!");
			redirectView.setUrl("/client/cart/");
			return redirectView;
		}
		try {
			Order order = new Order();
			order.setAccount(session.getLoggedInAccount());
			order.setStatus("WAITING");
			order.setStartDate(cart.getStartDate());
			order.setEndDate(cart.getEndDate());
			order.setTotal(total);
			orderSv.save(order);
			for (CartItem cartItem : cartItems) {
				if (cartItem.getRoom() != null) {

					Room room = roomSv.findById(cartItem.getRoom().getId());

					if (room.getStatus().equals("NOT_EMPTY")) {
						redirectAttributes.addFlashAttribute("danger", "Phòng đang không trống!");
						redirectView.setUrl("/client/cart/");
						return redirectView;
					}

					room.setStatus("NOT_EMPTY");
					roomSv.save(room);

					RoomOrder roomOrder = new RoomOrder();
					roomOrder.setOrder(order);
					roomOrder.setName(cartItem.getRoom().getName());
					roomOrder.setPrice(cartItem.getRoom().getPrice());
					roomOrder.setType(cartItem.getRoom().getType());
					roomOrder.setQuantity(1);
					roomOrder.setRoom(cartItem.getRoom());
					roomOrderSv.save(roomOrder);

				}
				if (cartItem.getService() != null) {
					ServiceOrder serviceOrder = new ServiceOrder();
					serviceOrder.setOrder(order);
					serviceOrder.setName(cartItem.getService().getName());
					serviceOrder.setPrice(cartItem.getService().getPrice());
					serviceOrder.setQuantity(cartItem.getQuantity());
					serviceOrder.setService(cartItem.getService());
					serviceOrder.setNote(cartItem.getNote());
					serviceOrderSv.save(serviceOrder);
				}

				cartItemSv.deleteById(cartItem.getId());
			}

			redirectView.setUrl("/client/logs/");
			redirectAttributes.addFlashAttribute("success", "Đặt phòng thành công!");
			return redirectView;
		} catch (Exception e) {
			redirectView.setUrl("/client/payment/");
			redirectAttributes.addFlashAttribute("danger", "Đặt phòng thất bại!");
			return redirectView;
		}

	}

	@PostMapping("/service-pay")
	public Object servicePay(@RequestParam("serviceId") List<Long> serviceIds,
			@RequestParam("quantity") List<Integer> quantities, @RequestParam("orderId") Long orderId,
			RedirectView redirectView, ModelAndView modelAndView, RedirectAttributes redirectAttributes) {
		Map<Long, Integer> serviceMap = new HashMap<>();

		for (int i = 0; i < serviceIds.size(); i++) {
			Long serviceId = serviceIds.get(i);
			Integer quantity = quantities.get(i);
			serviceMap.put(serviceId, quantity);
		}

		Order order = orderSv.findById(orderId);
		int serviceTotal = 0;

		for (Map.Entry<Long, Integer> entry : serviceMap.entrySet()) {
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

		redirectAttributes.addFlashAttribute("success", "Thêm dịch vụ thành công!");
		redirectView.setUrl("/client/logs/");
		return redirectView;

	}

}
