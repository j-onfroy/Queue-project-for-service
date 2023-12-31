package uz.pdp.govqueue.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.govqueue.payload.LevelDTO;
import uz.pdp.govqueue.payload.GovServiceDTO;
import uz.pdp.govqueue.service.ServiceService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GovServiceControllerImpl implements GovServiceController {

    private final ServiceService serviceService;

    @Override
    public HttpEntity<List<LevelDTO>> forQueue() {
        List<LevelDTO> servicesForQueue = serviceService.getServicesForQueue();
        return ResponseEntity.ok(servicesForQueue);
    }

    @Override
    public HttpEntity<GovServiceDTO> add(GovServiceDTO govServiceDTO) {
        govServiceDTO = serviceService.create(govServiceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(govServiceDTO);
    }
}
