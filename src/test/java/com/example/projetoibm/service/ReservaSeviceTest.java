package com.example.projetoibm.service;

import com.example.projetoibm.domain.Reserva;
import com.example.projetoibm.repository.ReservaRepository;
import com.example.projetoibm.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ReservaSeviceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

    LocalDateTime dataAtual = LocalDateTime.now();
    LocalDateTime dataInicio = dataAtual.plusDays(1);
    Date dataInicioDate = Date.from(dataInicio.toInstant(ZoneOffset.UTC));
    LocalDateTime dataFim = dataAtual.plusDays(2);
    Date dataFimDate = Date.from(dataFim.toInstant(ZoneOffset.UTC));
    @Test
    public void testFindAll(){
        List<Reserva> reservaList = reservaService.findAll();
        assertNotNull(reservaList);
    }

    @Test
    public void testFindById()throws ObjectNotFoundException {
        //entrada de dados
        Integer id = 1;
        //cria resposta esperada
        Reserva reservaMock = new Reserva(id, "julia", new Date(), new Date(), 4, "ok");
        //mock vai retornar resposta esperada com a entrada de dados
        when(reservaRepository.findById(1)).thenReturn(Optional.of(reservaMock));
        //executa o metodo q quer testar
        Reserva reserva = reservaService.findReservaById(id);
        //comparar o dado de entrada e a resposta esperada
        assertEquals(reserva.getId(), reservaMock.getId());
        verify(reservaRepository, times(1)).findById(id);
    }
    @Test
    public void testFindByIdNotFound() {

        assertThrows(ObjectNotFoundException.class, () -> {
                Reserva reserva = reservaService.findReservaById(2);
            });
        verify(reservaRepository, times(1)).findById(2);
    }

    @Test
    public void testCreateReserva() throws IllegalArgumentException {

        Reserva reserva = new Reserva(1, "maria", dataInicioDate, dataFimDate, 4, "CONFIRMADA");

        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        Reserva  novaReserva = reservaService.createReserva(reserva);

        assertNotNull(novaReserva);
        assertEquals("CONFIRMADA", novaReserva.getStatus());
    }

    @Test
    public void testDeleteReserva() {
        Integer id = 1;
        Reserva reservaCancelada = new Reserva(id, "Maria", new Date(), new Date(), 2, "CANCELADA");
        Reserva reservaExistente = new Reserva(id, "Maria", new Date(), new Date(), 2, "CONFIRMADA");

        when(reservaRepository.findById(id)).thenReturn(Optional.of(reservaExistente));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaCancelada);

        Reserva deleteReserva = reservaService.deleteReserva(id);
        assertNotNull(deleteReserva);
        assertEquals("CANCELADA", deleteReserva.getStatus());
    }

    @Test
    public void testUpdateReserva() {
        Integer id = 1;
        Reserva updatedReserva = new Reserva(id, "Maria", dataInicioDate, dataFimDate, 2, "PENDENTE");
        Reserva oldReserva = new Reserva(id, "Maria", new Date(), new Date(), 2, "CONFIRMADA");

        when(reservaRepository.findById(id)).thenReturn(Optional.of(oldReserva));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(updatedReserva);

        Reserva updateReserva = reservaService.updateReserva(updatedReserva);
        assertNotNull(updateReserva);
        assertEquals(updatedReserva.getNomeHospede(), updateReserva.getNomeHospede());
        assertEquals("PENDENTE", updateReserva.getStatus());

    }
}
