package samsamohoh.ai_mockly.domain.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserState {

    GUEST("Guest", "게스트 사용자"),
    KAKAO("Kakao", "카카오 사용자");

    final String key;
    final String value;
}
