package uz.pdp.govqueue.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.govqueue.enums.QueueStatusEnum;
import uz.pdp.govqueue.exceptions.MyException;
import uz.pdp.govqueue.model.GovService;
import uz.pdp.govqueue.model.Queue;
import uz.pdp.govqueue.payload.AddQueueDTO;
import uz.pdp.govqueue.payload.ApiResult;
import uz.pdp.govqueue.payload.QueueForPrintDTO;
import uz.pdp.govqueue.payload.StatusDTO;
import uz.pdp.govqueue.repository.GovServiceRepository;
import uz.pdp.govqueue.repository.OperatorRepository;
import uz.pdp.govqueue.repository.QueueRepository;
import uz.pdp.govqueue.utils.CommonUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

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
        //TODO
        // ALL RUNNABLE
        // MY QUEUE IN OTHER STATUS

        return null;
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
