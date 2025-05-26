package samsamoo.ai_mockly.domain.auth.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import samsamoo.ai_mockly.domain.auth.dto.request.LoginReq;
import samsamoo.ai_mockly.domain.auth.dto.request.LogoutReq;
import samsamoo.ai_mockly.domain.auth.dto.response.DuplicateCheckRes;
import samsamoo.ai_mockly.domain.auth.dto.response.LoginRes;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.global.annotation.LoginMember;
import samsamoo.ai_mockly.global.common.Message;
import samsamoo.ai_mockly.global.common.SuccessResponse;
import samsamoo.ai_mockly.global.exception.ErrorResponse;

@Tag(name = "Auth API", description = "인증 관련 API입니다.")
public interface AuthApi {

    @Operation(summary = "회원 가입 또는 로그인", description = "회원 가입 또는 로그인을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "회원 가입 또는 로그인 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LoginRes.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "회원 가입 또는 로그인 실패(잘못된 요청 방식)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @PostMapping("/login")
    ResponseEntity<SuccessResponse<LoginRes>> signUpOrLogin(
            @Parameter(description = "Schemas의 LoginReq 참고", required = true) @Valid @RequestBody LoginReq loginReq
    );

    @Operation(summary = "닉네임 중복 체크", description = "닉네임 중복 여부를 체크합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "닉네임 중복 체크 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DuplicateCheckRes.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "닉네임 중복 체크 실패(잘못된 요청 방식)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @GetMapping(value = "/nicknames")
    ResponseEntity<SuccessResponse<DuplicateCheckRes>> checkNicknameDuplicate(
            @Parameter(description = "검사할 닉네임을 입력해주세요.", required = true) @RequestParam(value = "nickname") String nickname
    );

    @Operation(summary = "회원 로그아웃", description = "회원 로그아웃을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "로그아웃 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "로그아웃 실패(잘못된 요청 방식)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @DeleteMapping("/logout")
    ResponseEntity<SuccessResponse<Message>> logout(
            @Parameter(description = "로그아웃하고 싶은 회원의 Access Token을 입력하시오.", required = true) @LoginMember Member member,
            @Parameter(description = "Schemas의 LogoutReq를 참고", required = true) @Valid @RequestBody LogoutReq logoutReq);

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "탈퇴 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "탈퇴 실패(잘못된 요청 방식)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @DeleteMapping("/exit")
    ResponseEntity<SuccessResponse<Message>> exit(
            @Parameter(description = "탈퇴하고 싶은 회원의 Access Token을 입력하시오.", required = true) @LoginMember Member member);
}
