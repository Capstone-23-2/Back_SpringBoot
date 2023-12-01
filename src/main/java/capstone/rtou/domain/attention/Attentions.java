package capstone.rtou.domain.attention;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attentions", indexes = @Index(name = "idx_attention", columnList = "userid"))
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Attentions {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "userid", nullable = false)
    String userId;
    @Column(name = "date")
    @Getter
    @CreationTimestamp
    Date date;

    public Attentions(String userId) {
        this.userId = userId;
    }
}
