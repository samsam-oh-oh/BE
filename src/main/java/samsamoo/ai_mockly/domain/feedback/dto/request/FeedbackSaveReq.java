package samsamoo.ai_mockly.domain.feedback.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FeedbackSaveReq {

    @Schema(type = "String", example = "**회원의 면접 결과 리포트 내용**", description = "회원의 면접 결과 리포트 내용을 작성하시오.")
    private String content;
}
