package uz.pdp.govqueue.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.govqueue.enums.QueueStatusEnum;
import uz.pdp.govqueue.model.GovService;
import uz.pdp.govqueue.model.Queue;
import uz.pdp.govqueue.payload.AddQueueDTO;
import uz.pdp.govqueue.payload.QueueForPrintDTO;
import uz.pdp.govqueue.repository.GovServiceRepository;
import uz.pdp.govqueue.repository.QueueRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {

    private final QueueRepository queueRepository;
    private final GovServiceRepository govServiceRepository;

    @Override
    public QueueForPrintDTO create(AddQueueDTO addQueueDTO) {

        GovService govService =
                govServiceRepository.findById(addQueueDTO.getServiceId())
                        .orElseThrow(() -> new RuntimeException("Oka bunday service yo'qku"));

        if (!govService.isStatus())
            throw new RuntimeException("Oka o'chiqku");

        Character firstLetter = govService.getFirstLetter();

        Timestamp start = Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)));
        Timestamp end = Timestamp.valueOf(LocalDateTime.now());

        int beforeCount = queueRepository
                .countByCreatedAtBetweenAndStatusInAndNumberStartingWith(start, end, List.of(QueueStatusEnum.RUNNABLE, QueueStatusEnum.WAITING), String.valueOf(firstLetter));

        Optional<Queue> lastQueueCurrentLetterAndToday = queueRepository.findFirstByCreatedAtBetweenAndNumberStartingWithOrderByNumberDesc(start, end, String.valueOf(firstLetter));

        String number;
        if (lastQueueCurrentLetterAndToday.isEmpty()) {
            number = firstLetter + "1";
        } else {
            number = lastQueueCurrentLetterAndToday.get().getNumber();
            number = firstLetter + "" + (Integer.parseInt(number.substring(1)) + 1);
        }

        Queue queue = Queue.builder()
                .beforeCount(beforeCount)
                .govServiceId(addQueueDTO.getServiceId())
                .number(number)
                .build();

        queueRepository.save(queue);

        QueueForPrintDTO.builder()
                .createdAt(queue.getCreatedAt())
                .beforeCount(queue.getBeforeCount())
                .serviceName(govService.getName());
        //todo finish guys

        return null;
    }
}
