package ru.hogwarts.hogwarts.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.hogwarts.service.InfoService;

@RestController
@RequestMapping("/getPort")
public class InfoController {
    private final InfoService infoService;

    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }


    @GetMapping
    public int getPortApp() {
        return infoService.getPortApp();
    }
}
