package com.example.miniprogect1.Service;


import com.example.miniprogect1.domain.ProductEntity;
import com.example.miniprogect1.domain.PurchasedProductEntity;
import com.example.miniprogect1.domain.User;
import com.example.miniprogect1.repository.ProductRepository;
import com.example.miniprogect1.repository.PurchasedProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PurchasedProductService {

    private PurchasedProductRepository purchasedProductRepository;

    private ProductRepository productRepository;
    @Autowired
    public PurchasedProductService(PurchasedProductRepository purchasedProductRepository,
                                   ProductRepository productRepository) {
        this.purchasedProductRepository = purchasedProductRepository;
        this.productRepository = productRepository;
    }

    public void purchase(PurchasedProductEntity purchasedProductEntity) {

        purchasedProductRepository.save(purchasedProductEntity);

    }

    public ProductEntity getProductInfo(long pId) {
        if(productRepository.findById(pId).isEmpty())
            return null;

        return productRepository.findById(pId).get();
    }

    public List<Map<String, Object>> getOwingProduct(User user, long pcId) {
        return purchasedProductRepository.findByUser(user.getId(), pcId);
    }

}
