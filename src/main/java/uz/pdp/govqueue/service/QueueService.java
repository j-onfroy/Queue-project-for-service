package uz.pdp.govqueue.service;

import uz.pdp.govqueue.payload.AddQueueDTO;
import uz.pdp.govqueue.payload.ApiResult;
import uz.pdp.govqueue.payload.QueueForPrintDTO;
import uz.pdp.govqueue.payload.StatusDTO;

import java.util.List;

public interface QueueService {

    ApiResult<QueueForPrintDTO> create(AddQueueDTO addQueueDTO);

    ApiResult<List<StatusDTO>> forBoard(Integer operatorId);

}
