package com.example.miniprogect1.controller;

import com.example.miniprogect1.Service.LikesService;
import com.example.miniprogect1.Service.PhotoUploadService;
import com.example.miniprogect1.domain.Likes;
import com.example.miniprogect1.domain.PhotoGallery;
import com.example.miniprogect1.domain.ResponseDTO;
import com.example.miniprogect1.domain.User;
import com.example.miniprogect1.repository.LikesRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;
    private final PhotoUploadService photoUploadService;
    private final LikesRepository likesRepository;

    @PostMapping("/likes")
    public ResponseEntity<?> likes(HttpSession session, @RequestParam("photoId")long photoId){
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        try {
            Likes likes = new Likes();

            User loginUser = (User)session.getAttribute("loginUser");

            PhotoGallery photoGallery = photoUploadService.getPhotoInfo(photoId);

            Likes checkLikes = likesService.checkLikes(loginUser, photoGallery);

            Map<String, String> returnMap = new HashMap<>();

            if(checkLikes == null) {
                photoGallery.setLikes(photoGallery.getLikes() + 1);
                likes.setUser(loginUser);
                likes.setPhotoGallery(photoGallery);
                likesRepository.save(likes);
                returnMap.put("msg", "like");
            } else {
                photoGallery.setLikes(photoGallery.getLikes() - 1);
                likes.setUser(loginUser);
                likes.setPhotoGallery(photoGallery);
                checkLikes = likesService.checkLikes(loginUser, photoGallery);
                likesRepository.delete(checkLikes);
                returnMap.put("msg", "dislike");
            }

            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            System.out.println("===========");
            System.out.println(checkLikes);

            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/cnt-likes")
    public int cntLikes(@RequestParam("id") long id) {
        PhotoGallery photoGallery = photoUploadService.getPhotoInfo(id);

        int cntLikes = photoGallery.getLikes();

        return cntLikes;
    }



}
