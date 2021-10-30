package com.explorers.smartparking.persistence.dao;

import com.explorers.smartparking.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {

    User findByEmail(String email);

    boolean existsByEmail(String email);

}
