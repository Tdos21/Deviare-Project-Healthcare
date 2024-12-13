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

import com.medicalsupplies.medical.models.Customer;
import com.medicalsupplies.medical.models.Deliveries;
import com.medicalsupplies.medical.models.Orders;
import com.medicalsupplies.medical.models.Response;
import com.medicalsupplies.medical.repository.DeliveriesRepository;


@Controller
@RequestMapping(path = "/delivery")
public class DeliveriesController {

	 @Autowired
	   DeliveriesRepository repository;
	    
	 @PostMapping(path = "/add")
	 public ResponseEntity<Response> addUser(
	         @RequestParam String delAddress,
	         @RequestParam LocalDate del_Date,
	         @RequestParam Customer customer,
	         @RequestParam Orders orders
	 ) {
	     Deliveries user = new Deliveries(null, delAddress, del_Date, customer, orders);
	     System.out.println("User: " + user);

	     try {
	         repository.save(user);
	         Response response = new Response(101, "Delivery " + delAddress + " Saved Successfully");
	         return new ResponseEntity<Response>(response, HttpStatus.OK);
	     } catch (Exception exception) {
	         Response response = new Response(701, "Delivery " + delAddress + " Not Saved. Exception: " + exception.getMessage());
	         return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	     }
	 }

	 
	 @PutMapping(path = "/edit/{id}")
	 public ResponseEntity<Response> editUser(
	         @PathVariable Integer id,
	         @RequestParam String delAddress,
	         @RequestParam LocalDate del_Date,
	         @RequestParam Customer customer,
	         @RequestParam Orders orders
	 ) {
	     Optional<Deliveries> deliveryOptional = repository.findById(id);
	     
	     if (deliveryOptional.isPresent()) {
	         Deliveries delivery = deliveryOptional.get();
	         delivery.setDelAddress(delAddress);
	         delivery.setDel_Date(del_Date);
	         delivery.setCustomer(customer);
	         delivery.setOrders(orders);

	         try {
	             repository.save(delivery);
	             Response response = new Response(102, "Delivery " + delAddress + " Updated Successfully");
	             return new ResponseEntity<Response>(response, HttpStatus.OK);
	         } catch (Exception exception) {
	             Response response = new Response(702, "Delivery " + delAddress + " Not Updated. Exception: " + exception.getMessage());
	             return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	         }
	     } else {
	         Response response = new Response(703, "Delivery Not Found");
	         return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
	     }
	 }

	 
	 @DeleteMapping(path = "/delete/{id}")
	 public ResponseEntity<Response> deleteUser(@PathVariable Integer id) {
	     Optional<Deliveries> deliveryOptional = repository.findById(id);
	     
	     if (deliveryOptional.isPresent()) {
	         repository.delete(deliveryOptional.get());
	         Response response = new Response(103, "Delivery Deleted Successfully");
	         return new ResponseEntity<Response>(response, HttpStatus.OK);
	     } else {
	         Response response = new Response(704, "Delivery Not Found");
	         return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
	     }
	 }
	 
	 @GetMapping(path = "/all")
	 public ResponseEntity<List<Deliveries>> getAllDeliveries() {
	     try {
	         List<Deliveries> deliveries = repository.findAll();
	         if (deliveries.isEmpty()) {
	             return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // No deliveries found
	         }
	         return new ResponseEntity<>(deliveries, HttpStatus.OK);  // Return all deliveries
	     } catch (Exception exception) {
	         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);  // Error while fetching deliveries
	     }
	 }


}
