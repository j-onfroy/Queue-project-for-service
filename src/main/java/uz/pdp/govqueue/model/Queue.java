package uz.pdp.govqueue.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import uz.pdp.govqueue.enums.QueueStatusEnum;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Queue {

    @Id
    private Integer id;

    private Integer govServiceId;

    @Builder.Default
    private Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());

    private int beforeCount;

    private String number;

    @Builder.Default
    private QueueStatusEnum status = QueueStatusEnum.RUNNABLE;

    private Timestamp startedAt;

    private Timestamp calledAt;

    private Timestamp waitingAt;

    private Timestamp finishedAt;

    private Integer operatorId;

    @Builder.Default
    private Integer calledCount = 0;

    //    private Operator operator;


    public Integer getCalledCount() {

        return calledCount == null ? 0 : calledCount;
    }
}
