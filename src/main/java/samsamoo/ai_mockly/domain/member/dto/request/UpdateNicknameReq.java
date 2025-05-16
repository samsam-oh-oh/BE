package samsamoo.ai_mockly.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateNicknameReq {

    @Schema(type = "String", example = "닉네임", description = "변경할 닉네임을 입력하세요.")
    private String nickname;
}
