package com.galvanize.autos.service;

import com.galvanize.autos.data.AutosRepository;
import com.galvanize.autos.model.Automobile;
import com.galvanize.autos.model.AutosList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutosService {

    AutosRepository autosRepository;

    public AutosService(AutosRepository autosRepository) {
        this.autosRepository = autosRepository;
    }

    public AutosList getAutos() {
        // Query DB: SELECT * FROM autos;
        // Put results into AutosList
        // Return new AutosList with results
        return new AutosList(autosRepository.findAll());
    }

    public AutosList getAutos(String color, String make) {
//        List<Automobile> automobiles = autosRepository.findByColorContainsAndMakeContains(color, make);
//        if (!automobiles.isEmpty()) {
//            return new AutosList(automobiles);
//        }
        return new AutosList(autosRepository.findByColorContainsAndMakeContains(color, make));
    }

    public AutosList getAutos(String make) {
        // Query DB: SELECT * FROM autos WHERE make = ?;
        // Put results into AutosList
        // Return new AutosList with results
        return new AutosList(autosRepository.findByMakeContains(make));
    }

    public Automobile addAuto(Automobile any) {
        return null;
    }

    public Automobile getAutoByVin(String vin) {
        return null;
    }

    public Automobile updateAuto(String vin, String color, String owner) {
        return null;
    }

    public Automobile deleteAuto(String vin) {
        return null;
    }
}
