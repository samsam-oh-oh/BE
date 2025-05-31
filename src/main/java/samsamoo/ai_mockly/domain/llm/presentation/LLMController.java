package samsamoo.ai_mockly.domain.llm.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import samsamoo.ai_mockly.domain.llm.application.LLMService;
import samsamoo.ai_mockly.domain.llm.dto.response.LLMQuestionRes;
import samsamoo.ai_mockly.global.common.Message;
import samsamoo.ai_mockly.global.common.SuccessResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/llm")
public class LLMController {

    private final LLMService llmService;

    @PostMapping("/upload/pdf")
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

    @GetMapping("/questions")
    public ResponseEntity<SuccessResponse<LLMQuestionRes>> getGeneratedQuestions() {
        return ResponseEntity.ok(llmService.getGeneratedQuestion());
    }
}
