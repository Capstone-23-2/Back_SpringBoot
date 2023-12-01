package capstone.rtou.api.estimation.dto;

import lombok.Getter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
public class UserEstimation implements Serializable {
    String estimationId;

    String characterName;

    String date;

    public UserEstimation(String estimationId, String characterName, String date) {
        this.estimationId = estimationId;
        this.characterName = characterName;
        this.date = date;
    }
}
