package samsamoo.ai_mockly.domain.llm.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import samsamoo.ai_mockly.domain.feedback.domain.Feedback;
import samsamoo.ai_mockly.domain.feedback.domain.repository.FeedbackRepository;
import samsamoo.ai_mockly.domain.llm.dto.response.LLMFeedbackRes;
import samsamoo.ai_mockly.domain.llm.dto.response.LLMQuestionRes;
import samsamoo.ai_mockly.domain.llm.dto.response.LLMScoreRes;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.domain.member.domain.repository.MemberRepository;
import samsamoo.ai_mockly.domain.score.domain.Score;
import samsamoo.ai_mockly.domain.score.domain.repository.ScoreRepository;
import samsamoo.ai_mockly.domain.scoredetails.domain.Category;
import samsamoo.ai_mockly.domain.scoredetails.domain.ScoreDetails;
import samsamoo.ai_mockly.global.common.Message;
import samsamoo.ai_mockly.global.common.SuccessResponse;
import samsamoo.ai_mockly.infrastructure.external.llm.LLMInterviewClient;
import samsamoo.ai_mockly.infrastructure.external.llm.dto.LLMResponseDTO;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LLMService {

    private final LLMInterviewClient llmClient;
    private final MemberRepository memberRepository;
    private final FeedbackRepository feedbackRepository;

    private static final String JSON_PARSER_PATTERN = "^\\{\\\"status\\\":\\\"success\\\",";
    private static final String JSON_SUFFIX_PATTERN = "\\\"}$";
    private static final String NEWLINE_PATTERN = "\\\\n";
    private static final String SPLIT_PATTERN = "\\d+\\. ";
    private final ScoreRepository scoreRepository;

    @Transactional
    public SuccessResponse<Message> processResumePdf(MultipartFile multipartFile) {
        try {
            byte[] fileBytes = multipartFile.getBytes();
            String filename = multipartFile.getOriginalFilename();
            LLMResponseDTO response = llmClient.uploadPdf(fileBytes, filename).block();

            Message message = Message.builder()
                    .message(response.getMessage())
                    .build();

            return SuccessResponse.of(message);
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
                .replaceFirst(JSON_PARSER_PATTERN+"\\\"questions\\\":\\\"", "")
                .replaceAll(NEWLINE_PATTERN, "")
                .replaceAll(JSON_SUFFIX_PATTERN, "");

        List<String> questionList = Arrays.stream(cleaned.split(SPLIT_PATTERN))
                .map(String::trim)
                .filter(question -> !question.isBlank())
                .collect(Collectors.toList());

        if(questionList.size() < 1) {
            throw new IllegalStateException("질문 분리에 실패했습니다. 질문 내용을 확인하세요.");
        }

        LLMQuestionRes llmQuestionRes = LLMQuestionRes.builder()
                .questionList(questionList)
                .build();

        return SuccessResponse.of(llmQuestionRes);
    }

    @Transactional
    public SuccessResponse<Message> processResumeQa(MultipartFile multipartFile) {
        try {
            byte[] fileBytes = multipartFile.getBytes();
            String filename = multipartFile.getOriginalFilename();
            LLMResponseDTO response = llmClient.uploadQa(fileBytes, filename).block();

            Message message = Message.builder()
                    .message(response.getMessage())
                    .build();

            return SuccessResponse.of(message);
        } catch (Exception e) {
            throw new RuntimeException("파일 처리 중 오류 발생 : " + e.getMessage());
        }
    }

    @Transactional
    public SuccessResponse<LLMFeedbackRes> getEvaluateFeedback(Long memberId) {
        String rawFeedbackText = llmClient.fetchFeedbacksText().block();

        if(rawFeedbackText == null || rawFeedbackText.isBlank()) {
            throw new IllegalStateException("피드백을 가져오는데 실패했습니다.");
        }

        String cleaned = rawFeedbackText
                .replaceFirst(JSON_PARSER_PATTERN+"\\\"feedback\\\":\\\"", "")
                .replaceAll(NEWLINE_PATTERN, "")
                .replaceAll(JSON_SUFFIX_PATTERN, "");

        if(memberId != null) {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalStateException("해당 유저가 없습니다."));

            Feedback feedback = Feedback.builder()
                    .member(member)
                    .content(cleaned)
                    .build();

            feedbackRepository.save(feedback);
        }

        List<String> feedbackList = Arrays.stream(cleaned.split(SPLIT_PATTERN))
                .map(String::trim)
                .filter(feedback -> !feedback.isBlank())
                .collect(Collectors.toList());

        LLMFeedbackRes llmFeedbackRes = LLMFeedbackRes.builder()
                .feedbackList(feedbackList)
                .build();

        return SuccessResponse.of(llmFeedbackRes);
    }

    @Transactional
    public SuccessResponse<LLMScoreRes> getScoreFeedback(Long memberId) {
        String rawScoreText = llmClient.fetchScoresText().block();

        if(rawScoreText == null || rawScoreText.isBlank()) {
            throw new IllegalArgumentException("점수를 가져오는데 실패했습니다.");
        }

        String cleaned = rawScoreText
                .replaceFirst(JSON_PARSER_PATTERN+"\\\"score\\\":\\\"", "")
                .replaceAll(NEWLINE_PATTERN, "")
                .replaceAll(JSON_SUFFIX_PATTERN, "");

        Map<String, Integer> scoreMap = new LinkedHashMap<>();
        AtomicInteger totalScore = new AtomicInteger(0);

        Arrays.stream(cleaned.split("\\/100"))
                .map(String::trim)
                .filter(line -> !line.isBlank() && line.contains(":"))
                .map(line -> line.split(":", 2))
                .forEach(pair -> {
                    String label = pair[0].trim();
                    Integer value = Integer.parseInt(pair[1].trim());
                    scoreMap.put(label, value);
                    totalScore.addAndGet(value);
                });

        if (memberId != null) {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalStateException("해당 유저가 없습니다."));

            double avgScore = totalScore.get()/10.0;

            Boolean highScore = member.getMaxScore() == null || member.getMaxScore() <= avgScore;

            if(highScore) {
                scoreRepository.findAllByMemberAndHighScore(member, true)
                        .forEach(prevHighScore -> {
                            prevHighScore.newHighScore(false);
                            scoreRepository.save(prevHighScore);
                        });
            }

            Score score = Score.builder()
                    .member(member)
                    .totalScore(avgScore)
                    .highScore(highScore)
                    .build();

            for (Map.Entry<String, Integer> entry : scoreMap.entrySet()) {
                try {
                    Category category = Category.fromValue(entry.getKey());

                    ScoreDetails scoreDetails = ScoreDetails.builder()
                            .category(category)
                            .scoreValue(entry.getValue())
                            .build();

                    score.addScoreDetails(scoreDetails);
                } catch (Exception e) {
                    throw new RuntimeException("점수 분류 실패: " + entry.getKey() + "는 정규화된 분류가 아닙니다." + e.getMessage());
                }
            }

            scoreRepository.save(score);

            if(highScore) {
                member.updateMaxScore(avgScore);
                memberRepository.save(member);
            }
        }

        LLMScoreRes llmScoreRes = LLMScoreRes.builder()
                .scoreMap(scoreMap)
                .build();

        return SuccessResponse.of(llmScoreRes);
    }
}
