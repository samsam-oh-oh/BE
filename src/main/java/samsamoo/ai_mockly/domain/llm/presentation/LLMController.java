package samsamoo.ai_mockly.domain.llm.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
}
