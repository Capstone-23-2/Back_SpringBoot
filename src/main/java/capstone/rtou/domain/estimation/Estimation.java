package capstone.rtou.domain.estimation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Estimation")
public class Estimation {

    @Id @Column(nullable = false)
    String userId;


}
