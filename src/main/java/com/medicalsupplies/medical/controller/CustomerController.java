package com.medicalsupplies.medical.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.medicalsupplies.medical.models.Customer;
import com.medicalsupplies.medical.models.Response;
import com.medicalsupplies.medical.repository.CustomerRepository;


@Controller
@RequestMapping(path = "/customer")
public class CustomerController {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	
    @Autowired
   CustomerRepository repository;
    
    @PostMapping(path = "/add")
    public ResponseEntity<Response> addUser(
            @RequestParam String custFullName,
            @RequestParam String custAge,
            @RequestParam String custAddress,
            @RequestParam String custEmail,
            @RequestParam String custPhone,
            @RequestParam String custPassword) {

        // Input validation
        if (custFullName == null || custFullName.isEmpty() || 
            custEmail == null || custEmail.isEmpty() || 
            custPassword == null || custPassword.isEmpty()) {
            return new ResponseEntity<>(new Response(400, "Invalid input: Full name, email, and password are required."), HttpStatus.BAD_REQUEST);
        }

        Customer user = new Customer(null, custFullName, custAge, custAddress, custEmail, custPhone, custPassword);
        logger.info("User: {}", user);

        try {
            repository.save(user);
            Response response = new Response(201, "User " + custFullName + " saved successfully.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception exception) {
            logger.error("Error saving user: {}", exception.getMessage());
            Response response = new Response(500, "Error saving user: " + exception.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
  
    @PostMapping("/loginRequest")
    public ResponseEntity<?> login(@RequestParam("email") String email, @RequestParam("custPassword") String custPassword) {
        // Fetch user by email
        Customer user = repository.findByEmail(email);;

        if (user != null && user.getCustPassword().equals(custPassword)) {
            // Return user details on successful login
            return ResponseEntity.ok(user);
        }

        // Return error message for invalid credentials
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }
    
    
    
    @GetMapping("/list")
    public ResponseEntity<List<Customer>> getAllUsers() {
        try {
            List<Customer> userList = repository.findAll();
            return new ResponseEntity<>(userList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
   
    
    /**
    @GetMapping("/editUser/{id}")
    public String editUser(@PathVariable Integer id, Model model) {
        // Find the user by ID
        User user = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        
        // Add user to the model to pre-populate the form
        model.addAttribute("user", user);
        
        return "editUser"; // Replace with the name of your edit form template
    }

    // Method to update the user details
    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute User user) {
        // Save the updated user to the database
        repository.save(user);
        
        return "redirect:/users"; // Redirect to the list of users after updating
    }
    
    **/

    // Method to delete a user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
 
    
  
}
