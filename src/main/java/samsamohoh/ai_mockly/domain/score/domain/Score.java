package samsamohoh.ai_mockly.domain.score.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samsamohoh.ai_mockly.domain.common.BaseEntity;
import samsamohoh.ai_mockly.domain.user.domain.User;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "score")
public class Score extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "total_score", nullable = false)
    @Min(value = 0)
    private Integer totalScore;             // 총 점수(논리성+구체성+사실성)

    @Column(name = "high_score", nullable = false)
    private Boolean highScore;              // true = user의 최고 점수

    @Column(name = "logicality_score", nullable = false)
    @Min(value = 0)
    private Integer logicalityScore;        // 논리성 점수

    @Column(name = "concreteness_score", nullable = false)
    @Min(value = 0)
    private Integer concretenessScore;      // 구체성 점수

    @Column(name = "idealism_score", nullable = false)
    @Min(value = 0)
    private Integer idealismScore;          // 사실성 점수

    @Builder
    public Score(Integer totalScore, Boolean highScore, Integer logicalityScore, Integer concretenessScore, Integer idealismScore) {
        this.totalScore = totalScore;
        this.highScore = highScore;
        this.logicalityScore = logicalityScore;
        this.concretenessScore = concretenessScore;
        this.idealismScore = idealismScore;
    }
}
