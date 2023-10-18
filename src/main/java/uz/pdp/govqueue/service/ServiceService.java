package uz.pdp.govqueue.service;

import uz.pdp.govqueue.payload.LevelDTO;
import uz.pdp.govqueue.payload.GovServiceDTO;

import java.util.List;

public interface ServiceService {

    List<LevelDTO> getServicesForQueue();

    GovServiceDTO create(GovServiceDTO govServiceDTO);
}
