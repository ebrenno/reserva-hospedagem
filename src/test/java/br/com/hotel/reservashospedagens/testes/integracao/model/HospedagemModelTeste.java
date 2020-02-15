/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.testes.integracao.model;

import br.com.hotel.reservashospedagens.exception.HospedagemNaoExisteException;
import br.com.hotel.reservashospedagens.model.hospedagem.HospedagemModel;
import br.com.hotel.reservashospedagens.model.hospedagem.HospedagemStatusEnum;
import br.com.hotel.reservashospedagens.persistencia.entidade.ClienteEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.HospedagemEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.QuartoEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaTipoQuarto;
import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaTipoQuartoId;
import br.com.hotel.reservashospedagens.persistencia.entidade.ServicoEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.TipoQuartoEntity;
import br.com.hotel.reservashospedagens.persistencia.repositorio.QuartoRepositorio;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
public class HospedagemModelTeste {

    @Autowired
    TestEntityManager em;
    @Autowired
    QuartoRepositorio quartoRepositorio;
    @Autowired
    HospedagemModel hospedagemModel;

    @Test
    public void RetornaQuartosLivres() {
        TipoQuartoEntity solteiro = new TipoQuartoEntity();
        TipoQuartoEntity casal = new TipoQuartoEntity();
        em.persist(solteiro);
        em.persist(casal);
        List<QuartoEntity> quartos = Arrays.asList(
                new QuartoEntity(solteiro, 0, 0),
                new QuartoEntity(solteiro, 0, 0),
                new QuartoEntity(casal, 0, 0),
                new QuartoEntity(casal, 0, 0)
        );
        quartos.forEach(quarto -> em.persist(quarto));
        Long count = quartoRepositorio.count();
        Assertions.assertEquals(quartos.size(), count.intValue());

        ClienteEntity cliente = new ClienteEntity();
        em.persist(cliente);
        LocalDate checkin = LocalDate.now().minusDays(5);
        LocalDate checkout = LocalDate.now().plusDays(5);
        HospedagemEntity hospedagem = new HospedagemEntity(cliente, HospedagemStatusEnum.INICIADA, checkin, checkout);
        hospedagem.incluirQuartos(Arrays.asList(quartos.get(0), quartos.get(2)));
        em.persist(hospedagem);

        Collection<QuartoEntity> quartosLivres = hospedagemModel.encontrarQuartosLivres();
        Assertions.assertEquals(2, quartosLivres.size());

    }

    @Test
    public void EscolheQuartosVagosOrdenadosPorAndar() {
        ReservaEntity reserva = new ReservaEntity();
        TipoQuartoEntity solteiro = new TipoQuartoEntity();
        em.persist(solteiro);
        TipoQuartoEntity casal = new TipoQuartoEntity();
        em.persist(casal);
        Collection<ReservaTipoQuarto> tiposReservados = Arrays.asList(
                new ReservaTipoQuarto(new ReservaTipoQuartoId(reserva, solteiro), 2),
                new ReservaTipoQuarto(new ReservaTipoQuartoId(reserva, casal), 1)
        );
        reserva.getQuartosReservados().addAll(tiposReservados);
        em.persist(reserva);

        List<QuartoEntity> quartos = Arrays.asList(
                new QuartoEntity(casal, 0, 3),
                new QuartoEntity(casal, 0, 1),
                new QuartoEntity(solteiro, 0, 1),
                new QuartoEntity(casal, 0, 2),
                new QuartoEntity(solteiro, 0, 2)
        );
        quartos.forEach(quarto -> em.persist(quarto));

        List<QuartoEntity> result = hospedagemModel.selecionarQuartosParaHospedagem(tiposReservados);
        Assertions.assertEquals(3, result.size());
        Assertions.assertTrue(result.contains(quartos.get(1)));
        Assertions.assertTrue(result.contains(quartos.get(2)));
        Assertions.assertTrue(result.contains(quartos.get(4)));

    }

    @Test
    public void RetornaUmaHospedagemBaseadaNoQuarto() {
        LocalDate checkin = LocalDate.now().minusDays(5);
        LocalDate checkout = LocalDate.now().plusDays(5);
        QuartoEntity quarto = new QuartoEntity(new TipoQuartoEntity(), 0, 0);
        em.persist(quarto);
        ClienteEntity cliente = new ClienteEntity();
        em.persist(cliente);
        HospedagemEntity hospedagem = new HospedagemEntity(cliente, HospedagemStatusEnum.INICIADA, checkin, checkout);
        em.persist(hospedagem);
        ServicoEntity servico = new ServicoEntity();
        em.persist(servico);
        hospedagem.incluirQuarto(quarto);

        HospedagemEntity result = hospedagemModel.encontrarPorQuarto(quarto);
        Assertions.assertEquals(hospedagem, result);
    }

    @Test
    public void expect_HospedagemNaoEncontradaException() {
        ClienteEntity cliente = new ClienteEntity();
        em.persist(cliente);

        Assertions.assertThrows(HospedagemNaoExisteException.class, () -> hospedagemModel.encontrarHospedagensIniciadas(cliente));
    }

}
