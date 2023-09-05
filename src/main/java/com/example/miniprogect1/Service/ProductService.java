package com.example.miniprogect1.Service;


import com.example.miniprogect1.domain.ProductEntity;
import com.example.miniprogect1.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    public void saveProduct(ProductEntity productEntity, MultipartFile file) throws Exception {

        String projectPath= System.getProperty("user.dir") + "\\src\\main\\resources\\static\\product"; //파일을 저장할 경로 생성
        UUID uuid = UUID.randomUUID(); //이름 중복방지하기 위해 random uuid생성
        String fileName = uuid + "_" + file.getOriginalFilename(); //원본파일에 uuid 붙혀 이름 생성
        File saveFile = new File(projectPath, fileName); //경로와 파일이름 가진 파일 객체 생성.
        file.transferTo(saveFile); //실제 업로드 처리
        productEntity.setProductFileName(fileName); //세터 이용해서 상품엔티티에 파일 이름 지정
        productEntity.setProductFilePath("/static/product" + fileName); //세터 이용해서 상품엔티티에 파일 경로 초기화
        productRepository.save(productEntity);
    }

    public List<ProductEntity> getFurniture(long pcId) {
        return productRepository.findByPcId(pcId);
    }

    public List<ProductEntity> findbyAllMusicWithCate() {

        return productRepository.findbyAllMusicWithCate();

    }




    public ProductEntity getProductEntitywithPid(long productId) {
        return productRepository.getProductEntitywithPid(productId);
    }
}
