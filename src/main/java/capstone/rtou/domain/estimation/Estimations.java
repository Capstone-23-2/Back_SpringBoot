package capstone.rtou.domain.estimation;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "estimations")
@EntityListeners(AuditingEntityListener.class)
public class Estimations {
    @EmbeddedId
    private EstimationsId id;

    @Column(name = "charactername")
    private String characterName;

    @Column(name = "date")
    @CreationTimestamp
    private LocalDateTime date;

    public Estimations() {

    }

    public Estimations(EstimationsId id, String characterName) {
        this.id = id;
        this.characterName = characterName;
    }
}
