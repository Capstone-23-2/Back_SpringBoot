package capstone.rtou.attention;

import capstone.rtou.attention.dto.AttentionRequestDto;
import capstone.rtou.domain.attention.Attention;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AttentionService {

    private final AttentionRepository attentionRepository;

    public AttentionService(AttentionRepository attentionRepository) {
        this.attentionRepository = attentionRepository;
    }

    String attention(AttentionRequestDto attentionRequest) {

        Attention attention = new Attention(attentionRequest.getUserId(), attentionRequest.getDate());

        attentionRepository.save(attention);

        return "";
    }
}
