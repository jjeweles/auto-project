package com.galvanize.autos.controller;

import com.galvanize.autos.UpdateOwnerRequest;
import com.galvanize.autos.exception.AutoNotFoundException;
import com.galvanize.autos.exception.InvalidAutoException;
import com.galvanize.autos.model.Automobile;
import com.galvanize.autos.model.AutosList;
import com.galvanize.autos.service.AutosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("autos")
    public Automobile addAuto(@RequestBody Automobile automobile) {
        return autosService.addAuto(automobile);
    }

    @GetMapping("autos/{vin}")
    public ResponseEntity<Automobile> getAutoByVin(@PathVariable String vin) {
        Automobile automobile = autosService.getAutoByVin(vin);
        return automobile == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(automobile);
    }

    @PatchMapping("autos/{vin}")
    public ResponseEntity<Automobile> updateAuto(@PathVariable String vin,
                                 @RequestBody UpdateOwnerRequest update) {
        Automobile automobile;
        if (autosService.getAutoByVin(vin) == null) {
            automobile = null;
        } else {
            automobile = autosService.updateAuto(vin, update.getColor(), update.getOwner());
            automobile.setColor(update.getColor());
            automobile.setOwner(update.getOwner());
        }

        return automobile == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(automobile);
    }

    @DeleteMapping("autos/{vin}")
    public ResponseEntity<Automobile> deleteAuto(@PathVariable String vin) {
        try {
            autosService.deleteAuto(vin);
        } catch (AutoNotFoundException e) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.accepted().build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void invalidAutoExceptionHandler(InvalidAutoException e) {
    }

}
