package samsamoo.ai_mockly.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
                .version("v1.1.2")
                .title("AI Mockly API")
                .description("AI Mockly API 목록입니다.");

        return new OpenAPI()
                .info(info);
    }
}
