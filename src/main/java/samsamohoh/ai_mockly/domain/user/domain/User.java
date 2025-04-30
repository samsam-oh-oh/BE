package samsamohoh.ai_mockly.domain.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samsamohoh.ai_mockly.domain.common.BaseEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "profile_image")
    private String profileImage;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "state", nullable = false)
    private UserState state;

    @Column(name = "max_score")
    @Min(value = 0)
    private Integer maxScore;

    @Builder
    public User(String nickname, UserState state) {
        this.nickname = nickname;
        this.profileImage = null;
        this.state = state;
        this.maxScore = 0;
    }
}
