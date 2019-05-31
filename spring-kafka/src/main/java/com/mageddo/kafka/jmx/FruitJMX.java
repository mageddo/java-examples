package com.mageddo.kafka.jmx;

import com.mageddo.kafka.service.FruitService;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@ManagedResource
@Component
public class FruitJMX {

	private final FruitService fruitService;

	public FruitJMX(FruitService fruitService) {
		this.fruitService = fruitService;
	}

	@ManagedOperation(description = "dasdasdas")
	public String postOnKafkaPreCommitAndRollback(){
		fruitService.postOnKafkaPreCommitAndRollback();
		return "success";
	}

	@ManagedOperation(description = "asdasdasd")
	public String postOnKafkaAfterCommitAndRollback(){
		fruitService.postOnKafkaAfterCommitAndRollback();
		return "success";
	}

}
