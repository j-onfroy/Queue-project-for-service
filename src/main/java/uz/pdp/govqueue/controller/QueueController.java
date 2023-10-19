package uz.pdp.govqueue.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uz.pdp.govqueue.payload.AddQueueDTO;
import uz.pdp.govqueue.payload.QueueMoveDTO;

@RequestMapping(QueueController.BASE_PATH)
public interface QueueController {

    String BASE_PATH = "/queue";
    String ADD_PATH = "/add";
    String OPERATOR_CALL_PATH = "/call";
    String MOVE_PATH = "/move";

    @PostMapping(ADD_PATH)
    HttpEntity<?> add(@RequestBody @Valid AddQueueDTO addQueueDTO);

    @PatchMapping(OPERATOR_CALL_PATH)
    HttpEntity<?> callQueue(@RequestParam Integer operatorId);

    @PatchMapping(MOVE_PATH)
    HttpEntity<?> moveToRunning(@Valid @RequestBody QueueMoveDTO queueMoveDTO);

}
