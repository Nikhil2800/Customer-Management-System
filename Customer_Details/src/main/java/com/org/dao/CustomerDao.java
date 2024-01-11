package com.org.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.entities.Customer;

@Repository
public interface CustomerDao extends JpaRepository<Customer, Long> {
	
//	 @Query("SELECT c FROM Customer c WHERE " +
//	           "(:searchBy = 'firstname' AND c.firstname = :query) OR " +
//	           "(:searchBy = 'city' AND c.city = :query) OR " +
//	           "(:searchBy = 'email' AND c.email = :query) OR " +
//	           "(:searchBy = 'phone' AND c.phone = :query)")
//	    List<Customer> findByDynamicSearch(@Param("searchBy") String searchBy, @Param("query") String query);
//	 
	 @Query("SELECT c FROM Customer c WHERE LOWER(c.firstname) LIKE LOWER(CONCAT('%', :partialFirstName, '%'))")
	    List<Customer> findByFirstNameContaining(@Param("partialFirstName") String partialFirstName);

	    @Query("SELECT c FROM Customer c WHERE LOWER(c.city) LIKE LOWER(CONCAT('%', :partialCity, '%'))")
	    List<Customer> findByCityContaining(@Param("partialCity") String partialCity);

	    @Query("SELECT c FROM Customer c WHERE LOWER(c.email) LIKE LOWER(CONCAT('%', :partialEmail, '%'))")
	    List<Customer> findByEmailContaining(@Param("partialEmail") String partialEmail);

	    @Query("SELECT c FROM Customer c WHERE c.phone = :phone")
	    List<Customer> findByPhone(@Param("phone") String phone);

}
