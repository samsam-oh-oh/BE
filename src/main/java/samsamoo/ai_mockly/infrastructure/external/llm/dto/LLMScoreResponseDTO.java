package samsamoo.ai_mockly.infrastructure.external.llm.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LLMScoreResponseDTO {

    private int coreTechUnderstanding;       // 핵심 기술 개념의 이해도
    private int comparisonAnalysis;          // 비교 및 분석 능력
    private int performanceOptimization;     // 성능 최적화 고려
    private int trendAwareness;              // 최신 트렌드 반영
    private int terminologyAccuracy;         // 기술 용어의 정확성과 일관성
    private int projectExplanation;          // 프로젝트 기반 설명력
    private int problemSolvingClarity;       // 문제 해결 과정의 명확성
    private int resultImprovementEffort;     // 결과 도출 및 개선 노력
    private int practicalApplicability;      // 실무 적용 가능성
    private int collaborationContribution;   // 협업 및 기여도 표현
}
