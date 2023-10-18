package uz.pdp.govqueue.payload;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.govqueue.enums.LevelEnum;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDTO {

    private Integer id;

    @NotBlank
    private String name;

    @NotNull
    private LevelEnum level;

    @NotNull
    private Character firstLetter;

    @NotNull
    private Boolean status;
}
