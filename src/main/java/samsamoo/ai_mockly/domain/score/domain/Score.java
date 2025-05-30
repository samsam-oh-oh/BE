package samsamoo.ai_mockly.domain.score.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samsamoo.ai_mockly.domain.common.BaseEntity;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.domain.scoredetails.domain.ScoreDetails;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "score", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScoreDetails> scoreDetails = new ArrayList<ScoreDetails>();

    @Builder
    public Score(Member member, Integer totalScore, Boolean highScore) {
        this.member = member;
        this.totalScore = totalScore;
        this.highScore = highScore;
    }

    public void addScoreDetails(ScoreDetails scoreDetails) {
        this.scoreDetails.add(scoreDetails);
        scoreDetails.setScore(this);
    }
}
