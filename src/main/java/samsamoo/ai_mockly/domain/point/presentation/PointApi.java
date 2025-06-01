package samsamoo.ai_mockly.domain.point.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.domain.point.dto.request.PointAmountReq;
import samsamoo.ai_mockly.global.annotation.LoginMember;
import samsamoo.ai_mockly.global.common.Message;
import samsamoo.ai_mockly.global.common.SuccessResponse;
import samsamoo.ai_mockly.global.exception.ErrorResponse;

@Tag(name = "Point API", description = "포인트 관련 API입니다.")
public interface PointApi {

    @Operation(summary = "포인트 잔고 확인", description = "회원의 포인트 잔고를 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "회원 포인트 잔고 불러오기 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "회원 포인트 잔고 실패(잘못된 요청 방식)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @GetMapping("/me")
    ResponseEntity<SuccessResponse<Integer>> getCurrentAmount(
            @Parameter(description = "정보를 불러오고 싶은 회원의 Access Token을 입력하시오.", required = true) @LoginMember Member member);

    @Operation(summary = "포인트 증가", description = "포인트를 증가합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "포인트 증가 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "포인트 증가 실패(잘못된 요청 방식)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @PostMapping("/add")
    ResponseEntity<SuccessResponse<Message>> addPoint(
            @Parameter(description = "포인트를 더할 회원의 Access Token을 입력하시오.", required = true)@LoginMember Member member,
            @Parameter(description = "포인트 증감 Request", required = true) @Valid @RequestBody PointAmountReq pointAmountReq);

    @Operation(summary = "포인트 차감", description = "포인트를 차감합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "포인트 차감 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "포인트 차감 실패(잘못된 요청 방식)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @PostMapping("/deduct")
    ResponseEntity<SuccessResponse<Message>> deductPoint(
            @Parameter(description = "포인트를 뺄 회원의 Access Token을 입력하시오.", required = true)@LoginMember Member member,
            @Parameter(description = "포인트 증감 Request", required = true) @Valid @RequestBody PointAmountReq pointAmountReq);
}
