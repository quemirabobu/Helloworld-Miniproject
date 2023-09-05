package com.example.miniprogect1.repository;

import com.example.miniprogect1.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);

    User findByPassword(String password);

    User findByUserNickName(String userNickName);

    Page<User> findAll(Pageable pageable);

    @Query("SELECT u FROM User u")
    List<User> findAll();

    Optional<User> findById(Long uId);

    @Modifying
    @Transactional
    @Query("UPDATE User m SET m.bamtori = :bamtori WHERE m.userName = :userName")
    void updateBamtori(@Param("bamtori") int bamtori, @Param("userName") String userName);
}