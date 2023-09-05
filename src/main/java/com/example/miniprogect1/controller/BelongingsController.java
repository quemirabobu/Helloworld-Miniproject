package com.example.miniprogect1.controller;


import com.example.miniprogect1.Service.BelongingsService;
import com.example.miniprogect1.domain.ProductEntity;
import com.example.miniprogect1.domain.PurchasedProductEntity;
import com.example.miniprogect1.domain.ResponseDTO;
import com.example.miniprogect1.domain.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpSession;
import org.aspectj.weaver.Lint;
import org.aspectj.weaver.LintMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class BelongingsController {

    BelongingsService belongingsService;
    @Autowired
    public BelongingsController(BelongingsService belongingsService){
        this.belongingsService = belongingsService;
    }
    @GetMapping("/haha")
    public ModelAndView haha(HttpSession session){
        ModelAndView mv = new ModelAndView();
        User cyuser = (User) session.getAttribute("loginUser");
        mv.addObject("session",session);
        System.out.println(session);
        System.out.println(session.getAttribute("loginUser"));
        System.out.println(cyuser.getId());
//        List<PurchasedProductEntity> purchasedProductEntityList = belongingsService.findAll();
//        System.out.println(purchasedProductEntityList);
//        List<PurchasedProductEntity> k = belongingsService.findById(cyuser.getId());
//        mv.addObject("k",k);
/// 적용된 벽지 가져오는거

        List  <PurchasedProductEntity> wallpaperlist = belongingsService.findById(cyuser.getId()).stream().filter(b->b.getProductEntity().getProductCategory().equals("벽지")).filter(c->c.getApplied()=='o').collect(Collectors.toList());


        mv.addObject("wallpaperlist",wallpaperlist);
        List<PurchasedProductEntity> belongingsList = belongingsService.findById(cyuser.getId()).stream().filter(b->b.getProductEntity().getProductCategory().equals("가구")).collect(Collectors.toList());
////아래 가구 리스트 가져오는거

        mv.addObject("belongingsList",belongingsList);
///여기부터는 작은방에 띄워줄 적용된거 갖고오는거
        List<PurchasedProductEntity>appliedbelongingsList =  belongingsService.findById(cyuser.getId()).stream().filter(b->b.getProductEntity().getProductCategory().equals("가구")||b.getProductEntity().getProductCategory().equals("미니미")).filter(z->z.getApplied()=='o').collect(Collectors.toList());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            String appliedbelongingsListJson = objectMapper.writeValueAsString(appliedbelongingsList);

            mv.addObject("appliedbelongingsList", appliedbelongingsListJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
//session의 Id, category가 2, 인거 가지고와서 belonginglist에 넣기
        List<Map<String, Object>> belongingList = belongingsService.getBelongingList(cyuser.getId());
//        System.out.println(k);

        mv.addObject("belongingList",belongingList);
//mv.addObject("purchasedProductEntityList",purchasedProductEntityList);


        if (!belongingList.isEmpty() && belongingList.size() > 1) {
            Object a = belongingList.get(1);
            mv.addObject("haha", a);
        } else {

            // belongingList가 비어있거나, 원소가 1개 미만일 경우의 처리
        }




        mv.setViewName("/haha.html");
        return mv;
    }


    ////적용하기 버튼을 눌렀을때 생기는 일
    @PostMapping("/product-apply")
    public ResponseEntity<?> productapply(@RequestParam int productId, HttpSession session){
        User cyuser = (User) session.getAttribute("loginUser");



        ResponseDTO<PurchasedProductEntity> responseDTO = new ResponseDTO<>();
        Map<String,String> returnmap = new HashMap<>();

        try {
            PurchasedProductEntity purchasedProductEntity = belongingsService.getpurchasedProductEntity(productId, cyuser.getId());





            System.out.println(purchasedProductEntity);
            responseDTO.setItem(purchasedProductEntity);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }


    }




//적용하기 누르면 db에 o로 저장되게하는거

    @PostMapping("/productdb-apply")
    public ResponseEntity<?> productdbapply(@RequestParam int productId, HttpSession session){
        ResponseDTO<Map<String,String>> responseDTO = new ResponseDTO<>();
        Map<String,String> returnmap = new HashMap<>();
        User cyuser = (User) session.getAttribute("loginUser");

        try {

            belongingsService.productapply(productId, cyuser.getId() );
            returnmap.put("msg","applied");
            responseDTO.setItem(returnmap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.OK.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }



    //    o를 x로 만드는거
    @PostMapping("/productdb-deapply")
    public ResponseEntity<?> productdbdeapply(@RequestParam int productId, HttpSession session){
        ResponseDTO<Map<String,String>> responseDTO = new ResponseDTO<>();
        Map<String,String> returnmap = new HashMap<>();
        User cyuser = (User) session.getAttribute("loginUser");

        try {

            belongingsService.productdeapply(productId, cyuser.getId() );
            returnmap.put("msg","deapplied");
            responseDTO.setItem(returnmap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.OK.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    ///미니미파트
    @GetMapping("/minimi-decoration")
    public ModelAndView minimiview( HttpSession session){
        ModelAndView mv = new ModelAndView();
        User cyuser = (User) session.getAttribute("loginUser");

        List<PurchasedProductEntity> belongingsList = belongingsService.findById(cyuser.getId()).stream().filter(b->b.getProductEntity().getProductCategory().equals("미니미")).collect(Collectors.toList());
        mv.addObject("belongingsList",belongingsList);

        List  <PurchasedProductEntity> appliedbelongingsList = belongingsService.findById(cyuser.getId()).stream().filter(b->b.getProductEntity().getProductCategory().equals("미니미")).filter(c->c.getApplied()=='o').collect(Collectors.toList());



        List<Object[]> top3minimi = belongingsService.findtop3minimi();
        List<ProductEntity> top3productentity = new ArrayList<>();
        for (Object[] result : top3minimi) {
            Long pId = (Long) result[0]; // assuming the first column is p_id and is of type Long
            Long pcId = (Long) result[1]; // assuming the second column is pc_id and is of type Long
            String productCategory = (String) result[2];
            String productName = (String) result[3];
            int productPrice = (int) result[4];
            String productFilePath = (String) result[5];
            String productFileName = (String) result[6];
            int coordinateX = (int) result[7];
            int coordinateY = (int) result[8];
            ProductEntity productEntity = new ProductEntity();
            productEntity.setPId(pId);
            productEntity.setPcId(pcId);
            productEntity.setProductCategory(productCategory);
            productEntity.setProductName(productName);
            productEntity.setProductPrice(productPrice);
            productEntity.setProductFilePath(productFilePath);
            productEntity.setProductFileName(productFileName);
            productEntity.setCoordinateX(coordinateX);
            productEntity.setCoordinateY(coordinateY);
            top3productentity.add(productEntity);

            // so on for other columns and their types...
        }
        System.out.println(top3productentity);
        mv.addObject("top3productentity",top3productentity);







        mv.addObject("appliedbelongingsList", appliedbelongingsList);


        mv.setViewName("minimidecoration.html");

        return mv;

    }




    @GetMapping("/deco-wallpaper")
    public ModelAndView decowallpaperview( HttpSession session){
        User cyuser = (User) session.getAttribute("loginUser");
        ModelAndView mv = new ModelAndView();
        List<PurchasedProductEntity> belongingsList =belongingsService.findById(cyuser.getId()).stream().filter(b->b.getProductEntity().getProductCategory().equals("벽지")).collect(Collectors.toList());
        mv.addObject("belongingsList",belongingsList);
        List<PurchasedProductEntity>appliedbelongingsList = belongingsService.findById(cyuser.getId()).stream().filter(b->b.getProductEntity().getProductCategory().equals("가구")||b.getProductEntity().getProductCategory().equals("미니미")).filter(z->z.getApplied()=='o').collect(Collectors.toList());


        List<PurchasedProductEntity>wallpaperlist = belongingsService.findById(cyuser.getId()).stream().filter(b->b.getProductEntity().getProductCategory().equals("벽지")).filter(z->z.getApplied()=='o').collect(Collectors.toList());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            String appliedbelongingsListJson = objectMapper.writeValueAsString(appliedbelongingsList);

            mv.addObject("appliedbelongingsList", appliedbelongingsListJson);
            mv.addObject("wallpaperlist",wallpaperlist);
            mv.setViewName("decowallpaper.html");

            System.out.println(appliedbelongingsList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return mv;

    }





    @GetMapping("/myroombgm")
    public ModelAndView myroombgm( HttpSession session){
        ModelAndView mv = new ModelAndView();
        String appliedbelongingsListJson = "";
        User cyuser = (User) session.getAttribute("loginUser");

        List<PurchasedProductEntity> belongingsList =belongingsService.findById(cyuser.getId()).stream().filter(b->b.getProductEntity().getProductCategory().equals("음악")).collect(Collectors.toList());

        PurchasedProductEntity appliedmusic = belongingsService.findById(cyuser.getId()).stream()
                .filter(b -> b.getProductEntity().getProductCategory().equals("음악"))
                .filter(b -> b.getApplied() == 'o')
                .findFirst().orElse(new PurchasedProductEntity());

        List<PurchasedProductEntity> appliedmusiclist = belongingsService.findById(cyuser.getId()).stream().filter(b-> b.getProductEntity().getProductCategory().equals("음악")).filter(b->b.getApplied()=='o'||b.getApplied() =='r').collect(Collectors.toList());


        PurchasedProductEntity representativemusic = belongingsService.findById(cyuser.getId()).stream()
                .filter(b -> b.getProductEntity().getProductCategory().equals("음악"))
                .filter(b -> b.getApplied() == 'r')
                .findFirst().orElse(new PurchasedProductEntity());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());



        /////여기수정함
        /////여기수정함
        /////여기수정함
        /////여기수정함
        /////여기수정함
        /////여기수정함
        /////여기수정함
































        List<Object[]> top5music = belongingsService.findtop5music();
        List<ProductEntity> top5productentity = new ArrayList<>();
        for (Object[] result : top5music) {
            Long pId = (Long) result[0]; // assuming the first column is p_id and is of type Long
            Long pcId = (Long) result[1]; // assuming the second column is pc_id and is of type Long
            String productCategory = (String) result[2];
            String productName = (String) result[3];
            int productPrice = (int) result[4];
            String productFilePath = (String) result[5];
            String productFileName = (String) result[6];
            int coordinateX = (int) result[7];
            int coordinateY = (int) result[8];
            ProductEntity productEntity = new ProductEntity();
            productEntity.setPId(pId);
            productEntity.setPcId(pcId);
            productEntity.setProductCategory(productCategory);
            productEntity.setProductName(productName);
            productEntity.setProductPrice(productPrice);
            productEntity.setProductFilePath(productFilePath);
            productEntity.setProductFileName(productFileName);
            productEntity.setCoordinateX(coordinateX);
            productEntity.setCoordinateY(coordinateY);
            top5productentity.add(productEntity);

            // so on for other columns and their types...
        }
        System.out.println(top5productentity);




        mv.addObject("top5productentity",top5productentity);
        /////여기수정함
        /////여기수정함
        /////여기수정함
        /////여기수정함
        /////여기수정함
        /////여기수정함
        /////여기수정함
        /////여기수정함
        /////여기수정함

//        System.out.println(top5music);


        try {
            appliedbelongingsListJson = objectMapper.writeValueAsString(representativemusic);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        mv.addObject("representativemusic2",appliedbelongingsListJson);

        mv.addObject("belongingsList",belongingsList);
        mv.addObject("appliedmusiclist",appliedmusiclist);
        mv.addObject("appliedmusic",appliedmusic);
        mv.addObject("representativemusic",representativemusic);
        mv.setViewName("myroombgm.html");
        return mv;
    }




//    getpurchasedProductEntity(int productId, long Id)


//    PurchasedProductEntity purchasedProductEntity = belongingsService.getpurchasedProductEntity(productId, cyuser.getId());

    @PostMapping("/musicapplyforplay")
    public ResponseEntity<?> musicapplyforplay(@RequestParam int productId, HttpSession session){
        ResponseDTO<PurchasedProductEntity> responseDTO = new ResponseDTO<>();
        User cyuser = (User) session.getAttribute("loginUser");
        try {
            PurchasedProductEntity belongings = belongingsService.getpurchasedProductEntity(productId, cyuser.getId());



            responseDTO.setItem(belongings);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return  ResponseEntity.badRequest().body(responseDTO);
        }
    }


    @PostMapping("/makeitrepresentativemusic")
    public ResponseEntity<?> makeitrepresentativemusic(@RequestParam int productId, HttpSession session){
        ResponseDTO<Map<String,String>> responseDTO = new ResponseDTO<>();
        User cyuser = (User) session.getAttribute("loginUser");

        Map<String,String> returnmap = new HashMap<>();
        try {

            belongingsService.makeitrepresentativemusic(productId, cyuser.getId());
            returnmap.put("msg","applied");
            responseDTO.setItem(returnmap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.OK.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    ////////////////////////////////////////





}
