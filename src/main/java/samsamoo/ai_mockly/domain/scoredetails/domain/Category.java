package samsamoo.ai_mockly.domain.scoredetails.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum Category {

    CORE_TECH_UNDERSTANDING("CORE_TECH_UNDERSTANDING", "핵심 기술 개념의 이해도"),
    COMPARISON_ANALYSIS("COMPARISON_ANALYSIS", "비교 및 분석 능력"),
    PERFORMANCE_OPTIMIZATION("PERFORMANCE_OPTIMIZATION", "성능 최적화 고려"),
    TREND_AWARENESS("TREND_AWARENESS", "최신 트렌드 반영"),
    TERMINOLOGY_ACCURACY("TERMINOLOGY_ACCURACY", "기술 용어의 정확성과 일관성"),
    PROJECT_EXPLANATION("PROJECT_EXPLANATION", "프로젝트 기반 설명력"),
    PROBLEM_SOLVING_CLARITY("PROBLEM_SOLVING_CLARITY", "문제 해결 과정의 명확성"),
    RESULT_IMPROVEMENT_EFFORT("RESULT_IMPROVEMENT_EFFORT", "결과 도출 및 개선 노력"),
    PRACTICAL_APPLICABILITY("PRACTICAL_APPLICABILITY", "실무 적용 가능성"),
    COLLABORATION_CONTRIBUTION("COLLABORATION_CONTRIBUTION", "협업 및 기여도 표현");

    private final String key;
    private final String value;

    private static final Map<String, Category> VALUE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(Category::getValue, Function.identity()));

    public static Category fromValue(String value) {
        Category category = VALUE_MAP.get(value);
        if(category == null) {
            throw new IllegalArgumentException("해당 value에 해당하는 Category가 없습니다: " + value);
        }
        return category;
    }
}
