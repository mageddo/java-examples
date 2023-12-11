package com.shardingsphere.application;

import com.shardingsphere.domain.vo.CreateCustomerVo;

public interface CreateCustomerUseCase {
    void execute(CreateCustomerVo customerVo);
}
