package capstone.rtou.api.attention;

import capstone.rtou.api.attention.dto.AttentionRequestDto;
import capstone.rtou.api.attention.dto.AttentionResponse;
import capstone.rtou.api.auth.AuthRepository;
import capstone.rtou.domain.attention.Attention;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AttentionService {

    private final AuthRepository authRepository;
    private final AttentionRepository attentionRepository;

    public AttentionService(AuthRepository authRepository, AttentionRepository attentionRepository) {
        this.authRepository = authRepository;
        this.attentionRepository = attentionRepository;
    }

    @Transactional
    public AttentionResponse attention(AttentionRequestDto attentionRequest) {
        if (attentionRepository.existsAttentionByUserIdAndDate(attentionRequest.getUserId(), attentionRequest.getDate())) {
            return new AttentionResponse(false, "오늘은 이미 출석했습니다");
        } else {
            if (authRepository.existsById(attentionRequest.getUserId())) {
                return new AttentionResponse(false, "잘못된 사용자의 접근입니다.");
            }
            Attention attention = new Attention(attentionRequest.getUserId(), attentionRequest.getDate());
            attentionRepository.save(attention);
            return new AttentionResponse(true, "출석 완료");
        }
    }

    @Transactional
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
