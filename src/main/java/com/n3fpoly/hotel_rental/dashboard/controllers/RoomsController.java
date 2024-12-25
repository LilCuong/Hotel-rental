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
import com.n3fpoly.hotel_rental.common.models.Room;
import com.n3fpoly.hotel_rental.common.models.RoomOrder;
import com.n3fpoly.hotel_rental.common.services.CartItemSv;
import com.n3fpoly.hotel_rental.common.services.RoomOrderSv;
import com.n3fpoly.hotel_rental.common.services.RoomSv;

@Controller("dashboard.RoomsController")
@RequestMapping(path = "/dashboard/rooms")
public class RoomsController {
    @Autowired
    private RoomSv roomSv;
    
    @Autowired
    CartItemSv cartItemSv;
    
    @Autowired
    RoomOrderSv roomOrderSv;

    @GetMapping(path = "/")
    public ModelAndView getIndex(ModelAndView modelAndView) {
        modelAndView.setViewName("/dashboard/views/rooms/index.html");
        modelAndView.addObject("objects", roomSv.findAll());
        return modelAndView;
    }

    @GetMapping(path = "/add/")
    public ModelAndView getAddNewAccount(ModelAndView modelAndView) {
        modelAndView.setViewName("/dashboard/views/rooms/add.html");
        return modelAndView;
    }

    @PostMapping(path = "/add/")
    public RedirectView postAddNewAccount(RedirectView redirectView, RedirectAttributes redirectAttributes,
            @RequestParam(name = "name") String name, @RequestParam(name = "price") int price,
            @RequestParam(name = "status") String status, @RequestParam(name = "type") String type) {
        redirectView.setUrl("/dashboard/rooms/add/");
        try {
            Room newRoom = new Room();
            newRoom.setName(name);
            newRoom.setType(type);
            newRoom.setPrice(price);
            newRoom.setStatus(status);
            roomSv.save(newRoom);
            redirectAttributes.addFlashAttribute("response_msg", "Thành công");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("response_msg", exception.getMessage());
        }
        return redirectView;
    }

    @GetMapping(path = "/edit/{id}/")
    public ModelAndView getEditAccount(ModelAndView modelAndView,
            @PathVariable(name = "id") Optional<Long> id) {
        modelAndView.setViewName("/dashboard/views/rooms/edit.html");
        if (id.isPresent()) {
            Room room = roomSv.findById(id.get());
            if (room != null) {
                modelAndView.addObject("object", room);
                return modelAndView;
            }
        }
        modelAndView.setViewName("redirect:/dashboard/rooms/");
        return modelAndView;
    }

    @PostMapping(path = "/edit/{id}/")
    public RedirectView postEditAccount(RedirectView redirectView, RedirectAttributes redirectAttributes,
            @ModelAttribute Room room) {
        redirectView.setUrl("/dashboard/rooms/edit/" + room.getId() + "/");
        try {
            roomSv.save(room);
            redirectAttributes.addFlashAttribute("response_msg", "Thành công");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("response_msg", exception.getMessage());
        }
        return redirectView;
    }

    @GetMapping(path = "/delete")
    public RedirectView getDelete(RedirectView redirectView, RedirectAttributes redirectAttributes,
            @RequestParam("id") int id) {
        try {
        	Room room = roomSv.findById(id);
        	List<CartItem> cartItems = cartItemSv.findByRoomId(room.getId());
        	for(CartItem cartItem : cartItems) {
        		cartItemSv.deleteById(cartItem.getId());
        	}
        	List<RoomOrder> roomOrders = roomOrderSv.findByRoomId(room.getId());
        	for(RoomOrder roomOrder : roomOrders) {
        		roomOrder.setRoom(null);
        		roomOrderSv.save(roomOrder);
        	}
        	
        	
            roomSv.deleteById(room.getId());
            redirectAttributes.addFlashAttribute("response_msg", "Xóa thành công");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("response_msg", exception.getMessage());
        }
        redirectView.setUrl("/dashboard/accounts/");
        return redirectView;
    }
}
