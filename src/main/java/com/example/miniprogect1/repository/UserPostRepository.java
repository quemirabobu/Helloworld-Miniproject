package com.example.miniprogect1.repository;

import com.example.miniprogect1.domain.User;
import com.example.miniprogect1.domain.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPostRepository extends JpaRepository<UserPost, Long> {
    UserPost findByUser(User user);

}
