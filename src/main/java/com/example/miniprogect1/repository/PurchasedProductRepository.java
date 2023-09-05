package com.example.miniprogect1.repository;


import com.example.miniprogect1.domain.PurchasedProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface PurchasedProductRepository extends JpaRepository<PurchasedProductEntity, Long> {
    @Query(value="select a.pp_id, " +
            "           a.applied, " +
            "           b.coordinatex, " +
            "           b.coordinatey, " +
            "           a.p_id, " +
            "           a.user_id, " +
            "           b.pc_id, " +
            "           b.product_file_name," +
            "           b.product_file_path, " +
            "           b.product_name, " +
            "           b.product_price " +
            "   from purchased_product_entity a, " +
            "           product_entity b" +
            "   where a.p_id = b.p_id" +
            "     and a.user_id = :id" +
            "     and b.pc_id = :pcId", nativeQuery = true)
    List<Map<String, Object>> findByUser(@Param("id") long id, @Param("pcId")long pcId);

}
