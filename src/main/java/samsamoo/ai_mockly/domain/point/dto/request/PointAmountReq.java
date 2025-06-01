package samsamoo.ai_mockly.domain.point.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PointAmountReq {

    @NotNull(message = "**포인트 수량**은 필수입니다.")
    @Positive(message = "**포인트 수량**은 0보다 커야합니다.")
    private Integer pointAmount;

    private String reason;
}
