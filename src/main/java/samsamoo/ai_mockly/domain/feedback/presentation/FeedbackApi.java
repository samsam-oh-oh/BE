package samsamoo.ai_mockly.domain.feedback.presentation;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import samsamoo.ai_mockly.domain.feedback.dto.request.FeedbackSaveReq;
import samsamoo.ai_mockly.domain.feedback.dto.response.FeedbackContentsRes;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.global.annotation.LoginMember;
import samsamoo.ai_mockly.global.common.Message;
import samsamoo.ai_mockly.global.common.SuccessResponse;
import samsamoo.ai_mockly.global.exception.ErrorResponse;

import java.util.List;

@Tag(name = "FeedbackAPI", description = "면접 피드백 및 결과 리포트 관련 API입니다.")
public interface FeedbackApi {

    @Operation(summary = "피드백 내용 저장", description = "회원의 피드백 내용을 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "피드백 내용 저장 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "피드백 내용 저장 실패(잘못된 요청 방식)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @PostMapping("/save")
    ResponseEntity<SuccessResponse<Message>> saveFeedback(
            @Parameter(description = "정보를 불러오고 싶은 회원의 Access Token을 입력하시오.", required = true) @LoginMember Member member,
            @Parameter(description = "Schemas의 ReedbackSaveReq를 참고", required = true) @Valid @RequestBody FeedbackSaveReq feedbackSaveReq);

    @Operation(summary = "피드백 내용 리스트 불러오기", description = "회원의 피드백 내용을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "회원의 피드백 내용 불러오기 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FeedbackContentsRes.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "회원의 피드백 내용 불러오기 실패(잘못된 요청 방식)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @GetMapping("/contents/all")
    ResponseEntity<SuccessResponse<List<FeedbackContentsRes>>> getFeedbackContents(
            @Parameter(description = "피드백 내용을 불러오고 싶은 회원의 Access Token을 입력하시오.", required = true) @LoginMember Member member);

    @Operation(summary = "피드백 내용 불러오기", description = "선택한 피드백 내용을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "피드백 내용 불러오기 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FeedbackContentsRes.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "피드백 내용 불러오기 실패(잘못된 요청 방식)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @GetMapping("/contents/{feedbackId}")
    ResponseEntity<SuccessResponse<FeedbackContentsRes>> getFeedbackContent(
            @Parameter(description = "불러올 피드백의 id를 입력하시오.", required = true) @PathVariable Long feedbackId);
}
