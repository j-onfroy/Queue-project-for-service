package uz.pdp.govqueue.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.govqueue.enums.QueueStatusEnum;
import uz.pdp.govqueue.exceptions.MyException;
import uz.pdp.govqueue.model.GovService;
import uz.pdp.govqueue.model.Queue;
import uz.pdp.govqueue.payload.*;
import uz.pdp.govqueue.repository.GovServiceRepository;
import uz.pdp.govqueue.repository.OperatorRepository;
import uz.pdp.govqueue.repository.QueueRepository;
import uz.pdp.govqueue.utils.CommonUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {

    private final QueueRepository queueRepository;
    private final GovServiceRepository govServiceRepository;
    private final OperatorRepository operatorRepository;

    @Transactional
    @Override
    public ApiResult<QueueForPrintDTO> create(AddQueueDTO addQueueDTO) {
        GovService govService = govServiceRepository.findById(addQueueDTO.getServiceId()).orElseThrow(() -> new MyException("Oka bunday service yo'qku", HttpStatus.NOT_FOUND));

        checkStatus(govService);

        String firstLetter = String.valueOf(govService.getFirstLetter());

        Timestamp start = CommonUtils.todayWithStartTime();
        Timestamp end = Timestamp.valueOf(LocalDateTime.now());

        Queue queue = Queue.builder()
                .beforeCount(queueRepository.countByCreatedAtBetweenAndStatusInAndNumberStartingWith(start, end, List.of(QueueStatusEnum.RUNNABLE, QueueStatusEnum.WAITING), firstLetter))
                .govServiceId(addQueueDTO.getServiceId())
                .number(getFirsLetter(firstLetter, start, end))
                .build();

        queueRepository.save(queue);

        QueueForPrintDTO queueForPrintDTO = mapQueueForPrintDTO(govService, firstLetter, start, queue.getBeforeCount(), queue);
        return new ApiResult<>(queueForPrintDTO);
    }

    @Override
    public ApiResult<List<StatusDTO>> forBoard(Integer operatorId) {
        List<Queue> queues = queueRepository.findAllByStatusAndOperatorId(operatorId);

        Map<QueueStatusEnum, List<QueueDTO>> map = new HashMap<>();

        Arrays.stream(QueueStatusEnum.values()).forEach(status -> map.put(status, new LinkedList<>()));

        queues.forEach(queue -> map.get(queue.getStatus()).add(mapQueueDTO(queue)));

        List<StatusDTO> statusDTOList = map.entrySet().stream().map(this::mapStatusDTO).toList();

        return new ApiResult<>(statusDTOList);
    }


    @Override
    public ApiResult<QueueDTO> callQueue(Integer operatorId) {

        Optional<Queue> optionalQueue = queueRepository.getTopCalledByOperatorId(operatorId);

        Queue queue;
        if (optionalQueue.isEmpty())
            queue = queueRepository.getTopRunnableByOperatorId(operatorId).orElseThrow(() -> new MyException("You don't have runnable queue", HttpStatus.NOT_FOUND));
        else
            queue = optionalQueue.get();

        if (queue.getCalledCount() == 3)
            throw new MyException("OKa 3 martadan ortiq chaqirdiz", HttpStatus.BAD_REQUEST);

        if (queue.getCalledAt() != null && (System.currentTimeMillis() - queue.getCalledAt().getTime() < 20_000))
            throw new MyException("Oka 20 sekunddan keyin chaqiring", HttpStatus.BAD_REQUEST);

        queue.setOperatorId(operatorId);
        queue.setStatus(QueueStatusEnum.CALLED);
        queue.setCalledAt(new Timestamp(System.currentTimeMillis()));
        queue.setCalledCount(queue.getCalledCount() + 1);

        queueRepository.save(queue);

        return new ApiResult<>(

                mapQueueDTO(queue));
    }

    private StatusDTO mapStatusDTO(Map.Entry<QueueStatusEnum, List<QueueDTO>> entry) {
        return StatusDTO.builder()
                .queues(entry.getValue())
                .queueCount(entry.getValue().size())
                .title(entry.getKey())
                .build();
    }

    private List<QueueDTO> mapQueueDTO(List<Queue> queues, QueueStatusEnum queueStatusEnum) {
        return queueRepository.findAllByStatus(QueueStatusEnum.RUNNABLE).stream().filter(queue -> queue.getStatus().equals(queueStatusEnum)).map(queue -> QueueDTO.builder()
                .number(queue.getNumber())
                .createdAt(queue.getCreatedAt())
                .beforeCount(queue.getBeforeCount())
                .status(queueStatusEnum)
                .finishedAt(queue.getFinishedAt())
                .waitingAt(queue.getWaitingAt())
                .startedAt(queue.getStartedAt())
                .build()).toList();
    }

    private QueueDTO mapQueueDTO(Queue queue) {
        return QueueDTO.builder()
                .id(queue.getId())
                .operatorId(queue.getOperatorId())
                .number(queue.getNumber())
                .createdAt(queue.getCreatedAt())
                .beforeCount(queue.getBeforeCount())
                .status(queue.getStatus())
                .finishedAt(queue.getFinishedAt())
                .waitingAt(queue.getWaitingAt())
                .startedAt(queue.getStartedAt())
                .build();
    }

    /**
     * IF status false throw
     *
     * @param govService GovService
     * @throws RuntimeException ex
     */

    private void checkStatus(GovService govService) {
        if (!govService.isStatus())
            throw new MyException("Oka o'chiqku bu service", HttpStatus.BAD_REQUEST);
    }

    private QueueForPrintDTO mapQueueForPrintDTO(GovService govService, String firstLetter, Timestamp start, int beforeCount, Queue queue) {
        Long avgYesterdayByFirstLetter = queueRepository.getAvgYesterdayByFirstLetter(CommonUtils.yesterdayWithStartTime(), start, firstLetter);

        long operatorCount = operatorRepository.countAllByStatusTrueAndServiceIncludes(firstLetter);

        long afterSomeMinutes = avgYesterdayByFirstLetter / operatorCount * beforeCount;
        // avg / operator count * 5
        LocalTime plus = LocalTime.now().plus(afterSomeMinutes, ChronoUnit.MINUTES);

        return QueueForPrintDTO.builder()
                .createdAt(queue.getCreatedAt())
                .beforeCount(queue.getBeforeCount())
                .roundTime(plus)
                .serviceName(govService.getName())
                .number(queue.getNumber())
                .build();
    }

    private String getFirsLetter(String firstLetter, Timestamp start, Timestamp end) {
        Optional<Queue> lastQueueCurrentLetterAndToday = queueRepository.findFirstByCreatedAtBetweenAndNumberStartingWithOrderByNumberDesc(start, end, firstLetter);

        String number;
        if (lastQueueCurrentLetterAndToday.isEmpty())
            number = firstLetter + "1";
        else {
            number = lastQueueCurrentLetterAndToday.get().getNumber();
            number = firstLetter + (Integer.parseInt(number.substring(1)) + 1);
        }
        return number;
    }
}
