package com.example.miniprogect1.controller;

import com.example.miniprogect1.Service.*;
import com.example.miniprogect1.domain.*;
import com.example.miniprogect1.entity.Diary;
import com.example.miniprogect1.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MiniController {

    private final VisitCountRepository visitCountRepository;
    private final MemberService memberService;
    private final UserPostService userPostService;
    private final UserPostRepository userPostRepository;
    private final MemberRepository memberRepository;


    //사진첩 레퍼지토리,서비스 ///영은//////
    @Autowired
    private final GalleryRepository galleryRepository;
    private final PhotoUploadService photoUploadService;
    private final ImgPathRepository imgPathRepository;

    ///////미니홈피 메인에 업데이트 뉴스부분땜에 만듬 영응으느느이이ㅣ잉//////
    private final RecentDiaryService recentDiaryService;
    private final DiaryRepository diaryRepository;

    ////////////////
    private final UserService userService;
    private final GuestBookService guestBookService;

    ///게시글수
    private final PostInfoService postInfoService;
    ////
    private IlchonService ilchonService;
    /////////여기 김은석꺼
    private final BelongingsService belongingsService;

    private final LikesRepository likesRepository;
    ///////

    @Value("${file.path}")
    private String attachPath;


    public MiniController(VisitCountRepository visitCountRepository, MemberService memberService, UserPostService userPostService, IlchonService ilchonService,UserPostRepository userPostRepository, MemberRepository memberRepository, GalleryRepository galleryRepository, PhotoUploadService photoUploadService, ImgPathRepository imgPathRepository, RecentDiaryService recentDiaryService, DiaryRepository diaryRepository, UserService userService, GuestBookService guestBookService, PostInfoService postInfoService, BelongingsService belongingsService, LikesRepository likesRepository) {
        this.visitCountRepository = visitCountRepository;
        this.memberService = memberService;
        this.userPostService = userPostService;
        this.userPostRepository = userPostRepository;
        this.memberRepository = memberRepository;
        this.galleryRepository = galleryRepository;
        this.photoUploadService = photoUploadService;
        this.imgPathRepository = imgPathRepository;
        this.recentDiaryService = recentDiaryService;
        this.diaryRepository = diaryRepository;
        this.userService = userService;
        this.guestBookService = guestBookService;
        this.postInfoService = postInfoService;
        this.belongingsService = belongingsService;
        this.ilchonService = ilchonService;
        this.likesRepository = likesRepository;

    }

    //이부분은 조회수 업하는거. 리다이렉트로 갑니다
    @GetMapping("/letsgotohompie/{userId}")
    public String uptoday(HttpSession session, @PathVariable Long userId) {
        //... your logic here

        User targetUser = (userId != null) ? userService.getUserById(userId) : (User) session.getAttribute("loginUser");
        VisitCount visitCount = visitCountRepository.findByUser(targetUser);
        System.out.println(targetUser);
        System.out.println("이게비짓카운트" + visitCount);
        if (visitCount == null) {
            // 새로운 날짜의 첫 방문 또는 자정 전 방문인 경우
            visitCount = new VisitCount();
            visitCount.setUser(targetUser);
            visitCount.setTodayVisitCount(0);
            visitCount.setTotalVisitCount(visitCount.getTotalVisitCount() + 1);
            visitCount.setDate(LocalDate.now());
        } else {


            // 자정에 방문한 경우
            // 이미 방문한 경우
            visitCount.setTodayVisitCount(visitCount.getTodayVisitCount() + 1);
            visitCount.setTotalVisitCount(visitCount.getTotalVisitCount() + 1);

        }
        visitCountRepository.save(visitCount);
        return "redirect:/MiniMain-view?userId=" + targetUser.getId();
    }




    @GetMapping("/musicgo/{userId}")
    public String gomusic(HttpSession session, @PathVariable Long userId, Model model) {
        //... your logic here
        User targetUser = (userId != null) ? userService.getUserById(userId) : (User) session.getAttribute("loginUser");
        Optional<PurchasedProductEntity> music = belongingsService.findById(targetUser.getId())
                .stream()
                .filter(b -> b.getProductEntity().getProductCategory().equals("음악") && b.getApplied() == 'r')
                .findFirst();

        if (music.isPresent()) {
            // 사용하려는 로직을 여기에 적어주세요.
            PurchasedProductEntity repmusic = music.get();
            model.addAttribute("representativemusic", repmusic);
        } else {
            // 리스트가 비어 있는 경우의 처리를 여기에 적어주세요.
        }
        List<PurchasedProductEntity> belongingsList = belongingsService.findById(targetUser.getId()).stream().filter(b -> b.getProductEntity().getProductCategory().equals("음악")).filter(z -> z.getApplied() == 'o').collect(Collectors.toList());
        model.addAttribute("belongingsList", belongingsList);
//        return "redirect:/MiniMain-view?userId=" + targetUser.getId();
        return "/MiniHomePage/music";
    }








    @GetMapping("/MiniMain-view")
    public ModelAndView MiniMain(HttpSession session, @RequestParam(value = "userId", required = false) Long userId) {
        Boolean fromMiniMain = (Boolean) session.getAttribute("fromMiniMain");
        session.removeAttribute("fromMiniMain");
        System.out.println(fromMiniMain);


        ModelAndView mv = new ModelAndView("/MiniHomePage/Main");

        // If userId is given, use that. Otherwise, use logged in user's ID
        User targetUser = (userId != null) ? userService.getUserById(userId): (User) session.getAttribute("loginUser");


        // 상태메시지 가져오기
        UserPost userPost = userPostService.getPostsByUser(targetUser);
        System.out.println(userPost);

//        if (userPost == null) {
//            userPost = new UserPost();
//            userPost.setContent("상태메세지를 입력해주세요.");
//            userPost.setPageTitle("미니홈피의 제목을 입력해주세요 :)");
//
//            userPost.setUser(targetUser);
//            userPostService.savePost(userPost);
//            targetUser.setUserPost(userPost);
//        } else {
//            mv.addObject("content", "상태메세지를 입력해주세요."); // 기본 상태 메시지
//        }
        mv.addObject("content", targetUser.getUserPost().getContent()); // 기본 상태 메시지

        mv.addObject("loginUser", targetUser);

        // 방문 횟수 정보 가져오기
        VisitCount visitCount = visitCountRepository.findByUser(targetUser);


        /////김은석이 이영은님의 VISitcount를 수정했는데 혹시 몰라서 일단 주석으로 놔둔다
        /////김은석이 이영은님의 VISitcount를 수정했는데 혹시 몰라서 일단 주석으로 놔둔다
        /////김은석이 이영은님의 VISitcount를 수정했는데 혹시 몰라서 일단  주석으로 놔둔다
//        // 현재 시간 정보
//        LocalTime currentTime = LocalTime.now();
//
//        if (visitCount == null || currentTime.isBefore(LocalTime.MIDNIGHT)) {
//            // 새로운 날짜의 첫 방문 또는 자정 전 방문인 경우
//            visitCount = new VisitCount();
//            visitCount.setUser(targetUser);
//            visitCount.setTodayVisitCount(0);
//            visitCount.setTotalVisitCount(visitCount.getTotalVisitCount() + 1);
//            visitCount.setDate(LocalDate.now());
//        } else if (currentTime.equals(LocalTime.MIDNIGHT)) {
//            // 자정에 방문한 경우
//            visitCount.setTodayVisitCount(0);
//            visitCount.setTotalVisitCount(visitCount.getTotalVisitCount() + 1);
//        } else {
//            // 이미 방문한 경우
//            visitCount.setTodayVisitCount(visitCount.getTodayVisitCount() + 1);
//            visitCount.setTotalVisitCount(visitCount.getTotalVisitCount() + 1);
//        }
        /////김은석이 이영은님의 VISitcount를 수정했는데 혹시 몰라서 일단 주석으로 놔둔다
        /////김은석이 이영은님의 VISitcount를 수정했는데 혹시 몰라서 일단 주석으로 놔둔다
        /////김은석이 이영은님의 VISitcount를 수정했는데 혹시 몰라서 일단  주석으로 놔둔다


//if(fromMiniMain==null||fromMiniMain!=false) {
//
//    if (visitCount == null) {
//        // 새로운 날짜의 첫 방문 또는 자정 전 방문인 경우
//        visitCount = new VisitCount();
//        visitCount.setUser(targetUser);
//        visitCount.setTodayVisitCount(0);
//        visitCount.setTotalVisitCount(visitCount.getTotalVisitCount() + 1);
//        visitCount.setDate(LocalDate.now());
//    } else {
//
//        // 자정에 방문한 경우
//        // 이미 방문한 경우
//        visitCount.setTodayVisitCount(visitCount.getTodayVisitCount() + 1);
//        visitCount.setTotalVisitCount(visitCount.getTotalVisitCount() + 1);
//
//    }
////여기까진 김은석이한거
//
//}


//여기까진 김은석이한거

        // VisitCount 저장
        visitCountRepository.save(visitCount);

        // 모델에 방문 횟수 정보 추가
        mv.addObject("todayVisitCount", visitCount.getTodayVisitCount());
        mv.addObject("totalVisitCount", visitCount.getTotalVisitCount());

        // 최신 다이어리 게시글 가져오기
//        // 지우지 마쎄용 - 영은 -
        // 최신 다이어리 게시물 가져오기
        int diaryPage = 0; // 첫 번째 페이지
        int diarySize = 1; // 한 페이지에 보여줄 개수
        Pageable diaryPageable = PageRequest.of(diaryPage, diarySize);
        Page<Diary> recentDiaries = recentDiaryService.getRecentDiariesByUser(targetUser, diaryPageable);
        mv.addObject("recentDiaryPosts", recentDiaries.getContent());


        // 최신 방명록 게시물 가져오기
        int guestBookSize = 1;
        Pageable guestBookPageable = PageRequest.of(0, guestBookSize);
        List<GuestBook> recentGuestBooks = guestBookService.findRecentGuestBooksByUser(targetUser, guestBookPageable);
        mv.addObject("recentGuestBookPosts", recentGuestBooks);


        // 최신 사진첩 게시글 가져오기
        int page = 0; // 첫 번째 페이지
        int size = 1; // 한 페이지에 보여줄 개수
        Pageable pageable = PageRequest.of(page, size);
        List<PhotoGallery> recentPhotoGalleries = galleryRepository.findRecentPhotoGalleriesByUser(targetUser, pageable);
        mv.addObject("recentPhotoGalleryPosts", recentPhotoGalleries);

        //게시글 수 표출 하는 아이들
        LocalDate today = LocalDate.now();
        mv.addObject("todayPostCount", postInfoService.getTodayDiaryCount(targetUser, today));
        mv.addObject("totalPostCount", postInfoService.getTotalDiaryCount(targetUser));
        mv.addObject("todayPhotoCount", postInfoService.getTodayGalleryCount(targetUser, today));
        mv.addObject("totalPhotoCount", postInfoService.getTotalGalleryCount(targetUser));
        mv.addObject("todayGuestbookCount", postInfoService.getTodayGuestbookCount(targetUser, today));
        mv.addObject("totalGuestbookCount", postInfoService.getTotalGuestbookCount(targetUser));
        mv.addObject("todayTotalCount", postInfoService.getTodayTotalCount(targetUser, today));
        mv.addObject("totalTotalCount", postInfoService.getTotalCount(targetUser));

        // 갖고있는 벽지 갖고오는 것
        List<PurchasedProductEntity> wallpaperlist = belongingsService.findById(targetUser.getId())
                .stream()
                .filter(b -> b.getProductEntity().getProductCategory().equals("벽지"))
                .filter(c -> c.getApplied() == 'o')
                .collect(Collectors.toList());
        mv.addObject("wallpaperlist", wallpaperlist);

        // 여기부터는 작은방에 띄워줄 적용된 것들을 갖고옴
        List<PurchasedProductEntity> appliedbelongingsList = belongingsService.findById(targetUser.getId())
                .stream()
                .filter(b -> b.getProductEntity().getProductCategory().equals("가구") || b.getProductEntity().getProductCategory().equals("미니미"))
                .filter(z -> z.getApplied() == 'o')
                .collect(Collectors.toList());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            String appliedbelongingsListJson = objectMapper.writeValueAsString(appliedbelongingsList);

            mv.addObject("appliedbelongingsList", appliedbelongingsListJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Optional<PurchasedProductEntity> music = belongingsService.findById(targetUser.getId())
                .stream()
                .filter(b -> b.getProductEntity().getProductCategory().equals("음악") && b.getApplied() == 'r')
                .findFirst();

        if (music.isPresent()) {
            PurchasedProductEntity repmusic = music.get();
            mv.addObject("representativemusic", repmusic);
        }

        List<PurchasedProductEntity> belongingsList = belongingsService.findById(targetUser.getId())
                .stream()
                .filter(b -> b.getProductEntity().getProductCategory().equals("음악"))
                .filter(z -> z.getApplied() == 'o')
                .collect(Collectors.toList());

        List<Ilchon> serch = ilchonService.findIlchonsByUser(targetUser.getUserNickName());
        List<User> users = new ArrayList<>();

        int idx = 0;
        for(Ilchon serd : serch)// 모든 사용자의 리스트를 조회
        {
            User ilchonUser = memberService.getid(serd.getYou().getUserName());

            users.add(ilchonUser);
        }
        mv.addObject("users", users);
        mv.addObject("belongingsList", belongingsList);


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

        String musicListUrl = "/musicgo/" + targetUser.getId();
        System.out.println(musicListUrl);
        mv.addObject("musicListUrl", musicListUrl);
        //여기수정한거
        System.out.println(musicListUrl);
        System.out.println(musicListUrl);
        System.out.println(musicListUrl);

        return mv;
    }

    // 미니홈피 상태메시지 글 수정 관리
    @PostMapping("/updateStatus")
    public ModelAndView updateStatus(@RequestBody Map<String, String> requestBody, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        UserPost userPost = userPostService.getPostsByUser(loginUser);

//        if (userPost == null) {
//            userPost = new UserPost();
//            userPost.setUser(loginUser);
//        }

        String content = requestBody.get("content"); // 요청 바디에서 content 값을 가져옴
        userPost.setContent(content);


        userPostService.savePost(userPost); // UserPostService를 통해 상태메시지 저장
        // 상태메시지 저장 후 세션에 사용자 정보 업데이트
        loginUser.setUserPost(userPost);

        // 세션을 갱신하여 변경된 정보를 반영
        // 갱신해야 다른 로그인을 하더라도 새로 정상작동함
        session.setAttribute("loginUser", loginUser);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/MiniHomePage/Main");
        return modelAndView;
    }

    // 상태메시지 표출
    @GetMapping("/getStatus")
    public ModelAndView getStatus(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        UserPost userPost = userPostService.getPostsByUser(loginUser); // UserPostService를 통해 상태메시지 조회

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("MiniHomePage/Main");

        if (userPost == null) {
//            userPost = new UserPost();
//            userPost.setPageTitle("상태메세지를 입력해주세요.");
//            userPost.setUser(loginUser);
//            userPostService.savePost(userPost); // UserPostService를 통해 상태메시지 저장
        } else {
            modelAndView.addObject("content", userPost.getContent()); // 상태 메시지를 모델에 추가
        }

        // 세션을 갱신하여 변경된 정보를 반영
        session.setAttribute("loginUser", loginUser);

        return modelAndView;
    }

    // 미니홈피 페이지 제목 수정 관리
    @PostMapping("/updatePageTitle")
    public ModelAndView updatePageTitle(@RequestBody Map<String, String> requestBody, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        UserPost userPost = userPostService.getPostsByUser(loginUser);

//        if (userPost == null) {
//            userPost = new UserPost();
//            userPost.setUser(loginUser);
//        }

        String pageTitle = requestBody.get("pageTitle"); // 요청 바디에서 pageTitle 값을 가져옴
        userPost.setPageTitle(pageTitle);
        userPostService.savePost(userPost); // UserPostService를 통해 페이지 제목 저장
        // 페이지 제목 저장 후 세션에 사용자 정보 업데이트
        loginUser.setUserPost(userPost);

        // 세션을 갱신하여 변경된 정보를 반영
        session.setAttribute("loginUser", loginUser);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/MiniHomePage/Main");
        return modelAndView;
    }

    //페이지 제목 표출
    @GetMapping("/titleStatus")
    public ModelAndView titleStatus(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        UserPost userPost = userPostService.getPostsByUser(loginUser); // UserPostService를 통해 사용자의 포스트 가져오기

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("MiniHomePage/Main");

        if (userPost == null) {
//            userPost = new UserPost();
//            userPost.setPageTitle("미니홈피의 제목을 입력해주세요 :)");
//            userPost.setUser(loginUser);
        } else {
            modelAndView.addObject("pageTitle", userPost.getPageTitle()); // 페이지 제목을 모델에 추가
        }

        // 세션을 갱신하여 변경된 정보를 반영
        session.setAttribute("loginUser", loginUser);

        return modelAndView;
    }


    //미니홈피 방명록화면 표출
    @GetMapping({"/MiniGuest-view", "/guestBook"})
    public String MiniGuest(HttpSession session, Model model,
                            @RequestParam(value = "userId", required = false) Long userId,
                            @RequestParam(value = "page", defaultValue = "0") int page) {

        session.setAttribute("fromMiniMain", false);


        User loginUser = (User) session.getAttribute("loginUser");

        // 세션에서 targetUser를 가져오기
        User targetUser = (userId != null) ? userService.getUserById(userId) : (User) session.getAttribute("loginUser");

        System.out.println(targetUser.getId());
        // userId 파라미터가 주어진 경우 사용자 조회
        if (userId != null) {
            targetUser = userService.getUserById(userId);
            // 새로운 targetUser를 세션에 저장
            session.setAttribute("targetUser", targetUser);
        }

        // MiniMain-view 컨트롤러의 정보 가져오기

//        ModelAndView miniMainModelAndView = MiniMain(session, targetUser.getId());
//        String content = miniMainModelAndView.getModel().get("content").toString();
//        int todayVisitCount = (int) miniMainModelAndView.getModel().get("todayVisitCount");
//        int totalVisitCount = (int) miniMainModelAndView.getModel().get("totalVisitCount");


        String content = userPostRepository.findByUser(targetUser).getContent();
        int todayVisitCount = visitCountRepository.findByUser(targetUser).getTodayVisitCount();
        int totalVisitCount = visitCountRepository.findByUser(targetUser).getTotalVisitCount();


        // Model 객체에 정보 추가
        model.addAttribute("content", content);
        model.addAttribute("todayVisitCount", todayVisitCount);
        model.addAttribute("totalVisitCount", totalVisitCount);
        model.addAttribute("loginUser", targetUser);

        User owner = targetUser; // targetUser를 owner로 사용

        int pageSize = 3; // 한 페이지에 표시할 게시글 수
        Sort sort = Sort.by(Sort.Direction.DESC, "id"); // 정렬 방식 설정
        Pageable pageable = PageRequest.of(page, pageSize, sort);

// 해당 사용자의 게시글을 페이징 처리하여 가져오기
        Page<GuestBook> guestBookPage = guestBookService.getBoardsByOwner(owner, pageable);
        List<GuestBook> guestBook = guestBookPage.getContent(); // 페이지에 해당하는 게시글 리스트

        PurchasedProductEntity currentwriteravatar = belongingsService.findById(loginUser.getId()).stream().filter(b -> b.getProductEntity().getProductCategory().equals("미니미")).filter(c -> c.getApplied() == 'o').findFirst().orElse(null);

        // 각 게스트북 항목의 작성자에 대한 아바타를 맵에 저장
        Map<User, PurchasedProductEntity> avatars = new HashMap<>();
        for (GuestBook gb : guestBook) {
            User writer = gb.getWriter();
            if (!avatars.containsKey(writer)) {
                PurchasedProductEntity avatar = belongingsService.findById(writer.getId())
                        .stream()
                        .filter(b -> b.getProductEntity().getProductCategory().equals("미니미"))
                        .filter(c -> c.getApplied() == 'o')
                        .findFirst().orElse(null);
                avatars.put(writer, avatar);
            }
        }

        model.addAttribute("avatars", avatars); // 모델에 아바타 맵 추가

        List<Ilchon> serch = ilchonService.findIlchonsByUser(targetUser.getUserNickName());
        List<User> users = new ArrayList<>();

        int idx = 0;
        for(Ilchon serd : serch)// 모든 사용자의 리스트를 조회
        {
            User ilchonUser = memberService.getid(serd.getYou().getUserNickName());

            users.add(ilchonUser);
        }

        model.addAttribute("currentwriteravatar", currentwriteravatar); // 모델에 게시글 리스트 추가
        model.addAttribute("guests", guestBook); // 모델에 게시글 리스트 추가
        model.addAttribute("user", owner); // 모델에 사용자 정보 추가
        model.addAttribute("users", users); // 모델에 사용자 리스트 추가
        model.addAttribute("currentPage", page); // 현재 페이지 번호
        model.addAttribute("totalPages", guestBookPage.getTotalPages()); // 총 페이지 수


        User user = (User) session.getAttribute("loginUser");
        ImgPath imgPath = imgPathRepository.findByUser(targetUser);
        if (imgPath != null) {
            model.addAttribute("profileImg", imgPath.getFileName());
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
            model.addAttribute("profileImg", defaultImgFileName);
        }


        return "/MiniHomePage/GuestBook";
    }


    @GetMapping({"/MiniDiary-view", "/diaryView"})
    public ModelAndView getDiaries(HttpSession session, @RequestParam(value = "userId", required = false) Long userId) {
        ModelAndView mv = new ModelAndView();
        session.setAttribute("fromMiniMain", false);

        // 세션에서 targetUser를 가져오기
        User targetUser = (userId != null) ? userService.getUserById(userId) : (User) session.getAttribute("loginUser");

        System.out.println(targetUser.getId());
        // userId 파라미터가 주어진 경우 사용자 조회
        if (userId != null) {
            targetUser = userService.getUserById(userId);
            // 새로운 targetUser를 세션에 저장
            session.setAttribute("targetUser", targetUser);
        }

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
        System.out.println(targetUser.getId());

        List<Diary> diaries = diaryRepository.findByUser(targetUser);

        String content = userPostRepository.findByUser(targetUser).getContent();
        int todayVisitCount = visitCountRepository.findByUser(targetUser).getTodayVisitCount();
        int totalVisitCount = visitCountRepository.findByUser(targetUser).getTotalVisitCount();


        // 가져온 정보를 MiniPicture 컨트롤러의 ModelAndView에 추가
        mv.addObject("content", content);
        mv.addObject("todayVisitCount", todayVisitCount);
        mv.addObject("totalVisitCount", totalVisitCount);

        mv.addObject("diaries", diaries);
        mv.addObject("loginUser", targetUser);
        List<Ilchon> serch = ilchonService.findIlchonsByUser(targetUser.getUserNickName());
        List<User> users = new ArrayList<>();

        int idx = 0;
        for(Ilchon serd : serch)// 모든 사용자의 리스트를 조회
        {
            User ilchonUser = memberService.getid(serd.getYou().getUserNickName());

            users.add(ilchonUser);
        }
        mv.addObject("users", users);
        mv.addObject("diary", new Diary());
        mv.setViewName("/MiniHomePage/diaryView");

        return mv;
    }


    //방문자수 로직 구현1.
    //updateVisitCount() 메서드를 호출할 때마다 세션에서 방문 횟수 정보를 가져와 갱신
    private void updateVisitCount(HttpSession session) {
        // 세션에서 방문 횟수 정보 가져오기
        Integer visitCount = (Integer) session.getAttribute("visitCount");

        // 방문 횟수 갱신
        if (visitCount == null) {
            // 최초 방문일 경우
            visitCount = 1;
        } else {
            // 기존에 방문한 적이 있을 경우
            //갱신된 방문 횟수를 세션에 다시 저장
            visitCount++;
        }

        // 세션에 방문 횟수 저장
        session.setAttribute("visitCount", visitCount);
    }


    //////////////////////////////////////////////
    /////////영은 사진첩 구현중////////////
    //미니홈피 사진첩 표출
    @GetMapping("/MiniPicture-view")
    public ModelAndView MiniPicture(HttpSession session, @RequestParam(value = "userId", required = false) Long userId
            , @RequestParam(value = "page", defaultValue = "0") int page) {
        session.setAttribute("fromMiniMain", false);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/MiniHomePage/Picture");

        User targetUser = (userId != null) ? userService.getUserById(userId) : (User) session.getAttribute("loginUser");
        session.setAttribute("targetUser", targetUser);

        // MiniMain-view 컨트롤러의 정보 가져오기
//        ModelAndView miniMainModelAndView = MiniMain(session, targetUser.getId());
//        String content = miniMainModelAndView.getModel().get("content").toString();
//        int todayVisitCount = (int) miniMainModelAndView.getModel().get("todayVisitCount");
//        int totalVisitCount = (int) miniMainModelAndView.getModel().get("totalVisitCount");

        String content = userPostRepository.findByUser(targetUser).getContent();
        int todayVisitCount = visitCountRepository.findByUser(targetUser).getTodayVisitCount();
        int totalVisitCount = visitCountRepository.findByUser(targetUser).getTotalVisitCount();


        // 가져온 정보를 MiniPicture 컨트롤러의 ModelAndView에 추가
        mv.addObject("content", content);
        mv.addObject("todayVisitCount", todayVisitCount);
        mv.addObject("totalVisitCount", totalVisitCount);
        mv.addObject("loginUser", targetUser);

        // 추가: 업로드된 사진 제목과 최신순으로 정렬된 사진 목록 가져오기
        int pageSize = 3; // 한 페이지에 표시할 사진 수
        Sort sort = Sort.by(Sort.Direction.DESC, "id"); // 정렬 방식 설정
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        Page<PhotoGallery> photoPage = galleryRepository.findByUploader(targetUser, pageable);
        List<PhotoGallery> photoList = photoPage.getContent(); // 현재 페이지에 해당하는 사진 목록

        List<Ilchon> serch = ilchonService.findIlchonsByUser(targetUser.getUserNickName());
        List<User> users = new ArrayList<>();

        //좋아요 어떤사람이 어떤 게시글 눌렀는지 알아보자!
        User user = (User) session.getAttribute("loginUser");

        List<Long> loginUserlikes = likesRepository.findPhotoIdByUserId(user.getId());

        for(PhotoGallery p : photoList) {
            for(long pId : loginUserlikes) {
                if(p.getId() == pId) {
                    p.setLike(true);
                }
            }
        }

        int idx = 0;
        for(Ilchon serd : serch)// 모든 사용자의 리스트를 조회
        {
            User ilchonUser = memberService.getid(serd.getYou().getUserNickName());

            users.add(ilchonUser);
        }


        //주인 유저만 삭제, 업로드 할 수 있도록 하기
//        boolean isUploader = false; // 초기화
//
//        for (PhotoGallery photo : photoList) {
//            if (targetUser != null && targetUser.getId().equals(photo.getUploader().getId())) {
//                isUploader = true; // uploader.id와 loginUser.id가 같을 경우
//                break;
//            }
//        }

        boolean isUploader = targetUser != null && targetUser.getId().equals(((User) session.getAttribute("loginUser")).getId());
        mv.addObject("isUploader", isUploader); // isUploader를 모델에 추가

        // 주인 유저인 경우에는 업로드 버튼 표시, 아니면 이미지 표시
        if (isUploader) {
            mv.addObject("showUploadButton", true);
        } else {
            if (photoList.isEmpty()) {
                mv.addObject("showNoPhotoImage", true);
            }
        }

        mv.addObject("isUploader", isUploader); // isUploader를 모델에 추가

        mv.addObject("users", users);
        mv.addObject("photoList", photoList);
        mv.addObject("photoPage", photoPage); // 페이징 처리된 페이지 객체 추가


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



        //여기수정한거
        String musicListUrl = "/musicgo/" + targetUser.getId();
        System.out.println(musicListUrl);
        mv.addObject("musicListUrl", musicListUrl);
        //여기수정한거






        return mv;
    }


    //사진을 업로드 하는 기능 구현

    //사진을 업로드 하는 기능 구현
    @PostMapping("/Photo-upload")
    public ModelAndView uploadPhoto(@RequestParam("file") MultipartFile file, @RequestParam("title") String title,
                                    HttpSession session, @RequestParam(value = "userId", required = false) Long userId) {
        ModelAndView modelAndView = new ModelAndView();

        User targetUser;
        if (userId != null) {
            try {
//                Long userIdValue = Long.parseLong(userId);
                targetUser = userService.getUserById(userId);
            } catch (NumberFormatException e) {
                // userId가 숫자 형식으로 변환되지 않는 경우 처리
                e.printStackTrace();
                targetUser = (User) session.getAttribute("loginUser");
            }
        } else {
            targetUser = (User) session.getAttribute("loginUser");
        }
        session.setAttribute("targetUser", targetUser);

        if (!file.isEmpty()) {
            // 파일 확장자 추출
            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();

            // PhotoUploadService를 사용하여 사진 업로드 처리
            PhotoGallery photo = photoUploadService.uploadPhoto(file, title, session, attachPath);

            modelAndView.addObject("photo", photo);
        }

        // MiniMain-view 컨트롤러의 정보 가져오기
        ModelAndView miniMainModelAndView = MiniMain(session, targetUser.getId());
//        String content = miniMainModelAndView.getModel().get("content").toString();
//        int todayVisitCount = (int) miniMainModelAndView.getModel().get("todayVisitCount");
//        int totalVisitCount = (int) miniMainModelAndView.getModel().get("totalVisitCount");

        String content = userPostRepository.findByUser(targetUser).getContent();
        int todayVisitCount = visitCountRepository.findByUser(targetUser).getTodayVisitCount();
        int totalVisitCount = visitCountRepository.findByUser(targetUser).getTotalVisitCount();

        modelAndView.addObject("content", content);
        modelAndView.addObject("todayVisitCount", todayVisitCount);
        modelAndView.addObject("totalVisitCount", totalVisitCount);
        modelAndView.addObject("loginUser", targetUser);

        modelAndView.setViewName("redirect:/MiniPicture-view");

        User user = (User) session.getAttribute("loginUser");
        ImgPath imgPath = imgPathRepository.findByUser(targetUser);
        if (imgPath != null) {
            modelAndView.addObject("profileImg", imgPath.getFileName());
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
            modelAndView.addObject("profileImg", defaultImgFileName);
        }
        return modelAndView;
    }


    //사진 업로드 하는 버튼을 눌렀을때 들어가지는 업로드 페이지
    @GetMapping(value = "/Upload-photo")
    public ModelAndView showUploadPhotoPage(HttpSession session, @RequestParam(value = "userId", required = false) Long userId) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/MiniHomePage/UploadPhoto");

        User targetUser = (userId != null) ? userService.getUserById(userId) : (User) session.getAttribute("loginUser");
        session.setAttribute("targetUser", targetUser);

        // MiniMain-view 컨트롤러의 정보 가져오기
//        ModelAndView miniMainModelAndView = MiniMain(session, targetUser.getId());
//        String content = miniMainModelAndView.getModel().get("content").toString();
//        int todayVisitCount = (int) miniMainModelAndView.getModel().get("todayVisitCount");
//        int totalVisitCount = (int) miniMainModelAndView.getModel().get("totalVisitCount");

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


        return mv;
    }


    //사진 삭제하기
    @PostMapping("/delete-photo")
    public ModelAndView deletePhoto(@RequestParam("photoId") Long photoId) {
        ModelAndView modelAndView = new ModelAndView();

        // PhotoGallery 삭제
        galleryRepository.deleteById(photoId);

        modelAndView.setViewName("redirect:/MiniPicture-view");
        return modelAndView;
    }


//    private List<String> getRecentPosts(User loginUser) {
//        List<String> recentPosts = new ArrayList<>();
//
//        // 최근 게시글 가져오는 로직 구현
//        List<Diary> recentDiaries = diaryRepository.findRecentDiariesByUser(loginUser, 2); // 최근 2개의 다이어리 게시글 가져오기
//        List<PhotoGallery> recentPhotoGalleries = galleryRepository.findRecentPhotoGalleriesByUser(loginUser, 2); // 최근 2개의 사진첩 게시글 가져오기
//
//        // Diary 게시글 제목 추가
//        for (Diary diary : recentDiaries) {
//            recentPosts.add(diary.getTitle());
//        }
//
//        // PhotoGallery 게시글 제목 추가
//        for (PhotoGallery photoGallery : recentPhotoGalleries) {
//            recentPosts.add(photoGallery.getTitle());
//        }
//
//        return recentPosts;
//    }


    @GetMapping("/musicq")
    public ModelAndView musicqq(HttpSession session, Model model) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/MiniHomePage/music");

        User loginUser = (User) session.getAttribute("loginUser");

        Optional<PurchasedProductEntity> music = belongingsService.findById(loginUser.getId())
                .stream()
                .filter(b -> b.getProductEntity().getProductCategory().equals("음악") && b.getApplied() == 'r')
                .findFirst();

        if (music.isPresent()) {
            // 사용하려는 로직을 여기에 적어주세요.
            PurchasedProductEntity repmusic = music.get();
            mv.addObject("representativemusic", repmusic);
        } else {
            // 리스트가 비어 있는 경우의 처리를 여기에 적어주세요.
        }

        List<PurchasedProductEntity> belongingsList = belongingsService.findById(loginUser.getId()).stream().filter(b -> b.getProductEntity().getProductCategory().equals("음악")).filter(z -> z.getApplied() == 'o').collect(Collectors.toList());


        mv.addObject("belongingsList", belongingsList);


        return mv;
    }

    // 0 51 14 이런식으로 하면 14시 51분 0초에 바뀌는거
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void resetTodayVisitCount() {
        List<VisitCount> allVisitCounts = visitCountRepository.findAll();
        for (VisitCount visitCount : allVisitCounts) {
            visitCount.setTodayVisitCount(0);
            visitCountRepository.save(visitCount);
        }
    }






