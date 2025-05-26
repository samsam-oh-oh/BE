package samsamoo.ai_mockly.domain.feedback.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedbackContentsRes {

    @Schema(type = "Long", example = "1", description = "면접 결과 리포트의 아이디 값을 출력합니다.")
    private Long id;

    @Schema(type = "String", example = "**회원의 면접 결과 리포트 내용**", description = "저장된 회원의 면접 결과 리포트 내용입니다.")
    private String content;
}
