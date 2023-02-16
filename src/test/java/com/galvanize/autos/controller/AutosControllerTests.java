package com.galvanize.autos.controller;

import com.galvanize.autos.model.Automobile;
import com.galvanize.autos.model.AutosList;
import com.galvanize.autos.service.AutosService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

// GET: /api/autos

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

    // ! GET: /api/autos?color=red Returns List of Autos with Color Red
    // TODO fix this test

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

// POST: /api/autos
    // POST: /api/autos Returns Created Auto
    // POST: /api/autos Returns Error Message Due To Bad Request (400)

// GET: /api/autos/{vin}
    // GET: /api/autos/{vin} Returns Requested Auto
    // GET: /api/autos/{vin} Returns NoContent (204) When Auto Not Found

// PATCH: /api/autos/{vin}
    // PATCH: /api/autos/{vin} Returns Patched Auto
    // PATCH: /api/autos/{vin} Returns NoContent (204) When Auto Not Found
    // PATCH: /api/autos/{vin} Returns 400 Bad Request (No Payload, No Changes, Already Done?)

// DELETE: /api/autos/{vin}
    // DELETE: /api/autos/{vin} Returns 202, Delete Request Accepted
    // DELETE: /api/autos/{vin} Returns 204, No Content (Auto Not Found)


}
