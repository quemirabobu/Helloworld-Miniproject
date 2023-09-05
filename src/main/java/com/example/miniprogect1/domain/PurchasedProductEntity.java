package com.example.miniprogect1.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@Table(name="purchased_product_entity")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchasedProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pp_id")

    private long ppId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User User;

    @ManyToOne
    @JoinColumn(name = "p_id")
    private ProductEntity productEntity;



    @Column(name = "applied")

    private char applied;




}
