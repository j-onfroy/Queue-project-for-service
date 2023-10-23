package uz.pdp.govqueue.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import uz.pdp.govqueue.enums.QueueStatusEnum;
import uz.pdp.govqueue.model.Queue;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface QueueRepository extends CrudRepository<Queue, Integer> {


    int countByCreatedAtBetweenAndStatusInAndNumberStartingWith(Timestamp start, Timestamp end, Collection<QueueStatusEnum> status, String number);

    Optional<Queue> findFirstByCreatedAtBetweenAndNumberStartingWithOrderByNumberDesc(Timestamp createdAt, Timestamp createdAt2, String number);

    @Query(value = """
            SELECT COALESCE(AVG(extract(EPOCH FROM finished_at - started_at)) / 60, 10) AS avg
                                                                     FROM queue
                                                                     WHERE created_at BETWEEN :start AND :end
                                                                       AND number LIKE CONCAT(:firstLetter, '%')
            """)
    Long getAvgYesterdayByFirstLetter(@Param("start") Timestamp start, @Param("end") Timestamp end, @Param("firstLetter") String firstLetter);

    List<Queue> findAllByStatus(QueueStatusEnum queueStatusEnum);

    @Query(value = """
                SELECT *
                FROM queue q
                WHERE created_at >= current_date
                  AND (operator_id = :operatorId OR (
                            status = 'RUNNABLE' AND
                            q.gov_service_id IN
                            (SELECT gov_service_id
                             FROM operator_service
                             WHERE operator_id = :operatorId)))
            """)
    List<Queue> findAllByStatusAndOperatorId(@Param("operatorId") Integer operatorId);

    @Query(value = """
                            WITH temp AS (SELECT *
                                          FROM queue
                                          WHERE status = 'RUNNABLE'
                                            AND created_at >= CURRENT_DATE
                                            AND gov_service_id IN (SELECT gov_service_id
                                                                   FROM operator_service
                                                                   WHERE operator_id = :operatorId))
                            SELECT *
                            FROM temp
                            WHERE created_at = (SELECT MIN(created_at)
                                                FROM temp) FOR UPDATE LIMIT 1
            """)
    Optional<Queue> getTopRunnableByOperatorId(Integer operatorId);

    @Query(value = """
            WITH temp AS (SELECT *
                          FROM queue
                          WHERE status = 'CALLED'
                            AND created_at >= CURRENT_DATE
                            AND gov_service_id IN (SELECT gov_service_id
                                                   FROM operator_service
                                                   WHERE operator_id = :operatorId))
            SELECT *
            FROM temp
            WHERE created_at = (SELECT MIN(created_at)
                                FROM temp) FOR UPDATE LIMIT 1
            """)
    Optional<Queue> getTopCalledByOperatorId(Integer operatorId);
}
