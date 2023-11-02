package capstone.rtou.domain.estimation;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Estimation")
@NoArgsConstructor
public class Estimation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String userId;
    @Column(nullable = false)
    String dateWithTime;
    @Column(nullable = false)
    String text;
    @Column
    String error; // errorWord/errorType 형태로 저장할 것.
    @Column(nullable = false)
    Double AccuracyScore;
    @Column(nullable = false)
    Double PronunciationScore;
    @Column(nullable = false)
    Double FluencyScore;
    @Column(nullable = false)
    Double CompletenessScore;

    public Estimation(String userId, String dateWithTime, String text, Double accuracyScore, Double pronunciationScore, Double fluencyScore, Double completenessScore) {
        this.userId = userId;
        this.dateWithTime = dateWithTime;
        this.text = text;
        AccuracyScore = accuracyScore;
        PronunciationScore = pronunciationScore;
        FluencyScore = fluencyScore;
        CompletenessScore = completenessScore;
    }

    public Estimation(String userId, String dateWithTime, String text, String error, Double accuracyScore, Double pronunciationScore, Double fluencyScore, Double completenessScore) {
        this.userId = userId;
        this.dateWithTime = dateWithTime;
        this.text = text;
        this.error = error;
        AccuracyScore = accuracyScore;
        PronunciationScore = pronunciationScore;
        FluencyScore = fluencyScore;
        CompletenessScore = completenessScore;
    }
}
