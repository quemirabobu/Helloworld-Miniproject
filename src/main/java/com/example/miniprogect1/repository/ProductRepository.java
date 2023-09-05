package com.example.miniprogect1.repository;


import com.example.miniprogect1.domain.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByPcId(long pcId);


    /////////////여기다가 내가 만듬
    @Query(value = "select  * " +
            "   from  " +
            "           product_entity" +
            "   where product_category = '음악' " +
            "" +
            "", nativeQuery = true)
    List<ProductEntity> findbyAllMusicWithCate();

    @Query(value = "select  * " +
            "   from  " +
            "           product_entity" +
            "   where product_category = '음악' and p_id = :pId " +
            "" +
            "", nativeQuery = true)
    ProductEntity getProductEntitywithPid(@Param("pId") long pId);

    @Query(value = "select  * " +
            "   from  " +
            "           product_entity" +
            "   where  p_id = :pId " +
            "" +
            "", nativeQuery = true)
    ProductEntity temporalmakeminimi(@Param("pId") long pId);

    //김은석이 만든거

}
