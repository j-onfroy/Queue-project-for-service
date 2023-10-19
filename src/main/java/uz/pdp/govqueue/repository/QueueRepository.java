package uz.pdp.govqueue.repository;

import org.springframework.data.repository.CrudRepository;
import uz.pdp.govqueue.enums.QueueStatusEnum;
import uz.pdp.govqueue.model.Queue;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;

public interface QueueRepository extends CrudRepository<Queue, Integer> {


    int countByCreatedAtBetweenAndStatusInAndNumberStartingWith(Timestamp start, Timestamp end, Collection<QueueStatusEnum> status, String number);

    Optional<Queue> findFirstByCreatedAtBetweenAndNumberStartingWithOrderByNumberDesc(Timestamp createdAt, Timestamp createdAt2, String number);

}
