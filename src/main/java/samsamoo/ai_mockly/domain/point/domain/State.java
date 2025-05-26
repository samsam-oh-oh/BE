package samsamoo.ai_mockly.domain.point.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum State {

    ACTIVE("ACTIVE", "활성"),
    USED("USED", "사용됨"),
    EXPIRED("EXPIRED", "만료됨"),
    CANCELED("CANCELED", "취소됨");

    final private String key;
    final private String value;
}
