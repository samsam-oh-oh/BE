package samsamoo.ai_mockly.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberInfoRes {

    @Schema(type = "String", example = "닉네임", description = "사용자의 닉네임입니다.")
    private String nickname;

    @Schema(type = "String", example = "profile.png", description = "사용자의 프로필 이미지입니다.")
    private String profileImage;

    @Schema(type = "Integer", example = "15", description = "사용자의 피드백 점수 중 최고점입니다. 최소 0, 최대 15입니다.")
    private Integer maxScore;

    @Schema(type = "Integer", example = "15", description = "사용자의 총 포인트량 입니다.")
    private Integer pointAmount;
}
