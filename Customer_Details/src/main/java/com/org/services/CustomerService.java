package com.org.services;

import java.util.List;

import com.org.entities.Customer;

public interface CustomerService {
	
	public List<Customer> getAllCustomer();
	
	public Customer getCustomer(long customerid);
	
	public Customer addCustomer(Customer customer);
	
	public Customer updateCustomer(Customer customer);
	
	public void deleteCustomer(long customerid);
	
	 List<Customer> searchCustomersByFirstName(String firstName);

	 List<Customer> searchCustomersByCity(String city);

	  List<Customer> searchCustomersByEmail(String email);

	  List<Customer> searchCustomersByPhone(String phone);

	  List<Customer> searchCustomers(String searchBy, String query);


}
