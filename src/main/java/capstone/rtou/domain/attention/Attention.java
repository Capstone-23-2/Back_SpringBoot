package capstone.rtou.domain.attention;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Table(name = "attention", indexes = @Index(name = "idx_user", columnList = "userid"))
@NoArgsConstructor
public class Attention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "userid", nullable = false)
    String userId;
    @Column(name = "attenddate", nullable = false)
    @Getter
    String date;

    public Attention(String userId, String date) {
        this.userId = userId;
        this.date = date;
    }
}
