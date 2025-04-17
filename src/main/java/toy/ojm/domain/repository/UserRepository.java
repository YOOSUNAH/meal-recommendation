package toy.ojm.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import toy.ojm.domain.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findById(String Id);
}
