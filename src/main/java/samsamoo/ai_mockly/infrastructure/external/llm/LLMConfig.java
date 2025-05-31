package samsamoo.ai_mockly.infrastructure.external.llm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class LLMConfig {

    @Value("${llm.api_key}")
    private String apiKey;

    @Value("${llm.base_url}")
    private String baseUrl;

    @Bean
    public WebClient llmWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }
}
