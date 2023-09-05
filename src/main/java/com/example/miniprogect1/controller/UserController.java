package com.example.miniprogect1.controller;

import com.example.miniprogect1.Service.UserPostService;
import com.example.miniprogect1.domain.ProductEntity;
import com.example.miniprogect1.domain.PurchasedProductEntity;
import com.example.miniprogect1.domain.User;
import com.example.miniprogect1.Service.MemberService;
import com.example.miniprogect1.domain.UserPost;
import com.example.miniprogect1.repository.MemberRepository;
import com.example.miniprogect1.repository.ProductRepository;
import com.example.miniprogect1.repository.PurchasedProductRepository;
import com.example.miniprogect1.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;


@Controller
public class UserController {

    //의존성 주입
    private final MemberService memberService;
    private final MemberRepository memberRepository;
private final PurchasedProductRepository purchasedProductRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final UserPostService userPostService;


    public UserController(MemberService memberService, MemberRepository memberRepository, PurchasedProductRepository purchasedProductRepository,
                          ProductRepository productRepository,  UserPostService userPostService, UserRepository userRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.purchasedProductRepository = purchasedProductRepository;
        this.productRepository = productRepository;
        this.userPostService = userPostService;
        this.userRepository = userRepository;
    }

    //회원가입 화면으로 이동
    @GetMapping("/join")
    public String joinView() {
        return "join";
    }



    @GetMapping("/homeP")
    public String homePView()  {


        return "MiniHomePage";
    }


    ///여기 김은석이 바꾼거
    ///여기 김은석이 바꾼거
    ///여기 김은석이 바꾼거










    //회원가입 처리
    @PostMapping("/join")
    public String join(User user) {
        user.setRegDate(LocalDateTime.now());
        memberService.join(user);
        PurchasedProductEntity purchasedProduct = new PurchasedProductEntity();
        purchasedProduct.setUser(user);  // Assuming that PurchasedProductEntity has a user field
        // Set other fields of the PurchasedProductEntity as needed
//////////////////////////가입을 하면 자동으로 엘사가 구입됩니다
//////////////////////////가입을 하면 자동으로 엘사가 구입됩니다
//////////////////////////가입을 하면 자동으로 엘사가 구입됩니다
//////////////////////////가입을 하면 자동으로 엘사가 구입됩니다
//////////////////////////가입을 하면 자동으로 엘사가 구입됩니다
//////////////////////////가입을 하면 자동으로 엘사가 구입됩니다
        UserPost userPost = new UserPost();
        userPost.setContent("상태메세지를 입력해주세요.");
        userPost.setPageTitle("미니홈피의 제목을 입력해주세요 :)");

        userPost.setUser(user);
        userPostService.savePost(userPost);
        user.setUserPost(userPost);
















purchasedProduct.setProductEntity(productRepository.temporalmakeminimi(19));
        // Save the PurchasedProductEntity
        System.out.println("여기까지 19번 넣은거");
        System.out.println(productRepository.temporalmakeminimi(19));
        System.out.println("여기까지 19번 넣은거");
        System.out.println(purchasedProduct);
        purchasedProductRepository.save(purchasedProduct);

        return "Login";
    }

    //로그인 화면 으로 이동
    @GetMapping("/")
    public String loginView(HttpSession session) {
        if (session.getAttribute("loginUser") != null) {
            session.invalidate(); // 세션 무효화
            return "redirect:/login";
        } else {
            return "Login";
        }
    }


    //로그인 처리
    @PostMapping("/login")
    public String login(User user, HttpSession session) {
        if (memberService.authenticate(user.getUserName(), user.getPassword())) {
            User loginUser = memberRepository.findByUserName(user.getUserName());
            session.setAttribute("loginUser", loginUser);
            System.out.println(loginUser+"11111111111");
        }
        return "redirect:/home";
    }

    @ResponseBody
    @RequestMapping("/idCheck")
    public int idCheck(@RequestParam("userName") String userName) {
        System.out.println(userName);
        int idCheck = memberService.idChk(userName);

        if (idCheck > 0) {
            // 중복된 아이디
            return 1;
        } else {
            // 사용 가능한 아이디
            return 0;
        }
    }

    @ResponseBody
    @RequestMapping("/NickNameCheck")
    public int nicknameCheck(@RequestParam("userNickName") String userNickName) {
        System.out.println(userNickName);
        int nicknameCheck = memberService.nChk(userNickName);

        if (nicknameCheck > 0) {
            // 중복된 닉네임
            return 1;
        } else {
            // 사용 가능한 닉네임
            return 0;
        }
    }

    //로그아웃 시키기

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }
        return "Login"; // 로그아웃 후 리다이렉트할 경로
    }



}
