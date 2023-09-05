package com.example.miniprogect1.Service;


import com.example.miniprogect1.domain.User;
import com.example.miniprogect1.repository.UserRepository;
import com.example.miniprogect1.repository.UserRepository2;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository2 userRepository2;
    public void signup(User User) {

        userRepository2.save(User);
    }

    public User login(User User) {
        Optional<User> ue =userRepository2.findByUsername(User.getUserName());
        if(ue.isPresent()) {
            User userEntity = ue.get();
            if(userEntity.getPassword().equals(User.getPassword())) {
                return userEntity;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void saveBamtori(User loginUser) {
        userRepository2.saveBamtori(loginUser.getId(), loginUser.getBamtori());
    }
    public User getUserById(Long id) {
        return userRepository2.findById(id).orElse(null); // userRepository의 findById 메소드를 호출해 사용자 조회 후 반환, 없을 경우 null 반환
    }

    public List<User> getAllUsers() {
        return userRepository2.findAll(); // userRepository의 findAll 메소드를 호출해 모든 사용자 조회 후 반환
    }
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository2.findAll(pageable);
    }
}
