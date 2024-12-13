package com.medicalsupplies.medical.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medicalsupplies.medical.models.Category;
import com.medicalsupplies.medical.models.Response;
import com.medicalsupplies.medical.repository.CategoryRepository;


@RestController
@RequestMapping(name="/category")
public class CategoryController {
	
	 @Autowired
	   CategoryRepository repository;
	    
	 @PostMapping(path = "/create")
	    public ResponseEntity<Response> addCategory(
	            @RequestParam String categoryName,
	            @RequestParam LocalDate dateCreated) {
	        
	        Category category = new Category(null, categoryName, dateCreated);
	        System.out.println("Category: " + category);

	        try {
	            repository.save(category);
	            Response response = new Response(101, "Category " + categoryName + " Saved Successfully");
	            return new ResponseEntity<>(response, HttpStatus.OK);

	        } catch (Exception exception) {
	            Response response = new Response(701, "Category " + categoryName + " Not Saved Successfully. Exception: " + exception.getMessage());
	            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	 
	 
	 @PutMapping("/update/{id}")
	    public ResponseEntity<Response> updateCategory(
	            @PathVariable int id,
	            @RequestBody Category updatedCategory) {
	        try {
	            // Check if the category exists
	            Optional<Category> existingCategoryOpt = repository.findById(id);
	            if (existingCategoryOpt.isPresent()) {
	                Category existingCategory = existingCategoryOpt.get();

	                // Update the fields
	                existingCategory.setCatName(updatedCategory.getCatName());
	                existingCategory.setDateCreated(updatedCategory.getDateCreated());

	                // Save the updated entity
	                repository.save(existingCategory);

	                // Return a success response
	                Response response = new Response(102, "Category updated successfully");
	                return new ResponseEntity<>(response, HttpStatus.OK);
	            } else {
	                // If the category does not exist
	                Response response = new Response(404, "Category with ID " + id + " not found");
	                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	            }
	        } catch (Exception exception) {
	            // Handle any exceptions
	            Response response = new Response(702, "Error updating category: " + exception.getMessage());
	            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    // Delete Category (DELETE)
	    @DeleteMapping("/delete/{id}")
	    public ResponseEntity<String> deleteCategory(@PathVariable("id") Integer id) {
	        Optional<Category> category = repository.findById(id);

	        if (category.isPresent()) {
	            repository.deleteById(id);
	            return ResponseEntity.ok("Category with ID " + id + " deleted successfully.");
	        } else {
	            return ResponseEntity.status(404).body("Category not found.");
	        }
	    }

	    // Get All Categories (GET)
	    @GetMapping("/all")
	    public ResponseEntity<List<Category>> getAllCategories() {
	        return ResponseEntity.ok(repository.findAll());  // Fetch all categories from the database
	    }

}
