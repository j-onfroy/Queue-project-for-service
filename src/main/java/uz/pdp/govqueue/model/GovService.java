package uz.pdp.govqueue.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import uz.pdp.govqueue.enums.LevelEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GovService {

    @Id
    private Integer id;

    private String name;

    private LevelEnum level;

    private Character firstLetter;

    private boolean status;
}
