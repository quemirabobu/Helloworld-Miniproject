package com.example.miniprogect1.Service;

import com.example.miniprogect1.domain.User;
import com.example.miniprogect1.repository.DiaryRepository;
import com.example.miniprogect1.repository.GalleryRepository;
import com.example.miniprogect1.repository.GuestBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PostInfoService {

    private final DiaryRepository diaryRepository;
    private final GalleryRepository galleryRepository;
    private final GuestBookRepository guestBookRepository;

    @Autowired
    public PostInfoService(DiaryRepository diaryRepository, GalleryRepository galleryRepository, GuestBookRepository guestBookRepository) {
        this.diaryRepository = diaryRepository;
        this.galleryRepository = galleryRepository;
        this.guestBookRepository = guestBookRepository;
    }

    public int getTodayDiaryCount(User user, LocalDate today) {
        return diaryRepository.countTodayDiariesByUser(user, today);
    }

    public int getTotalDiaryCount(User user) {
        return diaryRepository.countTotalDiariesByUser(user);
    }

    public int getTodayGalleryCount(User user, LocalDate today) {
        return galleryRepository.countTodayPhotoGalleriesByUser(user, today);
    }

    public int getTotalGalleryCount(User user) {
        return galleryRepository.countTotalPhotoGalleriesByUser(user);
    }

    public int getTodayGuestbookCount(User user, LocalDate today) {
        return guestBookRepository.countTodayGuestBooksByUser(user, today);
    }

    public int getTotalGuestbookCount(User user) {
        return guestBookRepository.countTotalGuestBooksByUser(user);
    }

    public int getTodayTotalCount(User user, LocalDate today) {
        return getTodayDiaryCount(user, today) + getTodayGalleryCount(user, today) + getTodayGuestbookCount(user, today);
    }

    public int getTotalCount(User user) {
        return getTotalDiaryCount(user) + getTotalGalleryCount(user) + getTotalGuestbookCount(user);
    }
}

