package capstone.rtou.domain.estimation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "estimationscores", indexes = @Index(name = "idx_scores", columnList = "userid, estimationid, sentence"))
@NoArgsConstructor
public class EstimationScores {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "userid", nullable = false)
    String userId;
    @Column(name = "estimationid", nullable = false)
    String estimationId;
    @Column(name = "sentence", nullable = false)
    String sentence;
    @Column(name = "accuracy", nullable = false)
    Double accuracy;
    @Column(name = "prosody", nullable = false)
    Double prosody;
    @Column(name = "pronunciation", nullable = false)
    Double pronunciation;
    @Column(name = "fluency", nullable = false)
    Double fluency;
    @Column(name = "completeness",nullable = false)
    Double completeness;

    public EstimationScores(String userId, String estimationId, String sentence, Double accuracy, Double prosody, Double pronunciation, Double fluency, Double completeness) {
        this.userId = userId;
        this.estimationId = estimationId;
        this.sentence = sentence;
        this.accuracy = accuracy;
        this.prosody = prosody;
        this.pronunciation = pronunciation;
        this.fluency = fluency;
        this.completeness = completeness;
    }
}
