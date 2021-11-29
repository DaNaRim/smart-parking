package com.explorers.smartparking.user.persistence.dao;

import com.explorers.smartparking.user.persistence.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenDao extends JpaRepository<Token, Long> {

    Token findByToken(String token);
}
