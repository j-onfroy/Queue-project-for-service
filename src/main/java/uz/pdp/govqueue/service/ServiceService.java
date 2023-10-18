package uz.pdp.govqueue.service;

import uz.pdp.govqueue.payload.LevelDTO;
import uz.pdp.govqueue.payload.ServiceDTO;

import java.util.List;

public interface ServiceService {

    List<LevelDTO> getServicesForQueue();

    ServiceDTO create(ServiceDTO serviceDTO);
}
