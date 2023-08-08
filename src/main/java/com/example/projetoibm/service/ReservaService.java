package com.example.projetoibm.service;

import com.example.projetoibm.domain.Reserva;
import com.example.projetoibm.dto.ReservaDto;
import com.example.projetoibm.repository.ReservaRepository;
import com.example.projetoibm.service.exceptions.IllegalArgumentException;
import com.example.projetoibm.service.exceptions.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.Date;
import java.util.List;
import java.util.Optional;

@Valid
@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public Reserva createReserva(Reserva novaReserva) {

        validateDate(novaReserva.getDataInicio(), novaReserva.getDataFim());
        validateExistReserva(novaReserva.getDataInicio(), novaReserva.getDataFim(), novaReserva.getStatus());
        novaReserva.setId(null);
        novaReserva.setStatus("CONFIRMADA");
        novaReserva = reservaRepository.save(novaReserva);
        return novaReserva;
    }

    public Reserva findReservaById(Integer id) {
        Optional<Reserva> reserva = reservaRepository.findById(id);
        return reserva.orElseThrow(() -> new ObjectNotFoundException("Reserva não encontrada!"));
    }

    public List<Reserva> findAll() {
        List<Reserva> reservaList = reservaRepository.findAll();
        return reservaList;
    }

    public Reserva updateReserva(Reserva reserva) {
        validateDate(reserva.getDataInicio(), reserva.getDataFim());

        validateExistReserva(reserva.getDataInicio(), reserva.getDataFim(), reserva.getStatus());

        Reserva updatedReserva = findReservaById(reserva.getId());

        validateStatus(updatedReserva);

        updatedReserva.setNomeHospede(reserva.getNomeHospede());
        updatedReserva.setDataInicio(reserva.getDataInicio());
        updatedReserva.setDataFim(reserva.getDataFim());
        updatedReserva.setQuantidadePessoas(reserva.getQuantidadePessoas());
        updatedReserva.setStatus(reserva.getStatus());
        return reservaRepository.save(updatedReserva);
    }

    public Reserva deleteReserva(Integer id) {
        Reserva cancelReserva = findReservaById(id);
        validateStatus(cancelReserva);
        cancelReserva.setStatus("CANCELADA");
        return reservaRepository.save(cancelReserva);
    }

    public Reserva fromDto(ReservaDto reservaDto){
        return new Reserva(reservaDto.getId(),reservaDto.getNomeHospede(), reservaDto.getDataInicio(), reservaDto.getDataFim(), reservaDto.getQuantidadePessoas(), reservaDto.getStatus());
    }

    private void validateDate(Date dataInicio, Date dataFim) {
        Date dataAtual = new Date();
        if (dataInicio.before(dataAtual) || dataFim.before(dataInicio)) {
            throw new IllegalArgumentException("As datas da reserva não são válidas.");
        }
    }

    private void validateExistReserva(Date dataInicio, Date dataFim, String status) {
        if ("CANCELADA".equalsIgnoreCase(status)) {
            return;
        }

        boolean reservaExist = reservaRepository.existByDate(dataInicio, dataFim);
        if (reservaExist) {
            throw new IllegalArgumentException("Já existe uma reserva na data escolhida.");
        }
    }

    private void validateStatus(Reserva reserva) {
        if ("CANCELADA".equalsIgnoreCase(reserva.getStatus())) {
            throw new IllegalArgumentException("Não é possivel modificar essa reserva, pois ela já está cancelada.");
        }
    }

}
