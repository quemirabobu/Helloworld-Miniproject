package com.example.miniprogect1.Service;

import com.example.miniprogect1.domain.GuestBook;
import com.example.miniprogect1.domain.User;
import com.example.miniprogect1.repository.GuestBookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service // 이 클래스가 스프링의 서비스 레이어에 속하는 것임을 선언
public class GuestBookService {
    private final GuestBookRepository guestBookRepository; // 게시판과 관련된 데이터베이스 작업을 수행하는 repository 선언

    // 서비스 생성자
    public GuestBookService(GuestBookRepository guestBookRepository) {
        this.guestBookRepository = guestBookRepository;
    }

    public List<GuestBook> getBoardsByOwner(User owner) {
        // owner의 게시글 조회 로직 구현
        return guestBookRepository.findByOwner(owner); // repository의 findByOwner 메소드를 호출해 소유자의 게시글을 조회
    }


    public Page<GuestBook> getBoardsByOwner(User owner, Pageable pageable) {
        return guestBookRepository.findByOwner(owner, pageable);
    }
    public GuestBook createBoard(User writer, User owner, String content, LocalDateTime regdate) {
        GuestBook board = new GuestBook(); // 새로운 GuestBook 객체 생성
        board.setContent(content); // 게시글의 내용 설정
        board.setRegdate(regdate); // 게시글의 등록 날짜 설정
        board.setWriter(writer); // 게시글의 작성자 설정
        board.setOwner(owner); // 게시글의 소유자 설정

        // 게시글을 저장하고, 저장된 객체를 반환
        // 수정된 save 메소드를 사용하여 게시글 저장
        return this.save(board);
    }
    public GuestBook save(GuestBook guestBook) {
        // 이전에 해당 유저가 작성한 마지막 게시글의 userPostId를 조회
        Long lastPostId = guestBookRepository.findLastUserPostIdByOwnerId(guestBook.getOwner().getId());
        if (lastPostId == null) lastPostId = 0L; // 아직 게시글을 작성하지 않은 경우에는 0으로 설정

        // 현재 게시글의 userPostId를 설정
        guestBook.setUserPostId(lastPostId + 1);

        // 게시글을 저장하고, 저장된 게시글을 반환
        return guestBookRepository.save(guestBook);
    }

    //최신게시물 띄우기 위해 만듬
    //updateNews
    public List<GuestBook> findRecentGuestBooksByUser(User loginUser, Pageable pageable) {
        return guestBookRepository.findRecentGuestBooksByUser(loginUser, pageable);
    }


}
