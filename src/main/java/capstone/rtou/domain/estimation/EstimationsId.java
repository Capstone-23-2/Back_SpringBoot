package capstone.rtou.domain.estimation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;


@Embeddable
@Getter
public class EstimationsId implements Serializable {
    @Column(name = "estimationid")
    private String estimationId;

    @Column(name = "userid")
    private String userId;

    public EstimationsId() {
    }

    public EstimationsId(String estimationId, String userId) {
        this.estimationId = estimationId;
        this.userId = userId;
    }
}
