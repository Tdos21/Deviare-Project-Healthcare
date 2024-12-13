package com.medicalsupplies.medical.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medicalsupplies.medical.models.CartItem;
import com.medicalsupplies.medical.models.Customer;
import com.medicalsupplies.medical.models.Orders;
import com.medicalsupplies.medical.models.Response;
import com.medicalsupplies.medical.repository.OrdersRepository;


@RestController
@RequestMapping(path = "/order")
public class OrdersController {
	
	@Autowired
	   OrdersRepository repository;
	    
	@PostMapping(path = "/add")
	public ResponseEntity<Response> addOrders(
	        @RequestParam double orderTotal,
	        @RequestParam LocalDate pay_Date,
	        @RequestParam int itemsQuantity,
	        @RequestParam Customer customer,
	        @RequestParam Orders orders,
	        @RequestParam List<CartItem> items
	) {
	    Orders user = new Orders(null, orderTotal, pay_Date, itemsQuantity, customer, orders, items);
	    System.out.println("Orders: " + user);

	    try {
	        repository.save(user);
	        Response response = new Response(101, "Order created Successfully");
	        return new ResponseEntity<Response>(response, HttpStatus.OK);
	    } catch (Exception exception) {
	        Response response = new Response(701, "Order Not Successfully. Exception: " + exception.getMessage());
	        return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	
	@GetMapping("/getAll")
	public ResponseEntity<List<Orders>> getAllOrders() {
	    try {
	        List<Orders> orders = repository.findAll();
	        return new ResponseEntity<>(orders, HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	
	@PutMapping("/edit/{id}")
	public ResponseEntity<Response> editOrder(
	        @PathVariable Integer id,
	        @RequestParam double orderTotal,
	        @RequestParam LocalDate pay_Date,
	        @RequestParam int itemsQuantity,
	        @RequestParam Customer customer,
	        @RequestParam Orders orders,
	        @RequestParam List<CartItem> items
	) {
	    Optional<Orders> existingOrder = repository.findById(id);
	    if (existingOrder.isPresent()) {
	        Orders order = existingOrder.get();
	        order.setOrderTotal(orderTotal);
	        order.setOrder_Date(pay_Date);
	        order.setItemsQuantity(itemsQuantity);
	        order.setCustomer(customer);
	       
	        order.setItems(items);

	        try {
	            repository.save(order);
	            Response response = new Response(102, "Order updated successfully");
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        } catch (Exception exception) {
	            Response response = new Response(702, "Order update failed. Exception: " + exception.getMessage());
	            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    } else {
	        Response response = new Response(703, "Order not found");
	        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    }
	}

	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Response> deleteOrder(@PathVariable Integer id) {
	    try {
	        repository.deleteById(id);
	        Response response = new Response(104, "Order deleted successfully");
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    } catch (Exception exception) {
	        Response response = new Response(704, "Failed to delete order. Exception: " + exception.getMessage());
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


}
