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
@Table(name = "room_order")
public class RoomOrder {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "type", nullable = false, length = 255)
	private String type;

	@Column(name = "price", nullable = false, length = 11)
	private int price;
	
	@Column(name = "quantity", nullable = false, length = 11)
	private int quantity;
	
	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;
	
	@ManyToOne
	@JoinColumn(name = "room_id", nullable = true)
	private Room room;

}
