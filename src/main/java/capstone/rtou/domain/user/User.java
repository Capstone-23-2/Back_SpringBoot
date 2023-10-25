package capstone.rtou.domain.user;

import jakarta.persistence.*;
import lombok.Setter;

/**
 * User DB
 */
@Entity
@Setter
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "userId")})
public class User {

    @Id
    @Column(nullable = false, length = 15, unique = true)
    private String userId;
}
