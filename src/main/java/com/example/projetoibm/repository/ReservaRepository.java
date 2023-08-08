package com.example.projetoibm.repository;

import com.example.projetoibm.domain.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    @Query("SELECT CASE WHEN COUNT(reserva) > 0 THEN true ELSE false END FROM Reserva reserva WHERE reserva.dataInicio = :dataInicio OR reserva.dataFim = :dataFim")
    boolean existByDate(@Param("dataInicio")Date dataInicio, @Param("dataFim") Date dataFim);
}
