package samsamoo.ai_mockly.domain.member.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberInfoRes {

    private String nickname;

    private String profileImage;

    private Integer maxScore;

    private Integer pointAmount;
}
