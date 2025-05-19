package samsamoo.ai_mockly.domain.score.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RankingListRes {

    private Long id;

    private String nickname;

    private Integer score;
}
