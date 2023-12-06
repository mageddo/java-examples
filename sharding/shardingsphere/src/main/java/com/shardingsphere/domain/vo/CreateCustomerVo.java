package com.shardingsphere.domain.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
@Builder
public class CreateCustomerVo {
    @NonNull
    private UUID customerCode;
}
