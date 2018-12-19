package com.mageddo.controller;

import com.mageddo.entity.CustomerEntity;
import com.mageddo.service.CustomerService;
import com.mageddo.service.ManyCustomersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by elvis on 13/08/16.
 */

@Controller
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	ManyCustomersService manyCustomersService;

	@RequestMapping(value = "/customer/name/{customer}",method = RequestMethod.GET)
	@ResponseBody
	public List<CustomerEntity> findByName(@PathVariable("customer") String customer){
		return customerService.findByName(customer);
	}

	@RequestMapping(value = "/customer/",method = RequestMethod.POST)
	@ResponseBody
	public void createCustomer(@RequestBody CustomerEntity customer){
		customerService.createCustomer(customer);
	}

	@RequestMapping(value = "/customers/",method = RequestMethod.POST)
	@ResponseBody
	public void createCustomers(@RequestBody List<CustomerEntity> customers){
		manyCustomersService.createCustomers(customers);
	}

	@RequestMapping(value = "/customer/{id}",method = RequestMethod.GET)
	@ResponseBody
	public CustomerEntity findCustomer(@PathVariable("id") long id){
		return customerService.findCustomerByIdSerial(id);
	}

	@RequestMapping(value = "/customer/{id}/{value}",method = RequestMethod.GET)
	@ResponseBody
	public boolean updateBalance(@PathVariable("id") long id, @PathVariable("value") double value){
		return customerService.updateCustomerBalanceConcurrencyProblem(id, value);
	}

}
