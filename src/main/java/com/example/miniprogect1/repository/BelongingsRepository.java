package com.example.miniprogect1.repository;


import com.example.miniprogect1.domain.ProductEntity;
import com.example.miniprogect1.domain.PurchasedProductEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
@Transactional
public interface BelongingsRepository extends JpaRepository<PurchasedProductEntity, Long> {


    @Query(value = "select a.pp_id, " +
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
            "     and a.user_id = :id", nativeQuery = true)
    List<Map<String, Object>> findallproductlistbyId(@Param("id") long id);


    List<PurchasedProductEntity> findAll();

    @Query(value = "select a.pp_id, " +
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
            "     and a.user_id = :id", nativeQuery = true)
    List<PurchasedProductEntity> findById(@Param("id") long id);

    @Query(value = "select a.pp_id, " +
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
            " and a.p_id= :pId " +
            "     and a.user_id = :id" +
            "" +
            "", nativeQuery = true)
    PurchasedProductEntity findBypIdAndId(@Param("pId") long pId, @Param("id") long id);



    @Modifying
    @Query(value = "update purchased_product_entity set applied ='o' where p_id = :pId and user_id = :id ", nativeQuery = true)
    void productdbapply(@Param("pId") long pId, @Param("id") long id);



    @Modifying
    @Query(value = "update purchased_product_entity set applied ='x' where p_id = :pId and user_id = :id ", nativeQuery = true)
    void productdbdeapply(@Param("pId") long pId, @Param("id") long id);

    @Modifying
    @Query(value = "update purchased_product_entity set applied ='r' where p_id = :pId and user_id = :id ", nativeQuery = true)
    void makeitrepresentativemusic(@Param("pId") long pId, @Param("id") long id);



//    @Query(value = "select a.pp_id, " +
//            "           a.applied, " +
//            "           b.coordinatex, " +
//            "           b.coordinatey, " +
//            "           a.p_id, " +
//            "           a.user_id, " +
//            "           b.pc_id, " +
//            "           b.product_file_name," +
//            "           b.product_file_path, " +
//            "           b.product_name, " +
//            "           b.product_price " +
//            "   from purchased_product_entity a, " +
//            "           product_entity b" +
//            "   where a.p_id = b.p_id" +
//            "       and b.product_category = '음악' ", nativeQuery = true)
//    List<PurchasedProductEntity> letsfindtop5music(@Param("id") long id);
//



    @Query(value = "SELECT a.p_id,b.pc_id, b.product_category, b.product_name, b.product_price, b.product_file_path, b.product_file_name , b.coordinatex, b.coordinatey " +
            "FROM purchased_product_entity a  " +
            "         JOIN product_entity b ON a.p_id = b.p_id  " +
            "WHERE b.product_category = '음악'  " +
            "GROUP BY a.p_id, b.product_file_name  " +
            "ORDER BY COUNT(*) DESC  " +
            "LIMIT 5  ", nativeQuery = true)
    List<Object[]> letsfindtop5music();



    @Query(value = "SELECT a.p_id,b.pc_id, b.product_category, b.product_name, b.product_price, b.product_file_path, b.product_file_name , b.coordinatex, b.coordinatey " +
            "FROM purchased_product_entity a  " +
            "         JOIN product_entity b ON a.p_id = b.p_id  " +
            "WHERE b.product_category = '미니미'  " +
            "GROUP BY a.p_id, b.product_file_name  " +
            "ORDER BY COUNT(*) DESC  " +
            "LIMIT 3  ", nativeQuery = true)
    List<Object[]> letsfindtop3minimi();













}
