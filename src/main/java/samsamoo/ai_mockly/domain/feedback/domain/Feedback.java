package samsamoo.ai_mockly.domain.feedback.domain;

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
@Table(name = "feedback")
public class Feedback extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder
    public Feedback(Member member, String content) {
        this.member = member;
        this.content = content;
    }
}
