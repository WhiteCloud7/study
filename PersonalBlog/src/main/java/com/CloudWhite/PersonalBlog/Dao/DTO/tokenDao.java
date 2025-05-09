package com.CloudWhite.PersonalBlog.Dao.DTO;

import com.CloudWhite.PersonalBlog.Entity.DTO.token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface tokenDao extends JpaRepository<token,Integer> {
    public int findByUserId(int userId);
}
