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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.medicalsupplies.medical.models.Brand;
import com.medicalsupplies.medical.models.Response;
import com.medicalsupplies.medical.repository.BrandRepository;

@Controller
@RequestMapping(path = "/brand")
public class BrandController {
	
    @Autowired
   BrandRepository repository;
    
    @PostMapping(path = "/add")
    public ResponseEntity<Response> addBrand(@RequestBody Brand user) {
        System.out.println("User: " + user);

        try {
            repository.save(user);
            Response response = new Response(101, "Brand " + user.getBrandName() + " Saved Successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception exception) {
            Response response = new Response(701, "Brand " + user.getBrandName() + " Not Saved Successfully. Exception: " + exception.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateBrand(
            @PathVariable Integer id,
            @RequestBody Brand updatedBrand) {
        try {
            // Check if the brand exists
            Optional<Brand> existingBrandOpt = repository.findById(id);
            if (existingBrandOpt.isPresent()) {
                Brand existingBrand = existingBrandOpt.get();

                // Update the fields
                existingBrand.setBrandName(updatedBrand.getBrandName());
                existingBrand.setDateCreated(updatedBrand.getDateCreated());

                // Save the updated entity
                repository.save(existingBrand);

                // Return a success response
                Response response = new Response(102, "Brand updated successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                // If the brand does not exist
                Response response = new Response(404, "Brand with ID " + id + " not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            // Handle any exceptions
            Response response = new Response(702, "Error updating brand: " + exception.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBrand(@PathVariable("id") int id) {
        Optional<Brand> brand = repository.findById(id);
        
        if (brand.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.ok("Brand with ID " + id + " deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Brand not found.");
        }
    }
    
    @GetMapping("/all")
    public List<Brand> getAllBrands() {
        return repository.findAll();  // Fetch all brands from the database
    }

}
