package samsamoo.ai_mockly.domain.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samsamoo.ai_mockly.domain.common.BaseEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "members")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kakao_id", nullable = false)
    private String kakaoId;

    private String nickname;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "max_level")
    private Integer maxLevel;

    @Builder
    public Member(String kakaoId, String nickname, String profileImage, Integer maxLevel) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.maxLevel = maxLevel;
    }
}
