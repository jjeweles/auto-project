package com.galvanize.autos.service;

import com.galvanize.autos.data.AutosRepository;
import com.galvanize.autos.model.Automobile;
import com.galvanize.autos.model.AutosList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AutosServiceTests {

    private AutosService autosService;

    @Mock
    private AutosRepository autosRepository;

    @BeforeEach
    public void setUp() {
        autosService = new AutosService(autosRepository);
    }

    @Test
    public void getAutosReturnsListOfAllAutosInList() {
        Automobile automobile = new Automobile(1999, "Ford", "Mustang", "1FAFP4042XG123456");
        when(autosRepository.findAll()).thenReturn(List.of(automobile));
        AutosList autosList = autosService.getAutos();
        assertThat(autosList).isNotNull();
        assertThat(autosList.isEmpty()).isFalse();
    }

    @Test
    public void getAutosWithParamsReturnsListOfAutosWithMatchingParamsColorAndMake() {
        Automobile automobile = new Automobile(1999, "Ford", "Mustang", "1FAFP4042XG123456");
        automobile.setColor("Red");
        when(autosRepository.findByColorContainsAndMakeContains(anyString(), anyString()))
                .thenReturn(List.of(automobile));
        AutosList autosList = autosService.getAutos("Red", "Ford");
        assertThat(autosList).isNotNull();
        assertThat(autosList.isEmpty()).isFalse();
    }

    @Test
    public void getAutosWithOneParamMakeReturnsListOfAutosWithMatchingParamMake() {
        Automobile automobile = new Automobile(1999, "Ford", "Mustang", "1FAFP4042XG123456");
        when(autosRepository.findByMakeContains(anyString()))
                .thenReturn(List.of(automobile));
        AutosList autosList = autosService.getAutos("Ford");
        assertThat(autosList).isNotNull();
        assertThat(autosList.isEmpty()).isFalse();
    }

}
