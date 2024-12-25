package com.n3fpoly.hotel_rental.common.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "service_order")
public class ServiceOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "name", length = 500, nullable = false)
	private String name;
	
	@Column(name = "price", nullable = false)
	private int price;
	
	@Column(name = "quantity", nullable = false, length = 11)
	private int quantity;
	
	@Column(name = "note", length = 500, nullable = true)
	private String note;
	
	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;
	
	@ManyToOne
	@JoinColumn(name = "service_id", nullable = true)
	private Service service;
}
