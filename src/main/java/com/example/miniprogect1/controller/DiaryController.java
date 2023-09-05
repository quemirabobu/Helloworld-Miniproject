package com.example.miniprogect1.controller;

import com.example.miniprogect1.Service.UserService;
import com.example.miniprogect1.domain.ImgPath;
import com.example.miniprogect1.domain.User;
import com.example.miniprogect1.entity.Diary;
import com.example.miniprogect1.repository.DiaryRepository;
import com.example.miniprogect1.repository.ImgPathRepository;
import com.example.miniprogect1.repository.UserPostRepository;
import com.example.miniprogect1.repository.VisitCountRepository;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
public class DiaryController {

    private final DiaryRepository diaryRepository;
    private final ImgPathRepository imgPathRepository;
    private final UserService userService;

    private final UserPostRepository userPostRepository;
    private final VisitCountRepository visitCountRepository;

    public DiaryController(DiaryRepository diaryRepository, ImgPathRepository imgPathRepository, UserService userService, UserPostRepository userPostRepository, VisitCountRepository visitCountRepository) {
        this.diaryRepository = diaryRepository;
        this.imgPathRepository = imgPathRepository;
        this.userService = userService;
        this.userPostRepository = userPostRepository;
        this.visitCountRepository = visitCountRepository;
    }
//
//    @GetMapping({"/MiniDiary-view", "/diaryView"})
//    public ModelAndView getDiaries(HttpSession session) {
//        ModelAndView mv = new ModelAndView();
//
//        User targetUser = (User) session.getAttribute("targetUser");
//        if (targetUser == null) {
//            targetUser = (User) session.getAttribute("loginUser");
//        }
//
//        List<Diary> diaries = diaryRepository.findByUser(targetUser);
//
//        mv.addObject("diaries", diaries);
//        mv.addObject("loginUser", targetUser);
//        List<User> users = userService.getAllUsers();
//        mv.addObject("users", users);
//        mv.addObject("diary", new Diary());
//        mv.setViewName("/MiniHomePage/diaryView");
//
//        return mv;
//    }


