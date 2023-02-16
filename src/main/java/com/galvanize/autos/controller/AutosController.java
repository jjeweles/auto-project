package com.galvanize.autos.controller;

import com.galvanize.autos.model.AutosList;
import com.galvanize.autos.service.AutosService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class AutosController {

    AutosService autosService;

    public AutosController(AutosService autosService) {
        this.autosService = autosService;
    }

    @GetMapping("autos")
    public AutosList getAutos() {
        return autosService.getAutos();
    }

}
