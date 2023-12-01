package capstone.rtou.api.attention;

import capstone.rtou.api.attention.dto.AttentionRequestDto;
import capstone.rtou.api.attention.dto.AttentionResponse;
import capstone.rtou.domain.attention.Attentions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AttentionService {

    private final AttentionRepository attentionRepository;

    public AttentionService(AttentionRepository attentionRepository) {
        this.attentionRepository = attentionRepository;
    }

    @Transactional
    public AttentionResponse attention(AttentionRequestDto attentionRequest) {
        Date date = Date.valueOf(LocalDate.now());
        if (attentionRepository.existsAttentionByUserIdAndDate(attentionRequest.getUserId(), date)) {
            return new AttentionResponse(false, "오늘은 이미 출석했습니다");
        } else {
            Attentions attentions = new Attentions(attentionRequest.getUserId());
            attentionRepository.save(attentions);
            return new AttentionResponse(true, "출석 완료");
        }
    }

    @Transactional
    public AttentionResponse findAttentionById(String userId) {
        List<Attentions> list = attentionRepository.findAllByUserId(userId);

        if (list.isEmpty()) {
            return new AttentionResponse(false, "출석 기록이 존재하지 않습니다");
        } else {
            List<String> dates = new ArrayList<>();

            for (Attentions i : list) {
                dates.add(i.getDate().toString());
            }
            return new AttentionResponse(true, "전체 출석 기록", (ArrayList<String>) dates);
        }
    }
}
