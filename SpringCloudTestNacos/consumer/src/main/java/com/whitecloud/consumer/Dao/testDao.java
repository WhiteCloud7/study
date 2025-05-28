package com.whitecloud.consumer.Dao;

import com.whitecloud.consumer.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface testDao extends JpaRepository<User,Long> {

}
