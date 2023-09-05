package com.example.miniprogect1.controller; // 패키지 선언

import com.example.miniprogect1.Service.GuestBookService;
import com.example.miniprogect1.Service.UserService;
import com.example.miniprogect1.domain.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller // 해당 클래스를 스프링의 컨트롤러로 선언
public class GuestBookController {
    private final GuestBookService guestBookService; // 게시판 서비스 클래스 선언
    private final UserService userService; // 사용자 서비스 클래스 선언

    // 컨트롤러 생성자
    public GuestBookController(GuestBookService guestBookService, UserService userService) {
        this.guestBookService = guestBookService;
        this.userService = userService;
    }

    // 게시글 생성
    @PostMapping("/guestBook") // "/board" URL로 POST 요청이 오면 해당 메서드를 실행
    public String createBoard(@RequestParam String content, HttpSession session, @RequestParam(value = "userId", required = false) Long userId) {
        User writer = (User) session.getAttribute("loginUser"); // 세션에서 현재 사용자 정보를 가져옴
        User owner;
        if (userId != null && !userId.equals(writer.getId())) { // userId 파라미터가 존재하고, 현재 사용자의 ID와 일치하지 않는 경우
            owner = userService.getUserById(userId); // 해당 ID의 사용자를 조회
        } else {
            owner = writer; // 아닌 경우, 현재 사용자를 게시글의 소유자로 설정
        }

        LocalDateTime regdate = LocalDateTime.now(); // 현재 시간을 등록일자로 설정

        // 게시글 생성 서비스 호출
        // 게시글 생성 서비스가 변경된 save 메소드를 사용하도록 수정
        guestBookService.createBoard(writer, owner, content, regdate);

        return "redirect:/guestBook?userId=" + owner.getId(); // 생성 후 게시판 페이지로 리다이렉트
    }




}
