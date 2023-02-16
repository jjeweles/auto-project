package com.galvanize.autos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galvanize.autos.exception.InvalidAutoException;
import com.galvanize.autos.model.Automobile;
import com.galvanize.autos.model.AutosList;
import com.galvanize.autos.service.AutosService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutosController.class)
public class AutosControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AutosService autosService;

    ObjectMapper mapper = new ObjectMapper();

// * GET: /api/autos
    // GET: /api/autos Returns List of All Autos
    @Test
    void getAutosNoParamsExistsReturnsAutosList() throws Exception {
        List<Automobile> automobiles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            automobiles.add(new Automobile(1967 + i, "Ford", "Mustang", "AABB" + i));
        }

        when(autosService.getAutos()).thenReturn(new AutosList(automobiles));
        mockMvc.perform(get("/api/autos"))
                // .andDo(print()) Prints the response to the console
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }

    // GET: /api/autos Returns 204 No Autos Found
    @Test
    void getAutosNoParamsNoAutosReturns204NoContent() throws Exception {
        when(autosService.getAutos()).thenReturn(new AutosList());
        mockMvc.perform(get("/api/autos"))
                .andExpect(status().isNoContent());
    }

    // GET: /api/autos?make=ford Returns List of Autos with Make Ford
    @Test
    void getAutosWithColorParamReturnsAutosList() throws Exception {
        List<Automobile> automobiles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            automobiles.add(new Automobile(1967 + i, "Ford", "Mustang", "AABB" + i));
        }
        when(autosService.getAutos(anyString())).thenReturn(new AutosList(automobiles));
        mockMvc.perform(get("/api/autos?make=red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }
    // GET: /api/autos?make=ford&color=red Returns List of Autos with Make Ford and Color Red
    @Test
    void getAutosWithParamsReturnsAutosList() throws Exception {
        List<Automobile> automobiles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            automobiles.add(new Automobile(1967 + i, "Ford", "Mustang", "AABB" + i));
        }
        when(autosService.getAutos(anyString(), anyString())).thenReturn(new AutosList(automobiles));
        mockMvc.perform(get("/api/autos?make=ford&color=red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }

// * POST: /api/autos
    // POST: /api/autos Returns Created Auto
    @Test
    void addAutoReturnsCreatedAuto() throws Exception {
        Automobile automobile = new Automobile(1967, "Ford", "Mustang", "AABBCC");
        String json = mapper.writeValueAsString(automobile);
        when(autosService.addAuto(any(Automobile.class))).thenReturn(automobile);
        mockMvc.perform(post("/api/autos").contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("make").value("Ford"));
    }

    // POST: /api/autos Returns Error Message Due To Bad Request (400)
    @Test
    void addAutoReturnsErrorMessageDueToBadRequest() throws Exception {
        String json = "{\"make\": \"Ford\", \"model\": \"Mustang\", \"year\": \"1967\", \"vin\": \"AABBCC\"}";
        when(autosService.addAuto(any(Automobile.class))).thenThrow(InvalidAutoException.class);
        mockMvc.perform(post("/api/autos").contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

// * GET: /api/autos/{vin}
    // GET: /api/autos/{vin} Returns Requested Auto
    @Test
    void getAutoByVinReturnsAuto() throws Exception {
        Automobile automobile = new Automobile(1967, "Ford", "Mustang", "AABBCC");
        when(autosService.getAutoByVin(anyString())).thenReturn(automobile);
        mockMvc.perform(get("/api/autos/AABBCC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("make").value("Ford"))
                .andExpect(jsonPath("model").value("Mustang"))
                .andExpect(jsonPath("year").value(1967));
    }

    // GET: /api/autos/{vin} Returns NoContent (204) When Auto Not Found
    @Test
    void getAutoByVinReturnsNoContentWhenAutoNotFound() throws Exception {
        when(autosService.getAutoByVin(anyString())).thenReturn(null);
        mockMvc.perform(get("/api/autos/AABBCC"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

// * PATCH: /api/autos/{vin}
    // PATCH: /api/autos/{vin} Returns Patched Auto
    @Test
    void updateAutoReturnsPatchedAuto() throws Exception {
        Automobile automobile = new Automobile(1967, "Ford", "Mustang", "AABBCC");
        when(autosService.updateAuto(anyString(), anyString(), anyString())).thenReturn(automobile);
        when(autosService.getAutoByVin(anyString())).thenReturn(automobile);
        mockMvc.perform(patch("/api/autos/" + automobile.getVin())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"color\":\"red\",\"owner\":\"John\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("color").value("red"))
                .andExpect(jsonPath("owner").value("John"));
    }

    // PATCH: /api/autos/{vin} Returns NoContent (204) When Auto Not Found
    @Test
    void updateAutoReturnsNoContentWhenAutoNotFound() throws Exception {
        when(autosService.updateAuto(anyString(), anyString(), anyString())).thenReturn(null);
        mockMvc.perform(patch("/api/autos/AABBCC")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"color\":\"red\",\"owner\":\"John\"}"))
                .andExpect(status().isNoContent());
    }

    // PATCH: /api/autos/{vin} Returns 400 Bad Request (No Payload)
    @Test
    void updateAutoReturnsBadRequestWhenNoPayload() throws Exception {
        Automobile automobile = new Automobile(1967, "Ford", "Mustang", "AABBCC");
        when(autosService.updateAuto(anyString(), anyString(), anyString())).thenReturn(automobile);
        when(autosService.getAutoByVin(anyString())).thenReturn(automobile);
        mockMvc.perform(patch("/api/autos/" + automobile.getVin()))
                .andExpect(status().isBadRequest());
    }

// * DELETE: /api/autos/{vin}
    // DELETE: /api/autos/{vin} Returns 202, Delete Request Accepted
    @Test
    void deleteAutoReturns202DeleteRequestAccepted() throws Exception {
        Automobile automobile = new Automobile(1967, "Ford", "Mustang", "AABBCC");
        when(autosService.deleteAuto(anyString())).thenReturn(automobile);
        mockMvc.perform(delete("/api/autos/" + automobile.getVin()))
                .andExpect(status().isAccepted());
    }

    // DELETE: /api/autos/{vin} Returns 204, No Content (Auto Not Found)
    @Test
    void deleteAutoReturns204NoContentAutoNotFound() throws Exception {
        when(autosService.deleteAuto(anyString())).thenReturn(null);
        mockMvc.perform(delete("/api/autos/AABBCC"))
                .andExpect(status().isNoContent());
    }

}
