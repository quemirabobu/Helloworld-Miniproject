package com.example.miniprogect1.Service;

import com.example.miniprogect1.domain.PhotoGallery;
import com.example.miniprogect1.domain.User;
import com.example.miniprogect1.repository.GalleryRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PhotoUploadService {

    private final GalleryRepository galleryRepository;

    public PhotoUploadService(GalleryRepository galleryRepository) {
        this.galleryRepository = galleryRepository;
    }

    //사진을 최신게시글이 위로 올라오게 표출하기 위한 메소드
    public List<PhotoGallery> getAllPhotoGallery() {
        return galleryRepository.findAllByOrderByCreatedDateDesc();
    }


    public PhotoGallery uploadPhoto(MultipartFile file, String title, HttpSession session, String atthchPath) {
        try {

            // 디렉토리 생성
            createDirectory(atthchPath);

            // 업로드된 파일의 원본 파일명
            String originalFilename = file.getOriginalFilename();

            // 저장할 파일 경로
            String imageUrl = atthchPath + File.separator + originalFilename;

            // 파일을 지정된 경로로 복사
            FileCopyUtils.copy(file.getBytes(), new File(imageUrl));

            // 파일 저장 완료 후에는 DB에 경로를 저장할 수 있습니다.
            // DB에는 imageUrl를 저장하거나, 상황에 맞게 필요한 정보만 저장할 수 있습니다.

            // PhotoGallery 객체 생성 및 설정
            PhotoGallery photo = new PhotoGallery();
            photo.setTitle(title); // 업로드된 파일의 제목 설정
            photo.setCreatedDate(LocalDateTime.now()); //업로드 시간
            photo.setImageUrl(originalFilename); // 파일 경로 설정
            // 사용자 정보 설정
            User loginUser = (User) session.getAttribute("loginUser");
            photo.setUploader(loginUser); // 작성자 설정

            // PhotoGallery 저장
            galleryRepository.save(photo);
            return photo;

        } catch (IOException e) {
            // 예외 처리
            e.printStackTrace();
        }
        return null;
    }


    //디렉토리가 존재하지 않을 경우 디렉토리를 생성하는 데 사용
    private void createDirectory(String uploadDir) throws IOException {
        Path directoryPath = Paths.get(uploadDir);

        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }
    }

    public PhotoGallery getPhotoInfo(long photoId) {
        return galleryRepository.findById(photoId);
    }

}
