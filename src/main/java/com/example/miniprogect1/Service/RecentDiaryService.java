package com.example.miniprogect1.Service;

import com.example.miniprogect1.domain.User;
import com.example.miniprogect1.entity.Diary;
import com.example.miniprogect1.repository.DiaryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecentDiaryService {
    private final DiaryRepository diaryRepository;

    public RecentDiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }
    public Page<Diary> getRecentDiariesByUser(User user, Pageable pageable) {
        return diaryRepository.findRecentDiariesByUser(user, pageable);
    }

}