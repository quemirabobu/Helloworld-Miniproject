package com.example.miniprogect1.controller;

import com.example.miniprogect1.Service.IlchonService;
import com.example.miniprogect1.domain.Ilchon;
import com.example.miniprogect1.domain.Ilchonpatch;
import com.example.miniprogect1.domain.User;
import com.example.miniprogect1.repository.IlchonpatchRepository;
import com.example.miniprogect1.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ilchon")
public class IlchonController {
    private final MemberRepository memberRepository;
    private final IlchonService ilchonService;
    private final IlchonpatchRepository ilchonpatchRepository;

    @Autowired
    public IlchonController(MemberRepository memberRepository, IlchonService ilchonService, IlchonpatchRepository ilchonpatchRepository) {
        this.memberRepository = memberRepository;
        this.ilchonService = ilchonService;
        this.ilchonpatchRepository = ilchonpatchRepository;
    }

    @PostMapping("/request/{userId}")
    public String sendIlchonRequest(@PathVariable("userId") Long userId, HttpSession session) {
        User requester = (User) session.getAttribute("loginUser");
        User receiver = memberRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));

        Ilchonpatch request = new Ilchonpatch();
        request.setRequester(requester);
        request.setReceiver(receiver);
        request.setStatus("PENDING"); // 승인 대기 상태로 설정

        ilchonService.saveIlchonpatch(request);

        return "일촌 신청이 완료되었습니다.";
    }

    @PostMapping("/approve/{requestId}")
    public String approveIlchonRequest(@PathVariable("requestId") Long requestId, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");

        Ilchonpatch request = ilchonpatchRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request not found"));

        if(!user.equals(request.getReceiver())) {
            return "권한이 없습니다.";
        }

        Ilchon newIlchon = new Ilchon();
        newIlchon.setMe(request.getRequester());
        newIlchon.setYou(request.getReceiver());

        Ilchon newIlchonReversed = new Ilchon();
        newIlchonReversed.setMe(request.getReceiver());
        newIlchonReversed.setYou(request.getRequester());

        ilchonService.saveIlchon(newIlchon);
        ilchonService.saveIlchon(newIlchonReversed);

        request.setStatus("APPROVED"); // 승인 상태로 변경
        ilchonService.saveIlchonpatch(request);

        return "일촌 요청이 승인되었습니다.";
    }

    @PostMapping("/reject/{requestId}")
    public String rejectIlchonRequest(@PathVariable("requestId") Long requestId, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");

        Ilchonpatch request = ilchonpatchRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request not found"));

        if(!user.equals(request.getReceiver())) {
            return "권한이 없습니다.";
        }

        request.setStatus("REJECTED"); // 거절 상태로 변경
        ilchonService.saveIlchonpatch(request);

        return "일촌 요청이 거절되었습니다.";
    }
}