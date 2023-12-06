package com.shardingsphere.application.impl;

import com.shardingsphere.application.CreateCustomerUseCase;
import com.shardingsphere.domain.vo.CreateCustomerVo;
import com.shardingsphere.infraestructure.database.dao.CustomerDao;
import com.shardingsphere.infraestructure.database.mapping.CustomerDaoMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreateCustomerUseCaseImpl implements CreateCustomerUseCase {

    private final CustomerDao customerDao;

    public CreateCustomerUseCaseImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public void execute(CreateCustomerVo customerVo) {
        try {
            final var customerToPersist = CustomerDaoMap.builder()
                    .customerCode(customerVo.getCustomerCode())
                    .name("CUSTOMER".concat(customerVo.getCustomerCode().toString()))
                    .age((int) (Math.random() * (100 - 1)) + 1)
                    .build();

            this.customerDao.save(customerToPersist);
            log.info("status=success, customerCode={}", customerVo.getCustomerCode());
        } catch (Exception e) {
            log.error("status=error, customerCode={}, exceptionName={}",
                    customerVo.getCustomerCode(), e.getClass().getName(), e);
        }
    }
}
