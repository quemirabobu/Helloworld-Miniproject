package com.example.miniprogect1.Service;



import com.example.miniprogect1.domain.PurchasedProductEntity;

import java.util.List;
import java.util.Map;

public interface BelongingsService {
    List<Map<String, Object>> getBelongingList(long Id);

//    Optional<PurchasedProductEntity> findById(long Id);

    List<PurchasedProductEntity> findAll();

    List<PurchasedProductEntity> findById(long Id);

    PurchasedProductEntity getpurchasedProductEntity(int productId, long Id);

    void productapply(int productId, long Id);
    void productdeapply(int productId, long Id);


    void makeitrepresentativemusic(int productId, long Id);

    List<Object[]> findtop5music();

    List<Object[]> findtop3minimi();
}
