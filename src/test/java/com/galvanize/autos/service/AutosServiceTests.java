package com.galvanize.autos.service;

import com.galvanize.autos.data.AutosRepository;
import com.galvanize.autos.exception.AutoNotFoundException;
import com.galvanize.autos.model.Automobile;
import com.galvanize.autos.model.AutosList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
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

    @Test
    public void addAutoShouldAddAutoToAutosList() {
        Automobile automobile = new Automobile(1999, "Ford", "Mustang", "1FAFP4042XG123456");
        when(autosRepository.save(any(Automobile.class))).thenReturn(automobile);
        Automobile addedAuto = autosService.addAuto(automobile);
        assertThat(addedAuto).isNotNull();
        assertThat(addedAuto.getVin()).isEqualTo(automobile.getVin());
    }

    @Test
    public void getAutoByVinReturnsAutoWithMatchingVin() {
        Automobile automobile = new Automobile(1999, "Ford", "Mustang", "1FAFP4042XG123456");
        when(autosRepository.findByVin(anyString())).thenReturn(Optional.of(automobile));
        Automobile auto = autosService.getAutoByVin("1FAFP4042XG123456");
        assertThat(auto).isNotNull();
        assertThat(auto.getVin()).isEqualTo(automobile.getVin());
    }

    @Test
    public void updateAutoShouldUpdateAutoWithMatchingVin() {
        Automobile automobile = new Automobile(1999, "Ford", "Mustang", "1FAFP4042XG123456");
        automobile.setColor("Red");
        when(autosRepository.findByVin(anyString())).thenReturn(Optional.of(automobile));
        when(autosRepository.save(any(Automobile.class))).thenReturn(automobile);
        Automobile auto = autosService.updateAuto("1FAFP4042XG123456", "orange", "Jeffrey");
        assertThat(auto).isNotNull();
        assertThat(auto.getVin()).isEqualTo(automobile.getVin());
    }

    @Test
    public void deleteAutoShouldDeleteAutoWithMatchingVin() {
        Automobile automobile = new Automobile(1999, "Ford", "Mustang", "1FAFP4042XG123456");
        when(autosRepository.findByVin(anyString())).thenReturn(Optional.of(automobile));
        autosService.deleteAuto("1FAFP4042XG123456");
        verify(autosRepository).delete(any(Automobile.class));
    }

    @Test
    public void deleteAutoWhenAutoDoesNotExistShouldThrowException() {
        when(autosRepository.findByVin(anyString())).thenReturn(Optional.empty());

        assertThatExceptionOfType(AutoNotFoundException.class)
                .isThrownBy(() -> autosService.deleteAuto("1FAFP4042XG123457"));
    }

}
