package com.explorers.smartparking.registration.persistence.dao;

import com.explorers.smartparking.registration.persistence.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenDao extends JpaRepository<Token, Long> {

    Token findByToken(String token);
}
