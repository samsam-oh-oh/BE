package samsamoo.ai_mockly.domain.llm.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class LLMScoreRes {

    @Schema(type = "HashMap",
            example = "{**카테고리명** : **점수**, **카테고리명** : **점수**}",
            description = "면접 점수 세부정보를 출력합니다.")
    private Map<String, Integer> scoreMap;

    @Schema(type = "Double", example = "85.35", description = "첫번째 묶음인 기술 전문성 및 문제 해결 능력 카테고리의 점수입니다.")
    private double techScore;

    @Schema(type = "Double", example = "52.12", description = "두번째 묶음인 표현력 및 커뮤니케이션 역량 카테고리의 점수입니다.")
    private double communicateScore;

    @Schema(type = "Double", example = "75.38", description = "두 개의 카테고리를 조화 평균으로 계산한 총 점수입니다.")
    private double totalScore;
}
