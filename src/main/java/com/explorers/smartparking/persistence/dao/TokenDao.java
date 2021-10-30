package com.explorers.smartparking.persistence.dao;

import com.explorers.smartparking.persistence.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenDao extends JpaRepository<Token, Long> {

    Token findByToken(String token);
}
