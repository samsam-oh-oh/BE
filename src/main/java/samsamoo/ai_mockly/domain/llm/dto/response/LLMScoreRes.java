package samsamoo.ai_mockly.domain.llm.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class LLMScoreRes {

    private Map<String, Integer> scoreMap;
}
