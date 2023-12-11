package com.shardingsphere.infraestructure.database.dao;

import com.shardingsphere.infraestructure.database.mapping.CustomerDaoMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerDao extends JpaRepository<CustomerDaoMap, UUID> {
}
