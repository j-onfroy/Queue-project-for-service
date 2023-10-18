package uz.pdp.govqueue.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.pdp.govqueue.payload.LevelDTO;
import uz.pdp.govqueue.payload.ServiceDTO;

import java.util.List;

@RequestMapping(ServiceController.BASE_PATH)
public interface ServiceController {

    String BASE_PATH = "/service";

    @GetMapping("/for-queue")
    HttpEntity<List<LevelDTO>> forQueue();

    @PostMapping
    HttpEntity<ServiceDTO> add(@Valid @RequestBody ServiceDTO serviceDTO);

    /*
    [
    {   "level":"EASY",
        "services":[
                    {
                        "id":1,
                        "name":"Passport",
                        "status":true
                    },
                ]
    },
    {   "level":"HARD",
        "services":[]
        }
    ]
     */
}
