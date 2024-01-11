package com.org.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.entities.Customer;
import com.org.services.CustomerService;

@Controller
public class CustomerController {
	
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    
    @GetMapping("/")
    public String login() {
    	return "login";
    }
	
	@GetMapping("/customers")
	public String getAllCustomer(Model model) {
		model.addAttribute("customers", customerService.getAllCustomer());
		return "customers";
	}
	
	@GetMapping("/customers/new")
	public String addCustomer(Model model) {
		Customer customer = new Customer();
		model.addAttribute("customer",customer);
		return "create_customer";
		
	}
	
	
	@PostMapping("/customers")
	public String saveStudent(@ModelAttribute("customer") Customer customer) {
		customerService.addCustomer(customer);
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/edit/{id}")
	public String editStudentForm(@PathVariable Long id, Model model) {
		model.addAttribute("customer", customerService.getCustomer(id));
		return "edit-customer";
	}

	@PostMapping("/customers/{id}")
	public String updateStudent(@PathVariable Long id,
			@ModelAttribute("student") Customer customer,
			Model model) {
		
		// get student from database by id
		Customer existingStudent = customerService.getCustomer(id);
		existingStudent.setId(id);
		existingStudent.setFirstname(customer.getFirstname());
		existingStudent.setLastname(customer.getLastname());
		existingStudent.setAddress(customer.getAddress());
		existingStudent.setCity(customer.getCity());
		existingStudent.setState(customer.getState());
		existingStudent.setEmail(customer.getEmail());
		existingStudent.setPhone(customer.getPhone());
		
		// save updated student object
		customerService.updateCustomer(existingStudent);
		return "redirect:/customers";		
	}
	
	// handler method to handle delete student request
	
	@GetMapping("/customers/{id}")
	public String deleteStudent(@PathVariable Long id) {
		customerService.deleteCustomer(id);
		return "redirect:/customers";
	}

	@GetMapping("/customers/searchByFirstName")
    public String searchCustomersByFirstName(
            @RequestParam(name = "partialFirstName") String partialFirstName,
            Model model) {
        List<Customer> searchResults = customerService.searchCustomersByFirstName(partialFirstName);
        model.addAttribute("customers", searchResults);
        return "search_results";
    }

    @GetMapping("/customers/searchByCity")
    public String searchCustomersByCity(
            @RequestParam(name = "partialCity") String partialCity,
            Model model) {
        List<Customer> searchResults = customerService.searchCustomersByCity(partialCity);
        model.addAttribute("customers", searchResults);
        return "search_results";
    }

    @GetMapping("/customers/searchByEmail")
    public String searchCustomersByEmail(
            @RequestParam(name = "partialEmail") String partialEmail,
            Model model) {
        List<Customer> searchResults = customerService.searchCustomersByEmail(partialEmail);
        model.addAttribute("customers", searchResults);
        return "search_results";
    }

    @GetMapping("/customers/searchByPhone")
    public String searchCustomersByPhone(
            @RequestParam(name = "phone") String phone,
            Model model) {
        List<Customer> searchResults = customerService.searchCustomersByPhone(phone);
        model.addAttribute("customers", searchResults);
        return "search_results";
    }

    @GetMapping("/customers/searchBy")
    public String searchCustomers(
            @RequestParam(name = "searchBy") String searchBy,
            @RequestParam(name = "query") String query,
            Model model) {
        List<Customer> searchResults = customerService.searchCustomers(searchBy, query);
        model.addAttribute("customers", searchResults);
        return "customers";
    }
    
   
    ////////////////// Authentication
    
    @PostMapping("/authenticate")
    public String authenticateUser(Model model, @RequestParam("name") String name, @RequestParam("pass") String pass) {
        // Call the authentication API and obtain the Bearer token
        String token = authenticateUser("test@sunbasedata.com", "Test@123");

        // Store the token in the model for use in subsequent API calls
        model.addAttribute("token", token);
       System.out.println("############### Authenticate Response : "+ token);

     
    	   return "redirect:/customers"; // Redirect to the customer list page
    }

    @GetMapping("/customers/list")
    public String getCustomerList(@RequestParam(name = "cmd", defaultValue = "get_customer_list") String cmd,
                                  @RequestParam(name = "token") String token,
                                  Model model) {
        // Call the customer list API with the Bearer token
        List<Customer> customerList = getCustomerList(cmd, token);

        // Add the customer list to the model for rendering in the view
        model.addAttribute("customers", customerList);

        return "customers"; // Assuming you have a customers.html template
    }

    // Existing code...

    private String authenticateUser(String loginId, String password) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{\"login_id\":\"" + loginId + "\",\"password\":\"" + password + "\"}"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Assuming the token is returned in the response body
                return response.body();
            } else {
                throw new RuntimeException("Authentication failed with status code: " + response.statusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during authentication", e);
        }
    }

    private List<Customer> getCustomerList(String cmd, String token) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=" + cmd))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Assuming you have a method to parse the JSON response into a List<Customer>
                return parseCustomerList(response.body());
            } else {
                throw new RuntimeException("Failed to get customer list with status code: " + response.statusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while getting customer list", e);
        }
    }
    
    private List<Customer> parseCustomerList(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, new TypeReference<List<Customer>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error parsing customer list JSON", e);
        }
    }


}
