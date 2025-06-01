package samsamoo.ai_mockly.domain.llm.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import samsamoo.ai_mockly.domain.llm.dto.response.LLMFeedbackRes;
import samsamoo.ai_mockly.domain.llm.dto.response.LLMQuestionRes;
import samsamoo.ai_mockly.domain.llm.dto.response.LLMScoreRes;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.global.annotation.LoginMember;
import samsamoo.ai_mockly.global.common.Message;
import samsamoo.ai_mockly.global.common.SuccessResponse;
import samsamoo.ai_mockly.global.exception.ErrorResponse;

import java.util.Optional;

@Tag(name = "LLM API", description = "면접 LLM 관련 API입니다.")
public interface LLMApi {

    @Operation(summary = "이력서 pdf 보내기", description = "LLM에 이력서 등 pdf 파일을 보냅니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "pdf 전송 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "pdf 전송 실패(잘못된 요청 방식)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @PostMapping(value = "/upload/pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<SuccessResponse<Message>> uploadPdf(
            @Parameter(description = "LLM에 보낼 pdf 파일을 입력하세요. key 값은 file 입니다.", required = true) @RequestPart("file") MultipartFile multipartFile);

    @Operation(summary = "면접 질문 불러오기", description = "LLM에 받은 면접 질문을 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "면접 질문 불러오기 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LLMQuestionRes.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "면접 질문 불러오기 실패(잘못된 요청 방식)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @GetMapping("/questions")
    ResponseEntity<SuccessResponse<LLMQuestionRes>> getGeneratedQuestions();

    @Operation(summary = "답변 text 보내기", description = "LLM에 질문에 대한 답변 text 파일을 보냅니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "text 전송 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "text 전송 실패(잘못된 요청 방식)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @PostMapping(value = "/upload/qa", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<SuccessResponse<Message>> uploadQa(
            @Parameter(description = "LLM에 보낼 txt 파일을 입력하세요. key 값은 STT_file입니다.", required = true) @RequestPart("STT_file") MultipartFile multipartFile);

    @Operation(summary = "면접 피드백 불러오기", description = "LLM에 받은 면접 피드백을 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "면접 피드백 불러오기 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LLMFeedbackRes.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "면접 피드백 불러오기 실패(잘못된 요청 방식)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @GetMapping("/feedbacks")
    ResponseEntity<SuccessResponse<LLMFeedbackRes>> getEvaluateFeedback(
            @Parameter(description = "피드백에 저장할 멤버의 AccessToken을 입력하세요. null은 게스트입니다.") @LoginMember Optional<Member> memberOpt);

    @Operation(summary = "면접 점수 불러오기", description = "LLM에 받은 면접 점수를 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "면접 점수 불러오기 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LLMScoreRes.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "면접 점수 불러오기 실패(잘못된 요청 방식)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @GetMapping("/scores")
    ResponseEntity<SuccessResponse<LLMScoreRes>> getScoreFeedback();
}
