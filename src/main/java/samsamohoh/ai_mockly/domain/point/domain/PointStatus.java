package samsamohoh.ai_mockly.domain.point.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PointStatus {
    ACTIVE("Active", "사용 가능"),
    USED("Used", "사용됨"),
    EXPIRED("Expired", "사용 만료"),
    CANCELLED("Canceled", "취소됨");

    final String key;
    final String value;
}
