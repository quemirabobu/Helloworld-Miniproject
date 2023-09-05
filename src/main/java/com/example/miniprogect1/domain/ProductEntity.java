package com.example.miniprogect1.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "product_entity")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_id")
    private long pId;

    @Column(name = "pc_id")
    private long pcId;
    @Column(name = "product_category")

    private String productCategory;
    @Column(name = "product_name")

    private String productName;
    @Column(name = "product_price")
    private int productPrice;
    @Column(name = "product_file_path")
    private String productFilePath;
    @Column(name = "product_file_name")
    private String productFileName;
@Column(name = "coordinatex")
    private int coordinateX;
    @Column(name = "coordinatey")

    private int coordinateY;
    //카테고리 아이디


   // 그럼 아이템들 구분은? 카테고리를 만들어서 구분해준다.

}
