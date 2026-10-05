package com.FieldServiceManagement.Service;

import java.util.List;

import com.FieldServiceManagement.Entity.Customer;

public interface CustomerService {

	Customer createCustomer(Customer customer);
	Customer getCustomer(Long id);
	Customer getCustomerByEmail(String email);
	List<Customer> getAllCustomer();
	void deleteCustomer(Long id);
	Customer updateCustomer(String email, Customer customer);

}
