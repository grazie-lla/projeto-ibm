package com.example.projetoibm.controller;

import com.example.projetoibm.domain.Reserva;
import com.example.projetoibm.dto.ReservaDto;
import com.example.projetoibm.service.ReservaService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@WebMvcTest(ReservaController.class)
public class ResevaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservaService reservaService;

    @InjectMocks
    private ReservaController reservaController;

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Test
    public void createReserva() throws Exception{
        Reserva reserva = new Reserva(1, "Maria", new Date(), new Date(), 3, "CONFIRMADA");
        when(reservaService.createReserva(any(Reserva.class))).thenReturn(reserva);
        mockMvc.perform(MockMvcRequestBuilders.post("/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(reserva)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void testfindReservaById() throws Exception {
        Integer id = 1;
        Reserva foundReserva = new Reserva(id, "Maria", new Date(), new Date(), 3, "CONFIRMADA");
        when(reservaService.findReservaById(id)).thenReturn(foundReserva);
        mockMvc.perform(MockMvcRequestBuilders.get("/reservas/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void testFindAll() throws Exception {
        Reserva reserva = new Reserva(1, "grazi", new Date(), new Date(), 4, "PENDENTE");
        Reserva reserva2 = new Reserva(2, "Maria", new Date(), new Date(), 3, "CONFIRMADA");

        when(reservaService.findAll()).thenReturn(Arrays.asList(reserva, reserva2));
        mockMvc.perform(MockMvcRequestBuilders.get("/reservas"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2));
    }

    @Test
    public void updateReserva() throws Exception{
        Integer id = 1;
        ReservaDto reservaDtoMock = new ReservaDto(id, "grazi", new Date(), new Date(), 4, "pendente");
        Reserva reservaMock = new Reserva(id, "grazi", new Date(), new Date(), 4, "pendente");
        when(reservaService.fromDto(any(ReservaDto.class))).thenReturn(reservaMock);
        when(reservaService.updateReserva(reservaMock)).thenReturn(reservaMock);

       mockMvc.perform(MockMvcRequestBuilders.put("/reservas/1")
               .contentType(MediaType.APPLICATION_JSON)
               .content(gson.toJson(reservaDtoMock)))
               .andExpect(status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id));

    }
    @Test
    public void deleteReserva() throws Exception{
        Integer id = 1;
        Reserva deleteReserva = new Reserva(id, "Maria", new Date(), new Date(), 3, "CANCELADA");
        when(reservaService.deleteReserva(id)).thenReturn(deleteReserva);
        mockMvc.perform(MockMvcRequestBuilders.delete("/reservas/1/cancelar"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("CANCELADA"));
    }

}
