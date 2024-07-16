package toy.ojm.domain.service;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;
import toy.ojm.domain.dto.RestaurantPageableResponseDto;
import toy.ojm.domain.entity.Restaurant;
import toy.ojm.domain.entity.Users;
import toy.ojm.domain.repository.RestaurantRepository;
import toy.ojm.domain.repository.RestaurantSpecifications;
import toy.ojm.domain.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
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
            () -> new IllegalArgumentException("admin user 가 존재하지 않습니다."));
    }

    public RestaurantPageableResponseDto getAllRestaurants(
        String id,
        int page,
        int size,
        String category,
        String keyword) {
        Users user = validateUser(id);
        Pageable pageable = PageRequest.of(page, size);

        Specification<Restaurant> specification = RestaurantSpecifications.alwaysTrue();

        if ("기타".equals(category)) {
            specification = specification.and(
                RestaurantSpecifications.categoryNotIn(Arrays.asList("한식", "일식", "중식", "양식"))
            );
        } else if (!StringUtils.isEmpty(category)) {
            specification = specification.and(
                RestaurantSpecifications.categoryEquals(category)
            );
        }

        if (!StringUtils.isEmpty(keyword)) {
            specification = specification.and(RestaurantSpecifications.nameContains(keyword));
        }

        Page<Restaurant> responseDtoPage = restaurantRepository.findAll(specification, pageable);

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
}

