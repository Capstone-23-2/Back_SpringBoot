package capstone.rtou.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User DB
 */
@Entity
@Table(name = "User", uniqueConstraints = {@UniqueConstraint(columnNames = "userId")})
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String sso;
}
