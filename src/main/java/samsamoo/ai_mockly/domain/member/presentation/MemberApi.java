package samsamoo.ai_mockly.domain.member.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import samsamoo.ai_mockly.domain.member.dto.request.UpdateNicknameReq;
import samsamoo.ai_mockly.domain.member.dto.response.MemberInfoRes;
import samsamoo.ai_mockly.global.common.Message;
import samsamoo.ai_mockly.global.common.SuccessResponse;
import samsamoo.ai_mockly.global.exception.ErrorResponse;

@Tag(name = "Member API", description = "회원 관련 API입니다.")
public interface MemberApi {

    @Operation(summary = "회원 정보 불러오기", description = "회원 정보를 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "회원 정보 불러오기 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MemberInfoRes.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "회원 정보 불러오기 실패(잘못된 요청 방식)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @GetMapping("/{memberId}")
    ResponseEntity<SuccessResponse<MemberInfoRes>> getMemberInfo(
            @Parameter(description = "정보를 불러오고 싶은 회원 아이디를 입력하시오.", required = true) @PathVariable Long memberId);

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
            @Parameter(description = "닉네임을 수정하고 싶은 회원 아이디를 입력하시오.", required = true) @PathVariable Long memberId,
            @Parameter(description = "Schemas의 UpdateNicknameReq 참고", required = true) @RequestBody UpdateNicknameReq request);

    @Operation(summary = "프로필 사진 수정", description = "프로필 사진을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "프로필 사진 수정 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "프로필 사진 수정 실패(잘못된 요청 방식)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @PatchMapping("/{memberId}/image")
    ResponseEntity<SuccessResponse<Message>> modifyProfileImage(
            @Parameter(description = "프로필 이미지를 수정하고 싶은 회원 아이디를 입력하시오.", required = true) @PathVariable Long memberId,
            @Parameter(description = "수정할 이미지 파일을 입력하시오.") @RequestPart MultipartFile profileImage);
}