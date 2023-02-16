package com.galvanize.autos.service;

import com.galvanize.autos.model.Automobile;
import com.galvanize.autos.model.AutosList;
import org.springframework.stereotype.Service;

@Service
public class AutosService {
    public AutosList getAutos() {
        return null;
    }

    public AutosList getAutos(String color, String make) {
        return null;
    }

    public AutosList getAutos(String make) {
        return null;
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
