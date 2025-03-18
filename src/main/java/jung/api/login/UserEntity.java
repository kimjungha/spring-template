package jung.api.login;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 사용
    private Long id;

    @Column(nullable = false, unique = true, length = 50) // 중복 불가
    private String username;

    @Column(nullable = false)
    private String password; // BCrypt 암호화된 비밀번호 저장

    @Column(nullable = false, length = 20)
    private String role; // 예: ROLE_USER, ROLE_ADMIN

    @Column(nullable = false, unique = true, length = 100) // 이메일 중복 방지
    private String email;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();

    }
}
