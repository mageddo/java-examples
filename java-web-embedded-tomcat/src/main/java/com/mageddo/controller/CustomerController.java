package com.mageddo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mageddo.entity.CustomerEntity;
import com.mageddo.service.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by elvis on 13/08/16.
 */

@WebServlet(urlPatterns = "/customer")
public class CustomerController extends HttpServlet {

	private CustomerService customerService = new CustomerService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		new ObjectMapper()
			.writeValue(resp.getOutputStream(), customerService.findByName(req.getParameter("name")));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		final CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setFirstName(req.getParameter("firstName"));
		customerEntity.setLastName(req.getParameter("lastName"));
		customerService.createCustomer(customerEntity);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			final long customerId = Long.parseLong(req.getParameter("customerId"));
			final double balance = Double.parseDouble(req.getParameter("balance"));
			customerService.updateCustomerBalanceTurnoverAtDB(customerId, balance);
		} catch (Exception e) {
			resp.setStatus(400);
			resp.getWriter().println("Failed to update customer balance: " + e.getMessage());
		}
	}

}
