package com.example.miniprogect1.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "user_post")
@Getter
@Setter
@Entity
public class UserPost {
    //미니홈피 내 한줄소개글? 상태메세지? 를 수정하고 저장관리 하는 용도의 엔티티 생성
    //미니홈피 페이지 제목 설정

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id") // 포스트(한줄소개줄)고유ID
    private Long id;

    @Column(name = "content") //포스트 내용
    private String content = "상태 메세지를 입력해주세요.";

    @Column(name = "page_title") //미니홈피 제목
    private String pageTitle = "미니홈피의 제목을 입력해주세요 :)";

    ///////////////////////////////////////////////여기 되돌릴수도 있음
    //조인 컬럼
//    @OneToOne(fetch = FetchType.LAZY)
    @OneToOne(fetch = FetchType.EAGER)
    @JsonManagedReference
    @JoinColumn(name = "user_id")
    private User user;


}
