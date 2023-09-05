package com.example.miniprogect1.domain;

import com.example.miniprogect1.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "photo_Gallery")
public class PhotoGallery {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "photo_title")
    private String title;

    @Column(name = "created_date") // 업로드일
    private LocalDateTime createdDate;

    @Column(name = "image_url")
    private String imageUrl; // 이미지 파일 경로

    //좋아요 기능 구현
    @Column(name = "likes", nullable = false)
    private Integer likes = 0;

    // 작성자 구분
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id")
    private User uploader;

    @Transient
    private boolean isLike;

//    // 소유자 구분
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "owner_id")
//    private User galleryOwner;



    //이미지 파일 경로를 저장하도록 하는 메소드를 추가.
    public void setImageUrl(String imageUrl) {
        this.imageUrl=imageUrl;
    }


    //수정한 부분 (업로더랑 세션 유저랑 구분 짓기 용)
    public User getUploader() {
        return this.uploader;
    }

}

