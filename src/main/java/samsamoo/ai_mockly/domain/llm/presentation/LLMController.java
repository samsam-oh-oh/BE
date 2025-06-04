package samsamoo.ai_mockly.domain.llm.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import samsamoo.ai_mockly.domain.llm.application.LLMService;
import samsamoo.ai_mockly.domain.llm.dto.response.LLMQuestionRes;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.global.annotation.LoginMember;
import samsamoo.ai_mockly.global.common.Message;
import samsamoo.ai_mockly.global.common.SuccessResponse;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/llm")
public class LLMController implements LLMApi {

    private final LLMService llmService;

    @Override
    @PostMapping(value = "/upload/pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<Message>> uploadPdf(@RequestPart("file") MultipartFile multipartFile) {
        if(multipartFile==null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("파일이 업로드 되지 않음");
        }
        String contentType = multipartFile.getContentType();
        if(contentType == null || !contentType.startsWith("application/pdf")) {
            throw new IllegalArgumentException("PDF 파일만 업로드 가능합니다.");
        }
        return ResponseEntity.ok(llmService.processResumePdf(multipartFile));
    }

    @Override
    @GetMapping("/questions")
    public ResponseEntity<SuccessResponse<LLMQuestionRes>> getGeneratedQuestions() {
        return ResponseEntity.ok(llmService.getGeneratedQuestion());
    }

    @Override
    @PostMapping(value = "/upload/qa", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<Message>> uploadQa(@LoginMember Optional<Member> memberOpt, @RequestPart("STT_file") MultipartFile multipartFile) {
        Long memberId = memberOpt.map(Member::getId).orElse(null);
        if(multipartFile==null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("파일이 업로드 되지 않음");
        }
        String contentType = multipartFile.getContentType();
        if(contentType == null || !contentType.startsWith("text/plain")) {
            throw new IllegalArgumentException("txt 파일만 업로드 가능합니다.");
        }
        return ResponseEntity.ok(llmService.processResumeQa(multipartFile, memberId));
    }

//    @Override
//    @GetMapping("/feedbacks")
//    public ResponseEntity<SuccessResponse<LLMFeedbackRes>> getEvaluateFeedback(@LoginMember Optional<Member> memberOpt) {
//        Long memberId = memberOpt.map(Member::getId).orElse(null);
//        return ResponseEntity.ok(llmService.getEvaluateFeedback(memberId));
//    }
//
//    @Override
//    @GetMapping("/scores")
//    public ResponseEntity<SuccessResponse<LLMScoreRes>> getScoreFeedback(@LoginMember Optional<Member> memberOpt) {
//        Long memberId = memberOpt.map(Member::getId).orElse(null);
//        return ResponseEntity.ok(llmService.getScoreFeedback(memberId));
//    }
}
