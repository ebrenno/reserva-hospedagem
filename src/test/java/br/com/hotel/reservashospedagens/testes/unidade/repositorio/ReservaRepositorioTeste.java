/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.testes.unidade.repositorio;

import br.com.hotel.reservashospedagens.persistencia.entidade.ClienteEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaTipoQuarto;
import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaTipoQuartoId;
import br.com.hotel.reservashospedagens.persistencia.entidade.TipoQuartoEntity;
import br.com.hotel.reservashospedagens.persistencia.repositorio.ReservaRepositorio;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class ReservaRepositorioTeste {

    Logger log = LogManager.getLogger(ReservaRepositorioTeste.class);
    @Autowired
    ReservaRepositorio reservaRepositorio;
    @Autowired
    TestEntityManager em;

    @Test
    public void MinimoParaPersistirUmaReserva() {
        ReservaEntity entity = new ReservaEntity();
        ReservaEntity result = em.persist(entity);
        Assertions.assertEquals(entity, result);
    }

    @Test
    public void DeveRetornarDuasReservasNoPeriodoEspecificado() {
        ClienteEntity cliente = new ClienteEntity("001", "joao", "M");
        cliente = em.persist(cliente);
        List<ReservaEntity> inserts = Arrays.asList(
                new ReservaEntity(cliente, LocalDate.parse("2020-01-05"), LocalDate.parse("2020-01-09")),
                new ReservaEntity(cliente, LocalDate.parse("2020-01-15"), LocalDate.parse("2020-01-18")),
                new ReservaEntity(cliente, LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-03"))
        );

        inserts.forEach(reserva -> em.persist(reserva));

        LocalDate in = LocalDate.parse("2020-01-08");
        LocalDate out = LocalDate.parse("2020-01-16");
        int quantidade = reservaRepositorio.findReservasBetween(in, out).size();

        Assertions.assertTrue(quantidade == 2, "retornou: " + quantidade);

    }

    @Test
    public void EncontraUmaReserva() {
        ClienteEntity clienteEntity = new ClienteEntity("000", "joao", "M");
        em.persist(clienteEntity);
        ReservaEntity reservaEntity = new ReservaEntity(clienteEntity, LocalDate.MIN, LocalDate.MIN);
        em.persist(reservaEntity);
        Optional<ReservaEntity> result = reservaRepositorio.findById(reservaEntity.getId());
        Assertions.assertTrue(result.get().equals(reservaEntity));
    }

    @Test
    public void PersistirUmQuartoNaReservaAutomaticamente() {
        ReservaEntity reservaEntity = new ReservaEntity();
        em.persist(reservaEntity);
        TipoQuartoEntity tipoQuartoEntity = new TipoQuartoEntity();
        em.persist(tipoQuartoEntity);
        ReservaTipoQuartoId reservaTipoQuartoId = new ReservaTipoQuartoId(reservaEntity, tipoQuartoEntity);
        ReservaTipoQuarto reservaTipoQuarto = new ReservaTipoQuarto(reservaTipoQuartoId, 2);
        //quando
        reservaEntity.addTipoQuarto(reservaTipoQuarto);
        ReservaEntity result = reservaRepositorio.findById(reservaEntity.getId()).get();
        //então
        Assertions.assertTrue(result.getQuartosReservados().size() == 1);
    }

}
