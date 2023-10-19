package uz.pdp.govqueue.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.govqueue.payload.AddQueueDTO;
import uz.pdp.govqueue.payload.QueueForPrintDTO;
import uz.pdp.govqueue.payload.QueueMoveDTO;
import uz.pdp.govqueue.service.QueueService;

@RestController
@RequiredArgsConstructor
public class QueueControllerImpl implements QueueController {

    private final QueueService queueService;

    @Override
    public HttpEntity<?> add(AddQueueDTO addQueueDTO) {
        QueueForPrintDTO queueForPrintDTO = queueService.create(addQueueDTO);
        return ResponseEntity.ok(queueForPrintDTO);
    }

    @Override
    public HttpEntity<?> callQueue(Integer operatorId) {

        return null;
    }

    @Override
    public HttpEntity<?> moveToRunning(QueueMoveDTO queueMoveDTO) {
        return null;
    }
}
