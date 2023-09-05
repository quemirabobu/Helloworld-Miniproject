package com.example.miniprogect1.repository;

import com.example.miniprogect1.domain.ImgPath;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PathRepository extends JpaRepository<ImgPath, Long> {
}
