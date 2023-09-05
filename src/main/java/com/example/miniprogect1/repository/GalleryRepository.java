package com.example.miniprogect1.repository;

import com.example.miniprogect1.domain.PhotoGallery;
import com.example.miniprogect1.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;


@Repository
public interface GalleryRepository extends JpaRepository <PhotoGallery, Long> {
    //사진을 최신게시글이 위로 올라오게 표출하기 위한 메소드
    List<PhotoGallery> findAllByOrderByCreatedDateDesc();

    PhotoGallery findById(long id);

////    //미니메인화면에 최신게시글 4개씩 표출하는거 활용하려고 만듬
//List<PhotoGallery> findRecentPhotoGalleriesByUser(@Param("loginUser")User loginUser, int count);

//    @Query("SELECT pg FROM PhotoGallery pg WHERE pg.user = :loginUser ORDER BY pg.createdDate DESC")
//    List<PhotoGallery> findRecentPhotoGalleriesByUser(@Param("loginUser") User loginUser, Pageable pageable);

    @Query("SELECT pg FROM PhotoGallery pg WHERE pg.uploader = :loginUser ORDER BY pg.createdDate DESC")
    List<PhotoGallery> findRecentPhotoGalleriesByUser(@Param("loginUser") User loginUser, Pageable pageable);

    //사진첩 페이징 처리 관련
    Page<PhotoGallery> findByUploader(User uploader, Pageable pageable);

    //일일게시글 수
    @Query("SELECT COUNT(pg) FROM PhotoGallery pg WHERE pg.uploader = :user AND DATE(pg.createdDate) = :today")
    int countTodayPhotoGalleriesByUser(@Param("user") User user, @Param("today") LocalDate today);
    //총합산게시글 수
    @Query("SELECT COUNT(pg) FROM PhotoGallery pg WHERE pg.uploader = :user")
    int countTotalPhotoGalleriesByUser(@Param("user") User user);


}
