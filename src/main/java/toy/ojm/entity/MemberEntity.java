package toy.ojm.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "MemberEntity")

public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;

    // getter, setter, 기타 생성자 등
}