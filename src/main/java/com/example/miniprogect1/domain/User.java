package com.example.miniprogect1.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


//유저테이블 구현한 곳 H_member
@Entity
@Getter
@Setter
@Table(name = "H_member")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id; //이것은 고유값입니다.

    @Column(name = "userName", unique = true)
    private String userName; //이 친구가 회원가입의 "아이디" 입니다.

    @Column(name = "password")
    private String password;

    @Column(name = "USER_EMAIL")
    private String USER_EMAIL;

    @Column(name = "user_nickname", unique = true)
    private String userNickName;

    @Column(name = "sex")
    private String sex;

    @Column(name = "bamtori")
    private int bamtori;

    @Column(name = "reg_date")
    private LocalDateTime regDate = LocalDateTime.now();

    @JsonIgnoreProperties("user")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ImgPath> Imgs;
    @JsonIgnoreProperties("user")

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<GuestBook> writtenBoards;

    @JsonIgnoreProperties("user")
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<GuestBook> receivedBoards;

    //방문자수테이블과 조인

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    /////////////////////////여기여기
    @JsonBackReference
    private VisitCount visitCount;

    @JsonBackReference
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserPost userPost;

    //사진첩
    @OneToMany(mappedBy = "uploader")
    private List<PhotoGallery> uploadedPhotos;

//    @OneToMany(mappedBy = "galleryOwner")
//    private List<PhotoGallery> ownedPhotos;



}