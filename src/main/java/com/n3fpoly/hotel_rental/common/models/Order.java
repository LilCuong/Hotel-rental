package com.n3fpoly.hotel_rental.common.models;

import java.util.Date;
import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(name = "start_date", nullable = true)
	private Date startDate;
	
	@Column(name = "end_date", nullable = true)
	private Date endDate;
	
	@Column(name = "total", nullable = true)
	private int total;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

	/*
	 * @ManyToOne(fetch = FetchType.EAGER)
	 * 
	 * @JoinColumn(name = "room_id", nullable = false) private Room room;
	 */

    @Column(name = "status", nullable = false, length = 255)
    private String status;

	/*
	 * @ManyToMany(fetch = FetchType.EAGER)
	 * 
	 * @JoinTable(name = "orders_services", joinColumns = @JoinColumn(name =
	 * "order_id"), inverseJoinColumns = @JoinColumn(name = "service_id")) private
	 * List<Service> services;
	 * 
	 * public int getTotalPrice() { return room.getPrice() +
	 * services.stream().mapToInt(service -> service.getPrice()).sum(); }
	 */
    
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<RoomOrder> roomOrders;
    
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<ServiceOrder> serviceOrders;
}
