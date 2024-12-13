package com.medicalsupplies.medical.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medicalsupplies.medical.models.Cart;
import com.medicalsupplies.medical.models.CartItem;
import com.medicalsupplies.medical.models.Response;
import com.medicalsupplies.medical.repository.CartRepository;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartRepository repository;

    // Add to Cart
    @PostMapping(path = "/add")
    public ResponseEntity<Response> addToCart(@RequestBody Cart user) {
        try {
            // Calculate the total price based on items
            double total = user.getCartitems().stream()
                    .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                    .sum();
            user.setCartTotal(total);

            repository.save(user); // Save cart to DB
            Response response = new Response(101, "Cart ADDED Successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exception) {
            Response response = new Response(701, "Cart Not Saved Successfully. Exception: " + exception.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Remove from Cart
    @PostMapping(path = "/remove")
    public ResponseEntity<Response> removeFromCart(@RequestParam Integer productId) {
        try {
            Cart userCart = repository.findById((int) 1L).orElseThrow(); // Assume we retrieve the cart based on the user
            userCart.getCartitems().removeIf(item -> item.getProdName().equals(productId));

            // Recalculate total after removal
            double total = userCart.getCartitems().stream()
                    .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                    .sum();
            userCart.setCartTotal(total);

            repository.save(userCart);
            Response response = new Response(101, "Item removed from cart");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exception) {
            Response response = new Response(701, "Item removal failed. Exception: " + exception.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update quantity in Cart
    @PostMapping(path = "/update")
    public ResponseEntity<Response> updateQuantity(@RequestParam Integer productId, @RequestParam int quantity) {
        try {
            Cart userCart = repository.findById((int) 1L).orElseThrow(); // Assume we retrieve the cart based on the user
            CartItem itemToUpdate = userCart.getCartitems().stream()
                    .filter(item -> item.getProdName().equals(productId))
                    .findFirst()
                    .orElseThrow();

            itemToUpdate.setQuantity(quantity);

            // Recalculate total after quantity update
            double total = userCart.getCartitems().stream()
                    .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                    .sum();
            userCart.setCartTotal(total);

            repository.save(userCart);
            Response response = new Response(101, "Quantity updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exception) {
            Response response = new Response(701, "Quantity update failed. Exception: " + exception.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

