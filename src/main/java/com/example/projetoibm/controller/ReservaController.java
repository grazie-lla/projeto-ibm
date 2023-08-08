package com.example.projetoibm.controller;

import com.example.projetoibm.domain.Reserva;
import com.example.projetoibm.dto.ReservaDto;
import com.example.projetoibm.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping
    public ResponseEntity<Reserva> createReserva(@Valid @RequestBody Reserva reserva){
        Reserva novaReserva = reservaService.createReserva(reserva);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand((novaReserva.getId())).toUri();
        return ResponseEntity.created(uri).body(novaReserva);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> findReservaById(@PathVariable Integer id){
        Reserva reserva = reservaService.findReservaById(id);
        return ResponseEntity.ok().body(reserva);
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> findAll(){
        List<Reserva> reservaList = reservaService.findAll();
        return ResponseEntity.ok().body(reservaList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> updateReserva(@Valid @PathVariable Integer id, @RequestBody ReservaDto reservaDto){
        Reserva updatedReserva = reservaService.fromDto(reservaDto);
        updatedReserva.setId(id);
        reservaService.updateReserva(updatedReserva);
        return ResponseEntity.ok().body(updatedReserva);
    }

    @DeleteMapping("/{id}/cancelar")
    public ResponseEntity<Reserva> deleteReserva(@PathVariable Integer id){
        Reserva cancelReserva = reservaService.deleteReserva(id);
        return ResponseEntity.ok().body(cancelReserva);
    }

}
