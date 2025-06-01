package samsamoo.ai_mockly.domain.llm.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LLMQuestionRes {

    @Schema(type = "List<String>",
            example = "[\\\"첫번째 질문\\\", \\\"두번째 질문\\\", \\\"세번째 질문\\\", \\\"네번째 질문\\\", \\\"디섯번째 질문\\\"]",
            description = "질문을 리스트 형태로 출력합니다.")
    private List<String> questionList;
}
