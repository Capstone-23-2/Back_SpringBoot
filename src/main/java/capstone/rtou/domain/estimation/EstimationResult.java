package capstone.rtou.domain.estimation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "Estimation")
@NoArgsConstructor
@AllArgsConstructor
public class EstimationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "userid", nullable = false)
    String userId;
    @Column(name = "conversationid", nullable = false)
    String conversationId;
    @Column(nullable = false)
    String sentence;
    @Column(name = "accuracyscore", nullable = false)
    Double AccuracyScore;
    @Column(name = "pronunciationscore", nullable = false)
    Double PronunciationScore;
    @Column(name = "fluencyscore", nullable = false)
    Double FluencyScore;
    @Column(name = "completenessscore",nullable = false)
    Double CompletenessScore;

    public EstimationResult(String userId, String conversationId, String sentence, Double accuracyScore, Double pronunciationScore, Double fluencyScore, Double completenessScore) {
        this.userId = userId;
        this.conversationId = conversationId;
        this.sentence = sentence;
        AccuracyScore = accuracyScore;
        PronunciationScore = pronunciationScore;
        FluencyScore = fluencyScore;
        CompletenessScore = completenessScore;
    }
}
