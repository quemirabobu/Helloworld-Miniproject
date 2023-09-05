package com.example.miniprogect1.Service;

import com.example.miniprogect1.domain.Likes;
import com.example.miniprogect1.domain.PhotoGallery;
import com.example.miniprogect1.domain.User;
import com.example.miniprogect1.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;

    public Likes checkLikes(User user, PhotoGallery photoGallery) {

        Optional<Likes> checkLikes = likesRepository.findByUserAndPhotoGallery(user, photoGallery);
        if(checkLikes.isEmpty()) {
            return null;
        }
        return checkLikes.get();
    }


}
