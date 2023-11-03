package capstone.rtou.api.conversation.model;

import capstone.rtou.api.conversation.dto.ModelRequest;
import capstone.rtou.api.conversation.dto.ModelResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ModelService {
    private final RestTemplate restTemplate;

    public ModelService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getSentence(String sentence) {
        String apiUrl = "http://54.180.104.9:8000/conversation"; // API 엔드포인트 URL

        // API 호출
        ModelResponse modelResponse = restTemplate.postForObject(apiUrl, new ModelRequest(1, sentence), ModelResponse.class);
        return modelResponse.getText();
    }

}
