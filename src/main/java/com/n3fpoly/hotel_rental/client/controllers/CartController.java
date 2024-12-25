package com.n3fpoly.hotel_rental.client.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.n3fpoly.hotel_rental.common.models.Cart;
import com.n3fpoly.hotel_rental.common.models.CartItem;
import com.n3fpoly.hotel_rental.common.models.Room;
import com.n3fpoly.hotel_rental.common.models.Service;
import com.n3fpoly.hotel_rental.common.services.AccountSv;
import com.n3fpoly.hotel_rental.common.services.CartItemSv;
import com.n3fpoly.hotel_rental.common.services.CartSv;
import com.n3fpoly.hotel_rental.common.services.OrderSv;
import com.n3fpoly.hotel_rental.common.services.RoomSv;
import com.n3fpoly.hotel_rental.common.services.ServiceSv;
import com.n3fpoly.hotel_rental.common.utils.Session;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping(path = "/client/cart")
public class CartController {

	@Autowired
	AccountSv accountSv;

	@Autowired
	CartSv cartSv;

	@Autowired
	Session session;

	@Autowired
	RoomSv roomSv;

	@Autowired
	OrderSv orderSv;

	@Autowired
	CartItemSv cartItemSv;

	@Autowired
	ServiceSv serviceSv;

	@GetMapping("/")
	public Object getMethodName(ModelAndView modelAndView, RedirectView redirectView, Model model) {

		if (!session.isLoggedIn()) {
			redirectView.setUrl("/auth/login/");
			return redirectView;
		}
		
		
	        
		Cart getCart = cartSv.findByAccountId(session.getLoggedInAccount().getId());
		
		if(getCart == null) {
        	Cart cart = new Cart(); 
        	model.addAttribute("cart", cart);
        }else {
        	model.addAttribute("cart", getCart);
		}
		
		List<CartItem> cartItems = cartItemSv.findByCartId(getCart.getId());
		model.addAttribute("cartItems", cartItems);

		int total = 0;
		for (CartItem cartItem : cartItems) {
			if (cartItem.getRoom() != null) {
				total = total + cartItem.getRoom().getPrice();
			}
			if (cartItem.getService() != null) {
				total = total + (cartItem.getService().getPrice() * cartItem.getQuantity());
			}

		}
		model.addAttribute("total", total);

		modelAndView.addObject("rooms", roomSv.findAll());
		modelAndView.addObject("services", serviceSv.findAll());

		modelAndView.setViewName("/client/views/cart.html");
		return modelAndView;
	}

	@PostMapping("/get-cart")
	public RedirectView getCart(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, RedirectView redirectView) {
		Cart getCart = cartSv.findByAccountId(session.getLoggedInAccount().getId());
		if (!session.isLoggedIn()) {
			redirectView.setUrl("/auth/login/");
			return redirectView;
		}
		if (getCart == null) {
			Cart cart = new Cart();
			cart.setAccount(session.getLoggedInAccount());
			cart.setStartDate(startDate);
			cart.setEndDate(endDate);
			cartSv.save(cart);
			redirectView.setUrl("/client/rooms/");
			return redirectView;
		} else {
			getCart.setStartDate(startDate);
			getCart.setEndDate(endDate);
			cartSv.save(getCart);
			redirectView.setUrl("/client/rooms/");
			return redirectView;
		}

	}
	
	
	@PostMapping("/update-date")
	public RedirectView updateDate(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, RedirectView redirectView, RedirectAttributes redirectAttributes) {
		Cart getCart = cartSv.findByAccountId(session.getLoggedInAccount().getId());
		if (!session.isLoggedIn()) {
			redirectView.setUrl("/auth/login/");
			return redirectView;
		}
		
			getCart.setStartDate(startDate);
			getCart.setEndDate(endDate);
			cartSv.save(getCart);
			redirectView.setUrl("/client/cart/");
			redirectAttributes.addFlashAttribute("success", "Cập nhật ngày thành công!");
			return redirectView;
		

	}

