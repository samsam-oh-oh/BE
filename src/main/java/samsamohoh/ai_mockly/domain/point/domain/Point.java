package samsamohoh.ai_mockly.domain.point.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samsamohoh.ai_mockly.domain.common.BaseEntity;
import samsamohoh.ai_mockly.domain.user.domain.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "point")
public class Point extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "type", columnDefinition = "TEXT", nullable = false)
    private String type;

    @Column(name = "expired_date", nullable = false)
    private LocalDateTime expiredDate;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PointStatus status;

    @Builder
    public Point(User user, Integer amount, String type, LocalDateTime expiredDate, PointStatus status) {
        this.user = user;
        this.amount = amount;
        this.type = type;
        this.expiredDate = expiredDate;
        this.status = status;
    }
}
