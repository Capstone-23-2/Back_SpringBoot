package capstone.rtou.domain.attention;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Attention", indexes = @Index(name = "userIdIndex", columnList = "userid"))
@NoArgsConstructor
public class Attention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "userid", nullable = false)
    String userId;

    @Column(name = "attenddate", nullable = false)
    @Getter
    String attendDate;

    public Attention(String userId, String attendDate) {
        this.userId = userId;
        this.attendDate = attendDate;
    }
}
