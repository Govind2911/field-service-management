package com.FieldServiceManagement.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.FieldServiceManagement.Entity.Customer;
import com.FieldServiceManagement.Service.CustomerService;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    
	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/create")
	public ResponseEntity<Customer>createCustomer(@RequestBody Customer customer){
		return ResponseEntity.ok(customerService.createCustomer(customer));
		
	}
	
	@PutMapping("/update/{email}")
	public ResponseEntity<Customer>updateCustomer(@PathVariable String email, @RequestBody Customer customer){
		return ResponseEntity.ok(customerService.updateCustomer(email, customer));
	}
	
	@GetMapping("/{email}")
	public ResponseEntity<Customer>getCustomerEmail(@PathVariable String email){
		return ResponseEntity.ok(customerService.getCustomerByEmail(email));
		
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Customer>> getAllCustomer(){
		return ResponseEntity.ok(customerService.getAllCustomer());
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String>deleteCustomer(@PathVariable Long id){
		customerService.deleteCustomer(id);
		return ResponseEntity.ok("Customer Deleted Successfully");
	}
}
