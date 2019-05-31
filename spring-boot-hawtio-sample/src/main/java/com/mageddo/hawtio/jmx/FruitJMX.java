package com.mageddo.hawtio.jmx;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource
public class FruitJMX {

	@ManagedOperation
	public String createFruit(String name){
		return "fruit created: " +  name;
	}
}
