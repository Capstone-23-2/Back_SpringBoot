package capstone.rtou.domain.attention;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Attention")
@NoArgsConstructor
public class Attention {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String userId;

    @Column @Getter
    String attendDate;

    public Attention(String userId, String attendDate) {
        this.userId = userId;
        this.attendDate = attendDate;
    }
}
