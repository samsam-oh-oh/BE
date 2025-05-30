package samsamoo.ai_mockly.domain.scoredetails.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samsamoo.ai_mockly.domain.common.BaseEntity;
import samsamoo.ai_mockly.domain.score.domain.Score;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "score_details")
public class ScoreDetails extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "score_id")
    private Score score;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(name = "score_value", nullable = false)
    @Min(value = 0) @Max(value = 100)
    private Integer scoreValue;

    @Builder
    public ScoreDetails(Score score, Category category, Integer scoreValue) {
        this.score = score;
        this.category = category;
        this.scoreValue = scoreValue;
    }

    public void setScore(Score score) {
        this.score = score;
    }
}
