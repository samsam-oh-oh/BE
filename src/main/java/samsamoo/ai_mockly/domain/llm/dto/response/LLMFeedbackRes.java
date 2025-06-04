package samsamoo.ai_mockly.domain.llm.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LLMFeedbackRes {

    @Schema(type = "List<String>", example = "[**첫번째 피드백**, **두번째 피드백**]", description = "피드백을 각 카테고리에 맞춰 리스트 형태로 출력합니다")
    private List<String> feedbackList;
}
