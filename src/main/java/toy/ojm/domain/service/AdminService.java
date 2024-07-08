package toy.ojm.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.ojm.domain.entity.Users;
import toy.ojm.domain.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public void login(String id, String password) {
        Users user = validateUser(id);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Transactional
    public void encodePassword(String id, String password) {
        Users user = validateUser(id);
        user.updatePassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public Users validateUser(String id) {
        return userRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("User 가 존재하지 않습니다."));
    }

}
