package com.example.miniprogect1.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "ilchonpatch")
public class Ilchonpatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester; // 일촌 요청자

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver; // 일촌 요청 받는 사용자

    // 요청 상태 (승인됨, 대기 중 등)
    @Column(name = "status")
    private String status;

    // 추가적인 속성...
}