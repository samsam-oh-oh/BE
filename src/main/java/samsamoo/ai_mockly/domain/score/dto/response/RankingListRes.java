package samsamoo.ai_mockly.domain.score.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RankingListRes {

    @Schema(type = "Long", example = "1", description = "멤버의 아이디 값을 출력합니다.")
    private Long id;

    @Schema(type = "String", example = "닉네임", description = "멤버의 닉네임을 출력합니다.")
    private String nickname;

    @Schema(type = "Double", example = "100.0", description = "멤버의 총 점수를 출력합니다.")
    private Double score;

    @Schema(type = "String", example = "멤버의 **피드백 내용**입니다.", description = "멤버의 피드백 내용을 출력합니다.")
    private String feedback;

    @Schema(type = "Boolean", example = "true", description = "피드백의 잠금 해제 여부를 출력합니다. true면 해제된 상태입니다.")
    private Boolean unlocked;
}
