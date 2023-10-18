package uz.pdp.govqueue.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.govqueue.enums.LevelEnum;
import uz.pdp.govqueue.model.GovService;
import uz.pdp.govqueue.payload.LevelDTO;
import uz.pdp.govqueue.payload.ServiceDTO;
import uz.pdp.govqueue.repository.GovServiceRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final GovServiceRepository govServiceRepository;

    @Override
    public List<LevelDTO> getServicesForQueue() {

        List<GovService> govServices = govServiceRepository.findAllByStatusTrue();

        Map<LevelEnum, LevelDTO> levelDTOMap = new TreeMap<>(Comparator.comparing(LevelEnum::getPriority));

        for (GovService govService : govServices) {
            LevelDTO levelDTO = levelDTOMap.getOrDefault(govService.getLevel(), new LevelDTO(govService.getLevel()));
            levelDTO.getServices().add(mapServiceDTO(govService));
            levelDTOMap.putIfAbsent(levelDTO.getLevel(), levelDTO);
        }

        return new ArrayList<>(levelDTOMap.values());
    }

    @Override
    public ServiceDTO create(ServiceDTO serviceDTO) {

        if (govServiceRepository.existsByName(serviceDTO.getName()))
            throw new RuntimeException("Service already exists");

        GovService govService = GovService.builder()
                .name(serviceDTO.getName())
                .firstLetter(serviceDTO.getFirstLetter())
                .level(serviceDTO.getLevel())
                .status(serviceDTO.getStatus())
                .build();


        govService = govServiceRepository.save(govService);
        serviceDTO.setId(govService.getId());

        return serviceDTO;
    }

    private ServiceDTO mapServiceDTO(GovService govService) {
        return ServiceDTO.builder()
                .id(govService.getId())
                .firstLetter(govService.getFirstLetter())
                .level(govService.getLevel())
                .name(govService.getName())
                .status(govService.isStatus())
                .build();
    }
}
