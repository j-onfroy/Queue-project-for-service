package uz.pdp.govqueue.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.govqueue.enums.QueueStatusEnum;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueueDTO {

    private Integer id;

    private Integer govServiceId;

    private Timestamp createdAt;

    private int beforeCount;

    private String number;

    private QueueStatusEnum status;

    private Timestamp startedAt;

    private Timestamp calledAt;

    private Timestamp waitingAt;

    private Timestamp finishedAt;
}
