package capstone.rtou.api.attention;

import capstone.rtou.api.attention.dto.AttentionRequestDto;
import capstone.rtou.api.attention.dto.AttentionResponse;
import capstone.rtou.domain.attention.Attention;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AttentionService {

    private final AttentionRepository attentionRepository;

    public AttentionService(AttentionRepository attentionRepository) {
        this.attentionRepository = attentionRepository;
    }

    AttentionResponse attention(AttentionRequestDto attentionRequest) {
        if (attentionRepository.existsAttentionByUserIdAndAttendDate(attentionRequest.getUserId(), attentionRequest.getDate())) {
            return new AttentionResponse(false,"오늘은 이미 출석했습니다");
        } else {
            Attention attention = new Attention(attentionRequest.getUserId(), attentionRequest.getDate());
            attentionRepository.save(attention);
            return new AttentionResponse(true, "출석 완료");
        }
    }

    public AttentionResponse findAttentionById(String userId) {
        List<Attention> list = attentionRepository.findAllByUserId(userId);

        if (list.isEmpty()) {
            return new AttentionResponse(false, "출석 기록이 존재하지 않습니다");
        } else {
            List<String> dates = new ArrayList<>();

            for (Attention i : list) {
                dates.add(i.getDate());
            }
            return new AttentionResponse(true, "전체 출석 기록", (ArrayList<String>) dates);
        }
    }
}
