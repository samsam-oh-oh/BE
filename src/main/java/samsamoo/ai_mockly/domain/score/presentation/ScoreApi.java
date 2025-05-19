package samsamoo.ai_mockly.domain.score.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import samsamoo.ai_mockly.domain.score.dto.response.RankingListRes;
import samsamoo.ai_mockly.global.common.SuccessResponse;
import samsamoo.ai_mockly.global.exception.ErrorResponse;

import java.util.List;

@Tag(name = "Score API", description = "점수 관련 API")
public interface ScoreApi {

    @Operation(summary = "랭킹 리스트 조회", description = "랭킹 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "랭킹 리스트 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RankingListRes.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "랭킹 리스트 조회 실패(잘못된 요청 방식)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @GetMapping("/rank")
    ResponseEntity<SuccessResponse<List<RankingListRes>>> getRankingList();
}
