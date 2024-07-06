package toy.ojm.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.ojm.domain.entity.Users;
import toy.ojm.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    // 자바 패스워드 암호화 라이브러리

    @Transactional
    public void login(String id, String password) {

        Users user = userRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("User not found")
        );

        if (!password.equals(user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
    }
}
