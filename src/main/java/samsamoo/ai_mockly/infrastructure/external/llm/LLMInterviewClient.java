package samsamoo.ai_mockly.infrastructure.external.llm;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import samsamoo.ai_mockly.infrastructure.external.llm.dto.LLMResponseDTO;

import java.io.File;

/**
 * FastAPI 서버와 통신하여 면접 관련 데이터(PDF, QA)를 업로드하거나 질문/피드백/점수 요청을 수행하는 클라이언트 클래스
 */
@Component
@RequiredArgsConstructor
public class LLMInterviewClient {

    private final WebClient webClient;

    /**
     * 포트폴리오 PDF 파일을 FastAPI 서버에 업로드하여 질문 생성을 요청합니다.
     */
    public Mono<LLMResponseDTO> uploadPdf(byte[] fileBytes, String fileName) {
        return uploadFile(fileBytes, fileName, "/upload/pdf");
    }

    /**
     * 사용자의 QA 텍스트 파일을 FastAPI 서버에 업로드하여 피드백과 점수를 요청합니다.
     */
    public Mono<LLMResponseDTO> uploadQa(byte[] fileBytes, String fileName) {
        return uploadFile(fileBytes, fileName, "/upload/qa");
    }

    private Mono<LLMResponseDTO> uploadFile(byte[] fileBytes, String fileName, String endPoint) {
        Resource resource = new ByteArrayResource(fileBytes) {
            @Override
            public String getFilename() {
                return fileName;
            }
        };

        return webClient.post()
                .uri(endPoint)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("file", resource))
                .retrieve()
                .bodyToMono(LLMResponseDTO.class)
                .onErrorMap(WebClientResponseException.class,
                        e -> new RuntimeException("LLM 서버 호출 실패: " + e.getMessage()));
    }

    /**
     * 저장된 interview_questions.txt 파일 내용을 FastAPI 서버로부터 가져옵니다.
     */
    public Mono<String> fetchQuestionsText() {
        return webClient.get()
                .uri("/get-questions-text")
                .retrieve()
                .bodyToMono(String.class)
                .onErrorMap(WebClientResponseException.class,
                        e -> new RuntimeException("LLM 서버 호출 실패: " + e.getMessage()));
    }

    /**
     * 저장된 interview_feedback.txt 파일 내용을 FastAPI 서버로부터 가져옵니다.
     */
    public Mono<String> fetchFeedbacksText() {
        return webClient.get()
                .uri("/get-feedback-text")
                .retrieve()
                .bodyToMono(String.class)
                .onErrorMap(WebClientResponseException.class,
                        e -> new RuntimeException("LLM 서버 호출 실패: " + e.getMessage()));
    }

    /**
     * 저장된 interview_scores.txt 파일 내용을 FastAPI 서버로부터 가져옵니다.
     */
    public Mono<String> fetchScoresText() {
        return webClient.get()
                .uri("/get-score-text")
                .retrieve()
                .bodyToMono(String.class)
                .onErrorMap(WebClientResponseException.class,
                        e -> new RuntimeException("LLM 서버 호출 실패: " + e.getMessage()));
    }
}
