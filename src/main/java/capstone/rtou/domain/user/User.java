package capstone.rtou.domain.user;

import jakarta.persistence.*;
import lombok.Setter;

/**
 * User DB
 */
@Entity
@Setter
@Table(name = "User", uniqueConstraints = {@UniqueConstraint(columnNames = "userId")})
public class User {

    @Id
    @Column(nullable = false, unique = true)
    private String userId;
}