    @PostMapping("/diaryWrite")
    public String add(@Valid @ModelAttribute("diary") Diary diary, BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return "redirect:/diaryView";
        }
        User loginUser = (User) session.getAttribute("loginUser");
        diary.setUser(loginUser);
        diaryRepository.save(diary);
        return "redirect:/diaryView";
    }

    @GetMapping("/diaryWrite")
    public ModelAndView showDiaryWriteForm(HttpSession session, @RequestParam(value = "userId", required = false) Long userId) {
        ModelAndView mv = new ModelAndView();
        // 로그인한 사용자를 가져오기
        User loginUser = (User) session.getAttribute("loginUser");
        // userId 파라미터가 주어진 경우 targetUser를 조회, 없으면 로그인 사용자를 targetUser로 설정
        User targetUser = (userId != null) ? userService.getUserById(userId) : loginUser;
        System.out.println(targetUser.getId());

        // targetUser의 프로필 이미지 불러오기
        ImgPath imgPath = imgPathRepository.findByUser(targetUser);
        if (imgPath != null) {
            mv.addObject("profileImg", imgPath.getFileName());
            System.out.println("!!!!!!!!!!!!!!!!!!!!!" + imgPath.getFileName());
        } else {
            // 사용자에 대한 ImgPath 객체가 없는 경우에 대한 처리 로직
            // 예: 기본 이미지 파일 이름을 지정해 모델에 추가
            String defaultImgFileName;
            if (targetUser.getSex().equals("여성")) {
                defaultImgFileName = "GirlPro.png";
            } else {
                defaultImgFileName = "BoyPro.png";
            }
            mv.addObject("profileImg", defaultImgFileName);
        }

        //투데이
        String content = userPostRepository.findByUser(targetUser).getContent();
        int todayVisitCount = visitCountRepository.findByUser(targetUser).getTodayVisitCount();
        int totalVisitCount = visitCountRepository.findByUser(targetUser).getTotalVisitCount();


        // 가져온 정보를 MiniPicture 컨트롤러의 ModelAndView에 추가
        mv.addObject("content", content);
        mv.addObject("todayVisitCount", todayVisitCount);
        mv.addObject("totalVisitCount", totalVisitCount);

        mv.addObject("diary", new Diary());
        mv.addObject("loginUser", loginUser); // 로그인 사용자를 뷰에 전달
        mv.addObject("targetUser", targetUser); // targetUser를 뷰에 전달

        mv.setViewName("/MiniHomePage/diaryWrite");
        return mv;
    }

    //다이어리 수정
    @GetMapping("/diaryUpdate/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Long id, HttpSession session, @RequestParam(value = "userId", required = false) Long userId) {


        // 세션에서 targetUser를 가져오기
        User targetUser = (userId != null) ? userService.getUserById(userId) : (User) session.getAttribute("loginUser");

        System.out.println(targetUser.getId());
        // userId 파라미터가 주어진 경우 사용자 조회
        if (userId != null) {
            targetUser = userService.getUserById(userId);
            // 새로운 targetUser를 세션에 저장
            session.setAttribute("targetUser", targetUser);
        }


        ModelAndView mv = new ModelAndView();

        String content = userPostRepository.findByUser(targetUser).getContent();
        int todayVisitCount = visitCountRepository.findByUser(targetUser).getTodayVisitCount();
        int totalVisitCount = visitCountRepository.findByUser(targetUser).getTotalVisitCount();


        // 가져온 정보를 MiniPicture 컨트롤러의 ModelAndView에 추가
        mv.addObject("content", content);
        mv.addObject("todayVisitCount", todayVisitCount);
        mv.addObject("totalVisitCount", totalVisitCount);
        mv.addObject("loginUser", targetUser);

        User user = (User) session.getAttribute("loginUser");
        ImgPath imgPath = imgPathRepository.findByUser(targetUser);
        if (imgPath != null) {
            mv.addObject("profileImg", imgPath.getFileName());
            System.out.println("!!!!!!!!!!!!!!!!!!!!!" + imgPath.getFileName());
        } else {
            // 사용자에 대한 ImgPath 객체가 없는 경우에 대한 처리 로직
            // 예: 기본 이미지 파일 이름을 지정해 모델에 추가
            String defaultImgFileName;
            if (targetUser.getSex().equals("여성")) {
                defaultImgFileName = "GirlPro.png";
            } else {
                defaultImgFileName = "BoyPro.png";
            }
            mv.addObject("profileImg", defaultImgFileName);
        }
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid diary Id:" + id));
        mv.addObject("diary", diary);

        User loginUser = (User) session.getAttribute("loginUser");
        mv.addObject("loginUser", targetUser);

        mv.setViewName("/MiniHomePage/diaryUpdate");
        return mv;
    }


    @PostMapping("/diaryUpdate/{id}")
    public String update(@PathVariable("id") Long id, @Valid @ModelAttribute("diary") Diary diaryDetails, BindingResult result, HttpSession session) throws NotFoundException {

        if (result.hasErrors()) {
            return "/MiniHomePage/diaryUpdate";
        }

        // 로그인한 사용자의 정보 가져오기
        User loginUser = (User) session.getAttribute("loginUser");

        // Debug line
        System.out.println("Login User: " + loginUser);

        // 데이터베이스에서 기존의 일기 가져오기
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid diary Id:" + id));

        // Debug line
        System.out.println("Fetched Diary: " + diary);

        // diary가 null인 경우 예외 처리
        if (diary == null) {
            throw new NotFoundException("Diary not found");
        }

        // 로그인한 사용자와 일기의 작성자가 동일한지 확인
        if (loginUser.getId().equals(diary.getUser().getId())) {
            // 수정된 내용으로 기존의 일기 업데이트
            diary.setTitle(diaryDetails.getTitle());
            diary.setContent(diaryDetails.getContent());
            diary.setUpdatedTime(LocalDateTime.now());

            diaryRepository.save(diary);
            return "redirect:/diaryView";
        } else {
            // 일기의 작성자와 로그인한 사용자가 동일하지 않은 경우 에러 처리
            return "error";
        }
    }


    // 삭제
    @PostMapping("/diaryDelete/{id}")
    public String delete(@PathVariable("id") Long id) {
        diaryRepository.deleteById(id);
        return "redirect:/diaryView";
    }
}