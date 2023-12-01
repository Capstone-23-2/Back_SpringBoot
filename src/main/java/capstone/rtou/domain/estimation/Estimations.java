package capstone.rtou.domain.estimation;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Entity
@Table(name = "estimations")
@EntityListeners(AuditingEntityListener.class)
public class Estimations {
    @Getter
    @EmbeddedId
    EstimationsId id;

    @Getter
    @Column(name = "date")
    @CreationTimestamp
    LocalDateTime date;

    public Estimations() {

    }

    public Estimations(EstimationsId id) {
        this.id = id;
    }
}
