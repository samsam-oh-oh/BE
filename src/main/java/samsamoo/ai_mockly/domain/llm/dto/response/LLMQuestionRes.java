package samsamoo.ai_mockly.domain.llm.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LLMQuestionRes {

    private List<String> questionList;
}
