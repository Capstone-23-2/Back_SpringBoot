package capstone.rtou.domain.attention;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attention", indexes = @Index(name = "idx_user", columnList = "userid"))
@NoArgsConstructor
public class Attention {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "userid", nullable = false)
    String userId;
    @Column(name = "date", nullable = false, columnDefinition = "varchar(8)")
    @Getter
    String date;

    public Attention(String userId, String date) {
        this.userId = userId;
        this.date = date;
    }
}
