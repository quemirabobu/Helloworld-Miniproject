package com.example.miniprogect1;

import com.example.miniprogect1.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MiniProgect1ApplicationTests {

    @Autowired
    private MemberRepository memberRepository;
    @Test
    void contextLoads() {
    }

//    @Test
//    public void join() {
//        Member member = new Member();
//
//        member.setUsername("공주");
//        member.setPassword("1234");
//        member.setUserEmail("abc");
//        member.setUserNickName("pppp");
//        member.setRegDate(LocalDateTime.now());
//
//        memberRepository.save(member);
//    }
}
