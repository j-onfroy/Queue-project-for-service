package uz.pdp.govqueue.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import uz.pdp.govqueue.enums.QueueStatusEnum;
import uz.pdp.govqueue.model.GovService;

import java.sql.Time;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueueForPrintDTO {

    private String serviceName;

    private Timestamp createdAt;

    private int beforeCount;

    private String number;

    private Time roundTime;
}
