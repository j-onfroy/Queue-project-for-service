package uz.pdp.govqueue.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import uz.pdp.govqueue.enums.QueueStatusEnum;

@Data
public class QueueMoveDTO {

    @NotNull
    private Integer id;

    @NotNull
    private QueueStatusEnum destinationStatus;
}
