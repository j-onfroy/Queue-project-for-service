package uz.pdp.govqueue.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.govqueue.payload.*;

import java.util.List;

@RequestMapping(QueueController.BASE_PATH)
public interface QueueController {

    String BASE_PATH = "/queue";
    String ADD_PATH = "/add";
    String OPERATOR_CALL_PATH = "/call/{id}";
    String MOVE_PATH = "/move";
    String FOR_BOARD_PATH = "/board/{operatorId}";


    @GetMapping(FOR_BOARD_PATH)
    HttpEntity<ApiResult<List<StatusDTO>>> forBoard(@PathVariable Integer operatorId);


    /**
     * {@link QueueForPrintDTO}
     *
     * @param addQueueDTO the {@link  AddQueueDTO}'s element type
     * @return a {@link HttpEntity}<{@link QueueForPrintDTO}> containing the elements of the given {@code Collection}
     * @throws RuntimeException if coll is bala battar
     * @implNote agar bal battar ....
     */
    @PostMapping(ADD_PATH)
    HttpEntity<ApiResult<QueueForPrintDTO>> add(@RequestBody @Valid AddQueueDTO addQueueDTO);

    @PatchMapping(OPERATOR_CALL_PATH)
    HttpEntity<?> callQueue(@PathVariable(required = false) Integer id,
                            @RequestParam(required = false) Integer operatorId);


    @PatchMapping(MOVE_PATH)
    HttpEntity<?> moveToRunning(@Valid @RequestBody QueueMoveDTO queueMoveDTO);
}
