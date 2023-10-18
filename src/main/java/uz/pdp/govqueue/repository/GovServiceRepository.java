package uz.pdp.govqueue.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.govqueue.model.GovService;

import java.util.List;


@Repository
public interface GovServiceRepository extends CrudRepository<GovService, Integer> {

    boolean existsByName(String name);

    List<GovService> findAllByStatusTrue();
}
