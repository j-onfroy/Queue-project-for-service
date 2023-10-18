package uz.pdp.govqueue.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.govqueue.enums.LevelEnum;
import uz.pdp.govqueue.model.GovService;
import uz.pdp.govqueue.payload.LevelDTO;
import uz.pdp.govqueue.payload.GovServiceDTO;
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
    public GovServiceDTO create(GovServiceDTO govServiceDTO) {

        if (govServiceRepository.existsByName(govServiceDTO.getName()))
            throw new RuntimeException("Service already exists");

        GovService govService = GovService.builder()
                .name(govServiceDTO.getName())
                .firstLetter(govServiceDTO.getFirstLetter())
                .level(govServiceDTO.getLevel())
                .status(govServiceDTO.getStatus())
                .build();


        govService = govServiceRepository.save(govService);
        govServiceDTO.setId(govService.getId());

        return govServiceDTO;
    }

    private GovServiceDTO mapServiceDTO(GovService govService) {
        return GovServiceDTO.builder()
                .id(govService.getId())
                .firstLetter(govService.getFirstLetter())
                .level(govService.getLevel())
                .name(govService.getName())
                .status(govService.isStatus())
                .build();
    }
}
