package uz.pdp.govqueue.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddQueueDTO {

    @NotNull(message = "Bratan service id null bo'lmasin")
    private Integer serviceId;
}
