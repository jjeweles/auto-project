package com.galvanize.autos.controller;

import com.galvanize.autos.model.AutosList;
import com.galvanize.autos.service.AutosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class AutosController {

    AutosService autosService;

    public AutosController(AutosService autosService) {
        this.autosService = autosService;
    }

    @GetMapping("autos")
    public ResponseEntity<AutosList> getAutos(@RequestParam(required = false) String color,
                                              @RequestParam(required = false) String make) {
        AutosList autosList;
        if (color == null && make == null) {
            autosList = autosService.getAutos();
        } else if (color == null) {
            autosList = autosService.getAutos(make);
        } else {
            autosList = autosService.getAutos(color, make);
        }
        return autosList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(autosList);
    }


}
