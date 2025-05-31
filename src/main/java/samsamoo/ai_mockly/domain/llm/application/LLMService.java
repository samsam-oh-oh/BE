package samsamoo.ai_mockly.domain.llm.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import samsamoo.ai_mockly.domain.llm.dto.response.LLMQuestionRes;
import samsamoo.ai_mockly.global.common.SuccessResponse;
import samsamoo.ai_mockly.infrastructure.external.llm.LLMInterviewClient;
import samsamoo.ai_mockly.infrastructure.external.llm.dto.LLMResponseDTO;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LLMService {

    private final LLMInterviewClient llmClient;

    private static final String JSON_PARSER_PATTERN = "\\{\\\"status\\\":\\\"success\\\",\\\"question\\\":\\\"(.*?)\\\"\\}";
    private static final String JSON_SUFFIX_PATTERN = "\\\"}$";
    private static final String NEWLINE_PATTERN = "\\\\n\\\\n";
    private static final String QUESTION_SPLIT_PATTERN = "\\d+\\. ";

    @Transactional
    public SuccessResponse<LLMResponseDTO> processResumePdf(MultipartFile multipartFile) {
        try {
            byte[] fileBytes = multipartFile.getBytes();
            String filename = multipartFile.getOriginalFilename();
            LLMResponseDTO response = llmClient.uploadPdf(fileBytes, filename).block();
            return SuccessResponse.of(response);
        } catch (Exception e) {
            throw new RuntimeException("파일 처리 중 오류 발생 : " + e.getMessage());
        }
    }

    public SuccessResponse<LLMQuestionRes> getGeneratedQuestion() {
        String rawQuestionText = llmClient.fetchQuestionsText().block();

        if(rawQuestionText == null || rawQuestionText.isBlank()) {
            throw new IllegalStateException("질문을 가져오는데 실패했습니다.");
        }

        String cleaned = rawQuestionText
                .replaceFirst(JSON_PARSER_PATTERN, "")
                .replaceAll(NEWLINE_PATTERN, "")
                .replaceAll(JSON_SUFFIX_PATTERN, "");

        List<String> questionList = Arrays.stream(cleaned.split(QUESTION_SPLIT_PATTERN))
                .map(String::trim)
                .filter(question -> !question.isBlank())
                .collect(Collectors.toList());

        if(questionList.size() < 1) {
            throw new IllegalStateException("질문 분리에 실패했습니다. 질문 내용을 확인하세요.");
        }

        LLMQuestionRes llmQuestionRes = LLMQuestionRes.builder()
                .status("success")
                .questionList(questionList)
                .build();

        return SuccessResponse.of(llmQuestionRes);
    }
}
