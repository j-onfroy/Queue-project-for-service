package uz.pdp.govqueue.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.pdp.govqueue.payload.LevelDTO;
import uz.pdp.govqueue.payload.GovServiceDTO;

import java.util.List;

@RequestMapping(GovServiceController.BASE_PATH)
public interface GovServiceController {

    String BASE_PATH = "/service";

    @GetMapping("/for-queue")
    HttpEntity<List<LevelDTO>> forQueue();

    @PostMapping
    HttpEntity<GovServiceDTO> add(@Valid @RequestBody GovServiceDTO govServiceDTO);

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
