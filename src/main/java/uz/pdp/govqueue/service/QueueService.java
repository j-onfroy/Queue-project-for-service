package uz.pdp.govqueue.service;

import uz.pdp.govqueue.payload.AddQueueDTO;
import uz.pdp.govqueue.payload.QueueForPrintDTO;

public interface QueueService {

    QueueForPrintDTO create(AddQueueDTO addQueueDTO);
}
