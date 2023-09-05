package com.example.miniprogect1.Service;

import com.example.miniprogect1.domain.User;
import com.example.miniprogect1.domain.UserPost;
import com.example.miniprogect1.repository.UserPostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserPostService {


    private final UserPostRepository userPostRepository;

    public UserPostService(UserPostRepository userPostRepository) {
        this.userPostRepository = userPostRepository;
    }

    public UserPost getPostsByUser(User user) {
        // 특정 사용자의 글 목록 조회 로직 구현
        return userPostRepository.findByUser(user);
    }

    public UserPost getPostById(Long postId) {
        // 특정 글 ID로 글 조회 로직 구현
        return userPostRepository.findById(postId).orElse(null);
    }

    public void savePost(UserPost post) {
        // 글 저장 로직 구현
        userPostRepository.save(post);
    }

    public void deletePost(UserPost post) {
        // 글 삭제 로직 구현
        userPostRepository.delete(post);
    }
//    // 상태글을 업데이트하는 메소드
//    // loginUser: 로그인한 사용자 객체
//    // content: 업데이트할 상태글 내용
//    public void updateStatus(User loginUser, String content) {
//        UserPost userPost = loginUser.getUserPost(); // 로그인한 사용자의 UserPost 객체를 가져옴
//
//        if (userPost == null) {
//            userPost = new UserPost(); // UserPost 객체가 없다면 새로 생성
//            userPost.setUser(loginUser); // UserPost 객체에 loginUser 설정
//        }
//
//        userPost.setContent(content); // 상태글 내용 업데이트
//        userPostRepository.save(userPost); // 업데이트된 UserPost 객체를 저장
//    }
//
//    // 상태메시지를 가져오는 메소드
//    // loginUser: 로그인한 사용자 객체
//    // 반환값: 상태메시지 내용 (문자열)
//    public String getStatus(User loginUser) {
//        UserPost userPost = userPostRepository.findByUser(loginUser); // loginUser에 대한 UserPost 조회
//
//        if (userPost == null) {
//            return "상태메세지를 입력해주세요."; // UserPost가 없다면 기본 상태메시지 반환
//        } else {
//            return userPost.getContent(); // UserPost가 있다면 상태메시지 내용 반환
//        }
//    }



}
