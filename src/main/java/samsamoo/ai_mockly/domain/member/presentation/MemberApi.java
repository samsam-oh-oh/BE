package samsamoo.ai_mockly.domain.member.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import samsamoo.ai_mockly.domain.member.dto.request.UpdateNicknameReq;
import samsamoo.ai_mockly.global.common.Message;
import samsamoo.ai_mockly.global.common.SuccessResponse;
import samsamoo.ai_mockly.global.exception.ErrorResponse;

@Tag(name = "MemberApi", description = "회원 관련 API입니다.")
public interface MemberApi {

    @Operation(summary = "닉네임 수정", description = "닉네임을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "닉네임 수정 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "닉네임 수정 실패(잘못된 요청 방식)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @PatchMapping("/{memberId}/nickname")
    ResponseEntity<SuccessResponse<Message>> updateNickname(
            @Parameter(description = "닉네임을 수정하고 싶은 멤버 아이디를 입력하시오.", required = true) @PathVariable Long memberId,
            @Parameter(description = "Schemas의 UpdateNicknameReq 참고", required = true) @RequestBody UpdateNicknameReq request);
}
