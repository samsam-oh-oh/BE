package samsamoo.ai_mockly.domain.score.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samsamoo.ai_mockly.domain.common.BaseEntity;
import samsamoo.ai_mockly.domain.member.domain.Member;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "score")
public class Score extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "total_score", nullable = false)
    private Integer totalScore;

    @Column(name = "high_score", nullable = false)
    private Boolean highScore;

    @Column(name = "logicality_score")
    private Integer logicalityScore;        // 논리성 점수

    @Column(name = "concreteness_score")
    private Integer concretenessScore;      // 구체성 점수

    @Column(name = "idealism_score")
    private Integer idealismScore;          // 사실성 점수

    @Builder
    public Score(Member member, Integer totalScore, Boolean highScore, Integer logicalityScore, Integer concretenessScore, Integer idealismScore) {
        this.member = member;
        this.totalScore = totalScore;
        this.highScore = highScore;
        this.logicalityScore = logicalityScore;
        this.concretenessScore = concretenessScore;
        this.idealismScore = idealismScore;
    }
}
