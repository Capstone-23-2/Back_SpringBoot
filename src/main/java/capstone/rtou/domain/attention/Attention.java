package capstone.rtou.domain.attention;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "Attention")
@AllArgsConstructor
@NoArgsConstructor
public class Attention {

    @Id @Column
    String userId;

    @Column
    LocalDate date;
}
