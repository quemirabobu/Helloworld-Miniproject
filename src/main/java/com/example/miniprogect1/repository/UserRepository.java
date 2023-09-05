package com.example.miniprogect1.repository;


import com.example.miniprogect1.domain.UserDotori;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDotori, Integer> {

}