	@GetMapping("/add-room-to-cart/{id}")
	public RedirectView addRoomToCart(@PathVariable int id, RedirectView redirectView,
			RedirectAttributes redirectAttributes) {
		
		if (session.getLoggedInAccount() == null) {
	        redirectAttributes.addFlashAttribute("danger", "Bạn cần đăng nhập để thêm phòng!");
	        redirectView.setUrl("/client/login");
	        return redirectView;
	    }
		
		Cart getCart = cartSv.findByAccountId(session.getLoggedInAccount().getId());
		

		if (getCart == null  || getCart.getStartDate() == null || getCart.getEndDate() == null) {
			redirectAttributes.addFlashAttribute("danger", "Vui lòng chọn ngày đến và ngày đi!");
			redirectView.setUrl("/client/rooms/");
			return redirectView;
		}
		redirectView.setUrl("/client/cart/");
		Room room = roomSv.findById(id);

		CartItem getCartItem = cartItemSv.getCartItemByCartIdAndRoomId(getCart.getId(), room.getId());
		if (getCartItem != null) {
			redirectAttributes.addFlashAttribute("danger", "Phòng này đã tồn tại!");
			return redirectView;
		}

		try {
			CartItem cartItem = new CartItem();
			cartItem.setCart(getCart);
			cartItem.setRoom(room);
			cartItemSv.save(cartItem);
			redirectAttributes.addFlashAttribute("success", "Thêm thành công!");
			return redirectView;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Thêm phòng thất bại!");
			return redirectView;
		}
	}

	@GetMapping("/add-cart-item/{id}")
	public RedirectView addRoom(@PathVariable Long id, RedirectView redirectView) {
		Optional<CartItem> cartItem = cartItemSv.findById(id);
		redirectView.setUrl("/client/cart/");
		try {
			cartItem.get().setQuantity(cartItem.get().getQuantity() + 1);
			cartItemSv.save(cartItem.get());
		} catch (Exception e) {
			redirectView.addStaticAttribute("danger", "Lỗi không thể thên");
		}

		return redirectView;
	}

	@GetMapping("/remove-cart-item/{id}")
	public RedirectView removeRoom(@PathVariable Long id, RedirectView redirectView) {
		Optional<CartItem> cartItem = cartItemSv.findById(id);
		redirectView.setUrl("/client/cart/");
		try {
			cartItem.get().setQuantity(cartItem.get().getQuantity() - 1);
			cartItemSv.save(cartItem.get());
			if (cartItem.get().getQuantity() == 0) {
				cartItemSv.deleteById(id);
			}
		} catch (Exception e) {
			redirectView.addStaticAttribute("danger", "Lỗi không thể thêm!");
		}

		return redirectView;
	}

	@GetMapping("/delete-cart-item/{id}")
	public RedirectView deleteRoom(@PathVariable Long id, RedirectView redirectView) {
		redirectView.setUrl("/client/cart/");
		try {
			cartItemSv.deleteById(id);
		} catch (Exception e) {
			redirectView.addStaticAttribute("danger", "Lỗi không thể thêm!");
		}
		return redirectView;
	}
	
	@GetMapping("/delete-all-cart-item")
	public RedirectView deleteAllCartItem(RedirectView redirectView, RedirectAttributes redirectAttributes) {
		Cart cart = cartSv.findByAccountId(session.getLoggedInAccount().getId());
		try {
			List<CartItem> cartItems = cartItemSv.findByCartId(cart.getId());
			for(CartItem cartItem : cartItems) {
				cartItemSv.deleteById(cartItem.getId());
			}
			
			redirectAttributes.addFlashAttribute("success", "Xóa tất cả thành công!");
			redirectView.setUrl("/client/cart/");
			return redirectView;
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Xóa tất cả thất bại!");
			redirectView.setUrl("/client/cart/");
			return redirectView;
		}
	}

	@GetMapping("/add-service-to-cart/{id}")
	public RedirectView addServiceToCart(@PathVariable Long id, RedirectView redirectView,
			RedirectAttributes redirectAttributes) {
		Cart getCart = cartSv.findByAccountId(session.getLoggedInAccount().getId());
		Service service = serviceSv.findById(id);
		CartItem cartItem = new CartItem();

		CartItem getCartItem = cartItemSv.getCartItemByCartIdAndServiceId(getCart.getId(), service.getId());
		if (getCartItem != null) {
			cartItem = getCartItem;
		}

		try {
			cartItem.setQuantity(cartItem.getQuantity() + 1);
			cartItem.setCart(getCart);
			cartItem.setService(service);
			cartItemSv.save(cartItem);
			redirectAttributes.addFlashAttribute("success", "Thêm dịch vụ thành công!");
			redirectView.setUrl("/client/cart/");
			return redirectView;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Thêm dịch vụ thất bại!");
			redirectView.setUrl("/client/cart/");
			return redirectView;
		}

	}
	
	@PostMapping("/save-service-note")
	public Object postMethodName(@RequestParam("note") String note, @RequestParam("id") Long cartItemId, RedirectAttributes redirectAttributes, RedirectView redirectView) {
		redirectView.setUrl("/client/cart/");
		Optional<CartItem> cartItem = cartItemSv.findById(cartItemId);
		if(cartItem != null) {
			try {
				cartItem.get().setNote(note);
				cartItemSv.save(cartItem.get());
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
