package com.explorers.smartparking.registration.persistence.dao;

import com.explorers.smartparking.registration.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {

    User findByEmail(String email);

    boolean existsByEmail(String email);

}
