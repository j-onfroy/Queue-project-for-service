package uz.pdp.govqueue.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import uz.pdp.govqueue.model.Operator;

public interface OperatorRepository extends CrudRepository<Operator, Integer> {

    @Query(value = """
            SELECT COUNT(DISTINCT o.id)
            FROM operator o
                     JOIN operator_service os on o.id = os.operator_id
                     JOIN gov_service gs on gs.id = os.gov_service_id
            WHERE o.status
              AND gs.first_letter = :firstLetter
            """)
    long countAllByStatusTrueAndServiceIncludes(@Param("firstLetter") String firstLetter);
}
