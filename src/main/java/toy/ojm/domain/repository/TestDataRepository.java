package toy.ojm.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.ojm.domain.entity.TestData;

import java.util.List;
import java.util.Optional;

public interface TestDataRepository extends JpaRepository<TestData, Long> {

    Optional<TestData> findByUniqueKey (String uniqueKey);

    List<TestData> findAllByUniqueKeyIn(List<String> uniqueKeys);
}
