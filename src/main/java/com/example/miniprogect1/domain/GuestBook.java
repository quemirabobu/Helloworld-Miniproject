package com.example.miniprogect1.domain;

import org.hibernate.annotations.JoinFormula;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "guests")
@Getter
@Setter
public class GuestBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private LocalDateTime regdate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;
    @JsonIgnore

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @Column(name = "user_post_id")
    private Long userPostId;


//    @ManyToOne
//    @JoinColumn(name = "owner_user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
//    @JoinFormula(value = "(SELECT p.user_id FROM purchased_product p WHERE p.user_id = writer_user_id)")

//    @ManyToOne
//    @JoinColumn(name = "owner_user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
//    @JoinFormula(value = "(SELECT p.user_id FROM purchased_product_entity p WHERE p.user_id = writer_user_id)")
//    @JsonIgnore
//    private PurchasedProductEntity purchasedProductEntity;

    // 생성자, Getter, Setter, 기타 메소드
}