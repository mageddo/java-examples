package com.shardingsphere.infraestructure.database.mapping;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "`customer`")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDaoMap {

    @Id
    @Column(name = "COD_CUSTOMER")
    private UUID customerCode;

    @Column(name = "NAM_NAME")
    private String name;

    @Column(name = "NUM_AGE")
    private Integer age;
}