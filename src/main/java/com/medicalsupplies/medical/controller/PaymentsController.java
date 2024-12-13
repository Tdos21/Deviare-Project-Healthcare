package com.medicalsupplies.medical.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medicalsupplies.medical.models.Customer;
import com.medicalsupplies.medical.models.Orders;
import com.medicalsupplies.medical.models.Payment;
import com.medicalsupplies.medical.models.Response;

import com.medicalsupplies.medical.repository.PaymentRepository;


@RestController
@RequestMapping("/payment")
public class PaymentsController {

    @Autowired
    private PaymentRepository repository;

    // Add payment
    @PostMapping(path = "/add")
    public ResponseEntity<Response> addPayment(
            @RequestParam double totalPaySuccess,
            @RequestParam LocalDate pay_Date,
            @RequestParam String payStatus,
            @RequestParam Customer customer,
            @RequestParam Orders orders
    ) {
        Payment payment = new Payment(null, totalPaySuccess, pay_Date, payStatus, customer, orders);
        System.out.println("Payment: " + payment);

        try {
            repository.save(payment);
            Response response = new Response(101, "Payment created successfully");
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        } catch (Exception exception) {
            Response response = new Response(701, "Payment creation failed. Exception: " + exception.getMessage());
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete payment by ID
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Response> deletePayment(@PathVariable Integer id) {
        try {
            Optional<Payment> payment = repository.findById(id);
            if (payment.isPresent()) {
                repository.delete(payment.get());
                Response response = new Response(101, "Payment deleted successfully");
                return new ResponseEntity<Response>(response, HttpStatus.OK);
            } else {
                Response response = new Response(404, "Payment not found");
                return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            Response response = new Response(701, "Deletion failed. Exception: " + exception.getMessage());
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
