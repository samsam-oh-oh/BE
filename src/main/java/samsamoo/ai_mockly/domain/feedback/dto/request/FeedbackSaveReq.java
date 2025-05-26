package samsamoo.ai_mockly.domain.feedback.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FeedbackSaveReq {

    @NotBlank(message = "**회원의 면접 결과 리포트 내용**은 필수입니다.")
    @Schema(type = "String", example = "**회원의 면접 결과 리포트 내용**", description = "회원의 면접 결과 리포트 내용을 작성하시오.")
    private String content;
}
