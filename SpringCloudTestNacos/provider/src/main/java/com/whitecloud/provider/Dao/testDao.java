package com.whitecloud.provider.Dao;

import com.whitecloud.provider.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface testDao extends JpaRepository<Order,Long> {

}
