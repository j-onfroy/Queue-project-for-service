package uz.pdp.govqueue.repository;

import org.springframework.data.repository.CrudRepository;
import uz.pdp.govqueue.model.Operator;

public interface OperatorRepository extends CrudRepository<Operator, Integer> {
}
