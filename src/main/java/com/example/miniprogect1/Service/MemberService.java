package com.example.miniprogect1.Service;

import com.example.miniprogect1.domain.ImgPath;
import com.example.miniprogect1.domain.User;
import com.example.miniprogect1.repository.MemberRepository;
import com.example.miniprogect1.repository.PathRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PathRepository pathRepository;

    //join함수 실행 ->회원가입 알아서 저장
    public void join(User user) {
        memberRepository.save(user);
    }
    public User getid(String nickname)
    {
        return memberRepository.findByUserNickName(nickname);
    }
    //아이디 체크하는 곳
    public int idChk(String id) {

        if (memberRepository.findByUserName(id) != null) {
            return 1;
        }
        return 0;

    }

    //닉네임 체크하는 곳
    public int nChk(String nickname) {
        if (memberRepository.findByUserNickName(nickname) != null) {
            return 1; // 닉네임이 이미 존재하는 경우
        }
        return 0; // 닉네임이 존재하지 않는 경우
    }


    ////비번 체크하는 곳
    public int pwChk(String pw) {
        if (memberRepository.findByPassword(pw) != null) {
            return 1;
        }
        return 0;
    }

    //로그인 인증 체크
    //회원가입된 유저가 맞는지 검사할 메소드
    public boolean authenticate(String userName, String password) {
        User user = memberRepository.findByUserName(userName);

        // 조회된 회원 정보가 없거나 비밀번호가 일치하지 않는 경우
        if (user == null || !user.getPassword().equals(password)) {
            return false; // 인증 실패
        }

        return true; // 인증 성공

    }


    /*민규 user서비스*/

    // 데이터 불러오기
    public Optional<User> userIDget(Long id) {
        return memberRepository.findById(id);
    }


    //이미지 덮어쓰기
    public void tempimg(ImgPath imgPath) {
        pathRepository.save(imgPath);
    }

//    @Transactional(readOnly = true)
//    public User getCurrentUser() {
//        // 로그인한 사용자의 아이디를 가져옵니다. 실제 구현에서는 적절한 인증 정보를 사용해야 합니다.
//        String userId = "testUser";
//        return userRepository.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));
//    }
}
