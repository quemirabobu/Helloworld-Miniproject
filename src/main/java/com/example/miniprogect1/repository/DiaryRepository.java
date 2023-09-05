package com.example.miniprogect1.repository;

import com.example.miniprogect1.domain.User;
import com.example.miniprogect1.entity.Diary;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByUser(User user);

//    List<Diary> findByTitle(String title);
//일기의 제목으로 일기를 검색하는 메소드
//    특정 사용자가 작성한 일기를 검색하거나, 특정 날짜에 작성된 일기를 검색하는 등의 사용자 정의 쿼리를 생성

    //미니메인화면에 최신게시글 4개씩 표출하는거 활용하려고 만듬
//    List<Diary> findTop4ByOrderByCreatedDateDesc();
//    List<Diary> findRecentDiariesByUser(User loginUser, int count);

    @Query("SELECT d FROM Diary d WHERE d.user = :loginUser ORDER BY d.createdTime DESC")
    Page<Diary> findRecentDiariesByUser(@Param("loginUser") User loginUser, Pageable pageable);

    //일일 다이어리 게시글 수
    @Query("SELECT COUNT(d) FROM Diary d WHERE d.user = :user AND DATE(d.createdTime) = :today")
    int countTodayDiariesByUser(@Param("user") User user, @Param("today") LocalDate today);
    //총 다이어리 게시글 수
    @Query("SELECT COUNT(d) FROM Diary d WHERE d.user = :user")
    int countTotalDiariesByUser(@Param("user") User user);
}




