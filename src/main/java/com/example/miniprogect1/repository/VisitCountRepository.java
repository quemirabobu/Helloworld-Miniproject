package com.example.miniprogect1.repository;

import com.example.miniprogect1.domain.User;
import com.example.miniprogect1.domain.VisitCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitCountRepository extends JpaRepository<VisitCount, Long> {
    VisitCount findByUser(User user);
}
