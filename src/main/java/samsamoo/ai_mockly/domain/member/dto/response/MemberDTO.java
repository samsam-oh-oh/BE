package samsamoo.ai_mockly.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberDTO {

    @Schema(type = "String", example = "${카카오아이디 번호}", description = "카카오 사용자의 아이디 번호입니다.")
    private String kakaoId;
}
