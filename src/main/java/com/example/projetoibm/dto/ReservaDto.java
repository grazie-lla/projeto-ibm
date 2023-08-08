package com.example.projetoibm.dto;

import com.example.projetoibm.domain.Reserva;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class ReservaDto implements Serializable {


    private Integer id;

    @NotNull
    @NotBlank
    private String nomeHospede;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dataInicio;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dataFim;

    @NotNull
    private Integer quantidadePessoas;

    private String status;

    public ReservaDto (Reserva reserva){
        id = reserva.getId();
        nomeHospede = reserva.getNomeHospede();
        dataInicio = reserva.getDataInicio();
        dataFim = reserva.getDataFim();
        quantidadePessoas = reserva.getQuantidadePessoas();
        status = reserva.getStatus();
    }

    public ReservaDto(Integer id, @NotNull String nomeHospede, @NotNull Date dataInicio, @NotNull Date dataFim, @NotNull Integer quantidadePessoas, String status) {
        this.id = id;
        this.nomeHospede = nomeHospede;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.quantidadePessoas = quantidadePessoas;
        this.status = status;
    }
}
