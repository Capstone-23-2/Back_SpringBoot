package capstone.rtou.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "userid")})
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "userid",nullable = false)
    private String userId;

    @Column(name = "sso", nullable = false)
    private String sso;
}
