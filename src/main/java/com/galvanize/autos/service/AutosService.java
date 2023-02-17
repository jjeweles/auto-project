package com.galvanize.autos.service;

import com.galvanize.autos.data.AutosRepository;
import com.galvanize.autos.exception.AutoNotFoundException;
import com.galvanize.autos.model.Automobile;
import com.galvanize.autos.model.AutosList;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        List<Automobile> automobiles = autosRepository.findByColorContainsAndMakeContains(color, make);
        //if (!automobiles.isEmpty()) {
        //    return new AutosList(automobiles);
        //}
        return automobiles == null ? null : new AutosList(autosRepository.findByColorContainsAndMakeContains(color, make));
    }

    public AutosList getAutos(String make) {
        List<Automobile> automobiles = autosRepository.findByMakeContains(make);
        //if (!automobiles.isEmpty()) {
        //    return new AutosList(automobiles);
        //}
        return automobiles == null ? null : new AutosList(autosRepository.findByMakeContains(make));
    }

    public Automobile addAuto(Automobile automobile) {
        return autosRepository.save(automobile);
    }

    public Automobile getAutoByVin(String vin) {
        return autosRepository.findByVin(vin).orElse(null);
    }

    public Automobile updateAuto(String vin, String color, String owner) {
        Optional<Automobile> oAuto = autosRepository.findByVin(vin);
        if (oAuto.isPresent()) {
            oAuto.get().setColor(color);
            oAuto.get().setOwner(owner);
            return autosRepository.save(oAuto.get());
        }
        return null;
    }

    public Automobile deleteAuto(String vin) {
        Optional<Automobile> oAuto = autosRepository.findByVin(vin);
        if (oAuto.isPresent()) {
            autosRepository.delete(oAuto.get());
            return oAuto.get();
        } else {
            throw new AutoNotFoundException();
        }
    }
}
