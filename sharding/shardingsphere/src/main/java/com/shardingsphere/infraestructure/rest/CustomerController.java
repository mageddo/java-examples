package com.shardingsphere.infraestructure.rest;

import com.shardingsphere.application.CreateCustomerUseCase;
import com.shardingsphere.domain.vo.CreateCustomerVo;
import com.shardingsphere.infraestructure.rest.dto.CreateCustomerRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;

    public CustomerController(CreateCustomerUseCase createCustomerUseCase) {
        this.createCustomerUseCase = createCustomerUseCase;
    }

    @PostMapping()
    public ResponseEntity<?> createCustomer(@RequestBody CreateCustomerRequestDto request) {
        final var customer = CreateCustomerVo.builder()
                .customerCode(UUID.fromString(request.getCustomerCode()))
                .build();

        this.createCustomerUseCase.execute(customer);
        return ResponseEntity.ok().body(null);
    }
}
