package com.n3fpoly.hotel_rental.common.models;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.CascadeType;
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
@Table(name = "cart_item")
public class CartItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "quantity", nullable = false)
	private int quantity;
	
	@Column(name = "note", length = 500, nullable = true)
	private String note;
	
	@ManyToOne
	@JoinColumn(name = "room_id", nullable = true)
	private Room room;
	
	@ManyToOne
	@JoinColumn(name = "service_id", nullable = true)
	private Service service;
	
	
	@ManyToOne
	@JoinColumn(name = "cart_id", nullable = true)
	private Cart cart;
	

}
