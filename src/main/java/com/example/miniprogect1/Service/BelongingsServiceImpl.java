package com.example.miniprogect1.Service;


import com.example.miniprogect1.domain.PurchasedProductEntity;
import com.example.miniprogect1.repository.BelongingsRepository;
import com.example.miniprogect1.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service

public class BelongingsServiceImpl  implements BelongingsService{

    private BelongingsRepository belongingsRepository;

    private ProductRepository productRepository;

    @Autowired
    public BelongingsServiceImpl(BelongingsRepository belongingsRepository, ProductRepository productRepository){
        this.belongingsRepository = belongingsRepository;
        this.productRepository = productRepository;

    }
    @Override
    public List<Map<String, Object>> getBelongingList(long Id) {
        return belongingsRepository.findallproductlistbyId(Id);
    }

//    @Override
//    public Optional<PurchasedProductEntity> findById(long Id) {
//        return Optional.empty();
//    }

    public List<PurchasedProductEntity> findAll() {
        return belongingsRepository.findAll();
    }

    @Override
    public List<PurchasedProductEntity> findById(long Id) {
        return belongingsRepository.findById(Id);
    }

    @Override
    public PurchasedProductEntity getpurchasedProductEntity(int productId, long Id) {
        return belongingsRepository.findBypIdAndId(productId, Id);
    }

    @Override
    public void productapply(int productId, long Id) {
        belongingsRepository.productdbapply(productId, Id);
    }

    @Override
    public void productdeapply(int productId, long Id) {
        belongingsRepository.productdbdeapply(productId, Id);
    }

    @Override
    public void makeitrepresentativemusic(int productId, long Id) {
        belongingsRepository.makeitrepresentativemusic(productId, Id);
    }
    @Override
    public List<Object[]> findtop5music(){



        return  belongingsRepository.letsfindtop5music();
    }

    @Override
    public List<Object[]> findtop3minimi(){



        return  belongingsRepository.letsfindtop3minimi();
    }

}
