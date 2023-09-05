package com.example.miniprogect1.repository;

import com.example.miniprogect1.domain.ImgPath;
import com.example.miniprogect1.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImgPathRepository extends JpaRepository<ImgPath, Long> {
    ImgPath findByUser(User user);
}