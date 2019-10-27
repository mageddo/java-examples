package com.mageddo.zipkin.customer.jmx;

import com.mageddo.zipkin.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource
@RequiredArgsConstructor
public class CustomerJMX {

	private final CustomerService customerService;

	@ManagedOperation
	public String buyAChair(){
		customerService.orderAChair();
		return "chair ordered";
	}
}
