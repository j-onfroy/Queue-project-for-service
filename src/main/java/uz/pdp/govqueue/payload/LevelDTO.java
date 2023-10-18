package uz.pdp.govqueue.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.govqueue.enums.LevelEnum;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LevelDTO {

    private LevelEnum level;

    private List<ServiceDTO> services;

    public LevelDTO(LevelEnum level) {
        this.level = level;
        this.services = new LinkedList<>();
    }
}
