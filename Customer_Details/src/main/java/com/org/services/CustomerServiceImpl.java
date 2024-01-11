package com.org.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.dao.CustomerDao;
import com.org.entities.Customer;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerDao customerDao;

	@Override
	public List<Customer> getAllCustomer() {
		// TODO Auto-generated method stub
		return customerDao.findAll();
	}

	@Override
	public Customer getCustomer(long customerid) {
		// TODO Auto-generated method stub
		return customerDao.getById(customerid);
	}

	@Override
	public Customer addCustomer(Customer customer) {
		// TODO Auto-generated method stub
		customerDao.save(customer);
		return customer;
	}

	@Override
	public Customer updateCustomer(Customer customer) {
		// TODO Auto-generated method stub
		customerDao.save(customer);
		return customer;
	}

	@Override
	public void deleteCustomer(long customerid) {
		// TODO Auto-generated method stub
		customerDao.deleteById(customerid);
		
	}

	@Override
    public List<Customer> searchCustomersByFirstName(String partialFirstName) {
        return customerDao.findByFirstNameContaining(partialFirstName);
    }

    @Override
    public List<Customer> searchCustomersByCity(String partialCity) {
        return customerDao.findByCityContaining(partialCity);
    }

    @Override
    public List<Customer> searchCustomersByEmail(String partialEmail) {
        return customerDao.findByEmailContaining(partialEmail);
    }

    @Override
    public List<Customer> searchCustomersByPhone(String phone) {
        return customerDao.findByPhone(phone);
    }

    @Override
    public List<Customer> searchCustomers(String searchBy, String query) {
        if (!Arrays.asList("firstname", "city", "email", "phone").contains(searchBy)) {
            throw new IllegalArgumentException("Invalid searchBy parameter");
        }
        System.out.println("Search By: " + searchBy);
        System.out.println("Query: " + query);

        switch (searchBy) {
            case "firstname":
                return customerDao.findByFirstNameContaining(query);
            case "city":
                return customerDao.findByCityContaining(query);
            case "email":
                return customerDao.findByEmailContaining(query);
            case "phone":
                // Assuming phone is a unique field, returning a list for consistency
                return customerDao.findByPhone(query);
            default:
                throw new IllegalArgumentException("Invalid searchBy parameter");
        }
    }

}
