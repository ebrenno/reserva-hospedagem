/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.testes.integracao.model;

import br.com.hotel.reservashospedagens.dto.reserva.VagaDto;
import br.com.hotel.reservashospedagens.model.reserva.ReservaModel;
import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoEnum;
import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoModel;
import br.com.hotel.reservashospedagens.persistencia.entidade.ClienteEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.QuartoEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.TipoQuartoEntity;
import br.com.hotel.reservashospedagens.persistencia.repositorio.ReservaRepositorio;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureTestEntityManager
@AutoConfigureTestDatabase
public class ReservaModelTeste {

    @Autowired
    TestEntityManager em;
    @Autowired
    ReservaModel reservaModel;
    @Autowired
    ReservaRepositorio reservaRepositorio;
    @Autowired
    TipoQuartoModel tipoQuartoModel;

    @BeforeEach
    public void before() {
        TipoQuartoEntity solteiro = new TipoQuartoEntity("quarto com cama de solteiro", TipoQuartoEnum.SOLTEIRO, 100d);
        TipoQuartoEntity casal = new TipoQuartoEntity("quarto com cama de casal", TipoQuartoEnum.CASAL, 300d);
        for (int i = 0; i < 5; i++) {
            int andar = 1;
            int numeroSolteiro = 101 + (i * 2);
            int numerocasal = 102 + (i * 2);
            QuartoEntity quartoSolteiro = new QuartoEntity(solteiro, numeroSolteiro, andar);
            QuartoEntity quartoCasal = new QuartoEntity(casal, numerocasal, andar);
            em.persist(quartoSolteiro);
            em.persist(quartoCasal);

        }
    }

    @Test
    public void ReservaUmQuartoDeSolteiro() throws Throwable {
        try {
            LocalDate checkin = LocalDate.parse("2020-01-05");
            LocalDate checkout = LocalDate.parse("2020-05-08");
            ClienteEntity cliente = new ClienteEntity();
            em.persist(cliente);
            TipoQuartoEntity tipo = tipoQuartoModel.getEntity(TipoQuartoEnum.SOLTEIRO);
            VagaDto vaga = new VagaDto(TipoQuartoEnum.SOLTEIRO, 1, 0);
            Collection<VagaDto> vagas = Arrays.asList(vaga);

            reservaModel.iniciar(cliente, vagas, checkin, checkout);

            int restam = reservaModel.vagasRestantes(tipo, checkin, checkout);
            Assertions.assertEquals(4, restam);
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

}