//홈누르면 음앍리스트 리프레쉬되서 그거 막는 메소드. mainmini랑 똑같은데 ${}에 정보 빼는것만 지웠음
    @GetMapping("/gotohomebutdontrefreshmusiclist")
    public ModelAndView blockhomerefresh(HttpSession session, @RequestParam(value = "userId", required = false) Long userId) {
        Boolean fromMiniMain = (Boolean) session.getAttribute("fromMiniMain");
        session.removeAttribute("fromMiniMain");
        System.out.println(fromMiniMain);
        ModelAndView mv = new ModelAndView("/MiniHomePage/Main");
        // If userId is given, use that. Otherwise, use logged in user's ID
        User targetUser = (userId != null) ? userService.getUserById(userId) : (User) session.getAttribute("loginUser");
        // 상태메시지 가져오기
        UserPost userPost = userPostService.getPostsByUser(targetUser);
        System.out.println(userPost);


        mv.addObject("content", targetUser.getUserPost().getContent()); // 기본 상태 메시지


        mv.addObject("loginUser", targetUser);
        // 방문 횟수 정보 가져오기
        VisitCount visitCount = visitCountRepository.findByUser(targetUser);
        // VisitCount 저장
        visitCountRepository.save(visitCount);
        // 모델에 방문 횟수 정보 추가
        mv.addObject("todayVisitCount", visitCount.getTodayVisitCount());
        mv.addObject("totalVisitCount", visitCount.getTotalVisitCount());
        int diaryPage = 0; // 첫 번째 페이지
        int diarySize = 1; // 한 페이지에 보여줄 개수
        Pageable diaryPageable = PageRequest.of(diaryPage, diarySize);
        Page<Diary> recentDiaries = recentDiaryService.getRecentDiariesByUser(targetUser, diaryPageable);
        mv.addObject("recentDiaryPosts", recentDiaries.getContent());
        // 최신 방명록 게시물 가져오기
        int guestBookSize = 1;
        Pageable guestBookPageable = PageRequest.of(0, guestBookSize);
        List<GuestBook> recentGuestBooks = guestBookService.findRecentGuestBooksByUser(targetUser, guestBookPageable);
        mv.addObject("recentGuestBookPosts", recentGuestBooks);
        // 최신 사진첩 게시글 가져오기
        int page = 0; // 첫 번째 페이지
        int size = 1; // 한 페이지에 보여줄 개수
        Pageable pageable = PageRequest.of(page, size);
        List<PhotoGallery> recentPhotoGalleries = galleryRepository.findRecentPhotoGalleriesByUser(targetUser, pageable);
        mv.addObject("recentPhotoGalleryPosts", recentPhotoGalleries);
        //게시글 수 표출 하는 아이들
        LocalDate today = LocalDate.now();
        mv.addObject("todayPostCount", postInfoService.getTodayDiaryCount(targetUser, today));
        mv.addObject("totalPostCount", postInfoService.getTotalDiaryCount(targetUser));
        mv.addObject("todayPhotoCount", postInfoService.getTodayGalleryCount(targetUser, today));
        mv.addObject("totalPhotoCount", postInfoService.getTotalGalleryCount(targetUser));
        mv.addObject("todayGuestbookCount", postInfoService.getTodayGuestbookCount(targetUser, today));
        mv.addObject("totalGuestbookCount", postInfoService.getTotalGuestbookCount(targetUser));
        mv.addObject("todayTotalCount", postInfoService.getTodayTotalCount(targetUser, today));
        mv.addObject("totalTotalCount", postInfoService.getTotalCount(targetUser));
        // 갖고있는 벽지 갖고오는 것
        List<PurchasedProductEntity> wallpaperlist = belongingsService.findById(targetUser.getId())
                .stream()
                .filter(b -> b.getProductEntity().getProductCategory().equals("벽지"))
                .filter(c -> c.getApplied() == 'o')
                .collect(Collectors.toList());
        mv.addObject("wallpaperlist", wallpaperlist);
        // 여기부터는 작은방에 띄워줄 적용된 것들을 갖고옴
        List<PurchasedProductEntity> appliedbelongingsList = belongingsService.findById(targetUser.getId())
                .stream()
                .filter(b -> b.getProductEntity().getProductCategory().equals("가구") || b.getProductEntity().getProductCategory().equals("미니미"))
                .filter(z -> z.getApplied() == 'o')
                .collect(Collectors.toList());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            String appliedbelongingsListJson = objectMapper.writeValueAsString(appliedbelongingsList);

            mv.addObject("appliedbelongingsList", appliedbelongingsListJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Optional<PurchasedProductEntity> music = belongingsService.findById(targetUser.getId())
                .stream()
                .filter(b -> b.getProductEntity().getProductCategory().equals("음악") && b.getApplied() == 'r')
                .findFirst();
        if (music.isPresent()) {








            PurchasedProductEntity repmusic = music.get();
            mv.addObject("representativemusic", repmusic);
        }
        List<PurchasedProductEntity> belongingsList = belongingsService.findById(targetUser.getId())
                .stream()
                .filter(b -> b.getProductEntity().getProductCategory().equals("음악"))
                .filter(z -> z.getApplied() == 'o')
                .collect(Collectors.toList());



        List<Ilchon> friends = ilchonService.findIlchonsByUser(targetUser.getUserNickName());
        List<User> users = new ArrayList<>();

        for(Ilchon friend : friends){
            User friendUser = friend.getYou();
            users.add(friendUser);
        }




        mv.addObject("users", users);
        mv.addObject("belongingsList", belongingsList);
        User user = (User) session.getAttribute("loginUser");
        ImgPath imgPath = imgPathRepository.findByUser(targetUser);















        if (imgPath != null) {
            mv.addObject("profileImg", imgPath.getFileName());
            System.out.println("!!!!!!!!!!!!!!!!!!!!!" + imgPath.getFileName());
        } else {
            // 사용자에 대한 ImgPath 객체가 없는 경우에 대한 처리 로직
            // 예: 기본 이미지 파일 이름을 지정해 모델에 추가
            String defaultImgFileName = "mini.png";
            mv.addObject("profileImg", defaultImgFileName);
        }
//        String musicListUrl = "/musicgo/" + targetUser.getId();
//        System.out.println(musicListUrl);
//        mv.addObject("musicListUrl", musicListUrl);
//        //여기수정한거
//        System.out.println(musicListUrl);
//        System.out.println(musicListUrl);
//        System.out.println(musicListUrl);
        return mv;
    }














}
