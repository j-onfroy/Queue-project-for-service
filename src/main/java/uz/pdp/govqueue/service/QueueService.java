package uz.pdp.govqueue.service;

import uz.pdp.govqueue.payload.*;

import java.util.List;

public interface QueueService {

    ApiResult<QueueForPrintDTO> create(AddQueueDTO addQueueDTO);

    ApiResult<List<StatusDTO>> forBoard(Integer operatorId);

    ApiResult<QueueDTO> callQueue(Integer operatorId);
}
