package toy.ojm.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.ojm.domain.dto.RestaurantPageableResponseDto;
import toy.ojm.domain.entity.Restaurant;
import toy.ojm.domain.entity.Users;
import toy.ojm.domain.repository.RestaurantQueryRepository;
import toy.ojm.domain.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final RestaurantQueryRepository restaurantQueryRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public void login(
            String id,
            String password) {
        Users user = validateUser(id);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Transactional
    public void encodePassword(
            String id,
            String password) {
        Users user = validateUser(id);
        user.updatePassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public RestaurantPageableResponseDto getAllRestaurants(
            String id,
            int page,
            int size,
            String category,
            String keyword) {
        validateUser(id);
        Pageable pageable = PageRequest.of(page, size);

        Page<Restaurant> responseDtoPage = restaurantQueryRepository.findByCategoryAndKeyword(pageable, category, keyword);

        int totalPage = responseDtoPage.getTotalPages();
        int totalElements = (int) responseDtoPage.getTotalElements();

        return new RestaurantPageableResponseDto(
                responseDtoPage.getContent(),
                size,
                page + 1,
                totalPage,
                totalElements
        );
    }

    private Users validateUser(String id) {
        return userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("admin user 가 존재하지 않습니다."));
    }
}

