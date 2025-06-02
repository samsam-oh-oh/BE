package samsamoo.ai_mockly.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReissueRes {

    @Schema(type = "String",
            example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsInJvbGUiOiJjdnZ6M0BuYXZlci5jb20iLCJpZCI6ImN2dnozQG5hdmVyLmNvbSIsImV4cCI6MTcyMjE4NTU5OSwiZW1haWwiOiJjdnZ6M0BuYXZlci5jb20ifQ.6b3gEhqolM3PcdeDpaT1ExTuNV0_PSQQhGDFEk1IvDnPePbjtV2ZX3ds48_nWx77ci4nSEbS1XsajcV9yW_vBQ",
            description = "Access Token을 출력합니다.")
    private String accessToken;
}
