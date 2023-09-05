package com.example.miniprogect1.repository;

import com.example.miniprogect1.domain.GuestBook;
import com.example.miniprogect1.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository // 이 인터페이스가 Spring Data JPA의 리포지토리임을 선언
public interface GuestBookRepository extends JpaRepository<GuestBook, Long> { // JpaRepository를 상속받아 GuestBook 엔티티와 그의 Primary Key 타입이 Long인 리포지토리 생성
    Page<GuestBook> findByOwner(User owner, Pageable pageable);

    List<GuestBook> findByOwner(User owner); // User 객체를 매개변수로 받아 그 User가 소유한 모든 GuestBook을 리스트로 반환하는 메서드

    @Query("SELECT MAX(g.userPostId) FROM GuestBook g WHERE g.owner.id = :userId")
    Long findLastUserPostIdByOwnerId(@Param("userId") Long userId);


    //최신게시물 띄우기 위해 만듬
    //updateNews
    @Query("SELECT gb FROM GuestBook gb WHERE gb.owner = :loginUser ORDER BY gb.regdate DESC")

    List<GuestBook> findRecentGuestBooksByUser(@Param("loginUser") User loginUser, Pageable pageable);
    //일일게시글 수
    @Query("SELECT COUNT(gb) FROM GuestBook gb WHERE gb.owner = :user AND DATE(gb.regdate) = :today")
    int countTodayGuestBooksByUser(@Param("user") User user, @Param("today") LocalDate today);
    //총합산게시글 수
    @Query("SELECT COUNT(gb) FROM GuestBook gb WHERE gb.owner = :user")
    int countTotalGuestBooksByUser(@Param("user") User user);


}