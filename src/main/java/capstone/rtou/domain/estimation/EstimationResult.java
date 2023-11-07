package capstone.rtou.domain.estimation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "estimations", indexes = @Index(name = "idx_estimation", columnList = "userid"))
@NoArgsConstructor
public class EstimationResult {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "userid", nullable = false)
    String userId;
    @Column(nullable = false)
    String sentence;
    @Column(name = "accuracy", nullable = false)
    Double accuracy;
    @Column(name = "pronunciation", nullable = false)
    Double pronunciation;
    @Column(name = "fluency", nullable = false)
    Double fluency;
    @Column(name = "completeness",nullable = false)
    Double completeness;

    public EstimationResult(String userId, String sentence, Double accuracy, Double pronunciation, Double fluency, Double completeness) {
        this.userId = userId;
        this.sentence = sentence;
        this.accuracy = accuracy;
        pronunciation = pronunciation;
        this.fluency = fluency;
        this.completeness = completeness;
    }
}
