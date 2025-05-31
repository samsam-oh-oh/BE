package samsamoo.ai_mockly.domain.llm.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import samsamoo.ai_mockly.domain.llm.application.LLMService;
import samsamoo.ai_mockly.domain.llm.dto.response.LLMQuestionRes;
import samsamoo.ai_mockly.global.common.SuccessResponse;
import samsamoo.ai_mockly.infrastructure.external.llm.dto.LLMResponseDTO;

@RequiredArgsConstructor
@RestController
@RequestMapping("/llm")
public class LLMController {

    private final LLMService llmService;

    @PostMapping("/upload/pdf")
    public ResponseEntity<SuccessResponse<LLMResponseDTO>> uploadPdf(@RequestPart("file") MultipartFile multipartFile) {
        return ResponseEntity.ok(llmService.processResumePdf(multipartFile));
    }

    @GetMapping("/questions")
    public ResponseEntity<SuccessResponse<LLMQuestionRes>> getGeneratedQuestions() {
        return ResponseEntity.ok(llmService.getGeneratedQuestion());
    }
}
