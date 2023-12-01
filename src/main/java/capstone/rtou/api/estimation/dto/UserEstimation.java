package capstone.rtou.api.estimation.dto;

import lombok.Getter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
public class UserEstimation implements Serializable {
    String estimationId;
    String date;

    public UserEstimation(String estimationId, String date) {
        this.estimationId = estimationId;
        this.date = date;
    }
}
