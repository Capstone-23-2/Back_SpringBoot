package capstone.rtou.domain.estimation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Estimation")
public class Estimation {

    @Id @Column(nullable = false)
    String userId;
    @Column(nullable = false)
    String text;
    @Column
    String word;
    @Column(nullable = false)
    Double AccuracyScore;
    @Column(nullable = false)
    Double PronunciationScore;
    @Column(nullable = false)
    Double FluencyScore;
    @Column(nullable = false)
    Double CompletenessScore;

    static class Words {

        String errorType;

    }
}
