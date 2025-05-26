package samsamoo.ai_mockly.domain.point.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samsamoo.ai_mockly.domain.common.BaseEntity;
import samsamoo.ai_mockly.domain.member.domain.Member;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "point")
public class Point extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private Integer amount;

    private String type;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Enumerated(EnumType.STRING)
    private State state;

    @Builder
    public Point(Member member, Integer amount, String type, State state) {
        this.member = member;
        this.amount = amount;
        this.type = type;
        this.expiredAt = LocalDateTime.now().plusMonths(6);
        this.state = state;
    }

}
