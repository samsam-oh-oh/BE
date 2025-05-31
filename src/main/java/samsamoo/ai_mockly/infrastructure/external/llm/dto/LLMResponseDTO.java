package samsamoo.ai_mockly.infrastructure.external.llm.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LLMResponseDTO {

    private String status;  // 요청 처리 상태(ex: success)
    private String message; // 요청 처리 결과 메시지
    private Object question_status; // 질문 생성 결과
    private Object feedback_status; // 피드백 생성 결과
    private Object score_status;    // 점수 산출 결과
}
