package com.medicalsupplies.medical.controller;

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

import com.medicalsupplies.medical.models.Brand;
import com.medicalsupplies.medical.models.Category;
import com.medicalsupplies.medical.models.Customer;
import com.medicalsupplies.medical.models.Medicine;
import com.medicalsupplies.medical.models.Response;
import com.medicalsupplies.medical.repository.CustomerRepository;
import com.medicalsupplies.medical.repository.MedicineRepository;

@RestController
@Controller
@RequestMapping(path = "/medicine")
public class MedicineController {
	    

	     @Autowired
	     private MedicineRepository repository;

	     // Add Medicine
	     @PostMapping(path = "/add")
	     public ResponseEntity<Response> addMedicine(
	             @RequestParam String prodName,
	             @RequestParam Category category,
	             @RequestParam Brand brand,
	             @RequestParam double unitPrice,
	             @RequestParam int recommendAge,
	             @RequestParam boolean isActive,
	             @RequestParam String prodDescription) {

	         Medicine medicine = new Medicine(null, prodName, brand, category, unitPrice, recommendAge, isActive, prodDescription);
	         try {
	             repository.save(medicine);
	             Response response = new Response(101, "Medicine Saved Successfully");
	             return new ResponseEntity<>(response, HttpStatus.OK);
	         } catch (Exception exception) {
	             Response response = new Response(701, "Medicine Not Saved Successfully. Exception: " + exception.getMessage());
	             return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	         }
	     }

	     // Edit Medicine
	     @PutMapping(path = "/edit/{id}")
	     public ResponseEntity<Response> editMedicine(
	             @PathVariable Integer id,
	             @RequestParam String prodName,
	             @RequestParam Category category,
	             @RequestParam Brand brand,
	             @RequestParam double unitPrice,
	             @RequestParam int recommendAge,
	             @RequestParam boolean isActive,
	             @RequestParam String prodDescription) {

	         Optional<Medicine> optionalMedicine = repository.findById(id);
	         if (optionalMedicine.isPresent()) {
	             Medicine existingMedicine = optionalMedicine.get();
	             existingMedicine.setProdName(prodName);
	             existingMedicine.setCategory(category);
	             existingMedicine.setBrand(brand);
	             existingMedicine.setUnitPrice(unitPrice);
	             existingMedicine.setRecommendAge(recommendAge);
	             existingMedicine.setActive(isActive);
	             existingMedicine.setProdDescription(prodDescription);

	             try {
	                 repository.save(existingMedicine);
	                 Response response = new Response(102, "Medicine Updated Successfully");
	                 return new ResponseEntity<>(response, HttpStatus.OK);
	             } catch (Exception exception) {
	                 Response response = new Response(702, "Medicine Not Updated Successfully. Exception: " + exception.getMessage());
	                 return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	             }
	         } else {
	             Response response = new Response(703, "Medicine Not Found");
	             return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	         }
	     }

	     // Delete Medicine
	     @DeleteMapping(path = "/delete/{id}")
	     public ResponseEntity<Response> deleteMedicine(@PathVariable Integer id) {
	         Optional<Medicine> optionalMedicine = repository.findById(id);
	         if (optionalMedicine.isPresent()) {
	             try {
	                 repository.deleteById(id);
	                 Response response = new Response(103, "Medicine Deleted Successfully");
	                 return new ResponseEntity<>(response, HttpStatus.OK);
	             } catch (Exception exception) {
	                 Response response = new Response(704, "Medicine Not Deleted Successfully. Exception: " + exception.getMessage());
	                 return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	             }
	         } else {
	             Response response = new Response(705, "Medicine Not Found");
	             return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	         }
	     }

	     // List all Medicines
	     @GetMapping(path = "/all")
	     public ResponseEntity<List<Medicine>> getAllMedicines() {
	         try {
	             List<Medicine> medicines = repository.findAll();
	             return new ResponseEntity<>(medicines, HttpStatus.OK);
	         } catch (Exception exception) {
	             return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	         }
	     }
	 }

