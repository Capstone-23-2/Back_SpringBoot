package capstone.rtou.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "userId")})
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "userid",nullable = false)
    private String userId;

    @Column(nullable = false)
    private String sso;
}
