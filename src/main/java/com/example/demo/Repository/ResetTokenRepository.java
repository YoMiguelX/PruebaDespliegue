package com.example.demo.Repository;

import com.example.demo.Model.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {
    Optional<ResetToken> findByToken(String token);
}
