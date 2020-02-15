/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.testes.unidade.repositorio;

import br.com.hotel.reservashospedagens.model.hospedagem.HospedagemStatusEnum;
import br.com.hotel.reservashospedagens.persistencia.entidade.ClienteEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.HospedagemEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.HospedagemServicoEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.QuartoEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.ServicoEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.TipoQuartoEntity;
import br.com.hotel.reservashospedagens.persistencia.repositorio.HospedagemRepositorio;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class HospedagemRepositorioTeste {

    @Autowired
    HospedagemRepositorio hospedagemRepositorio;
    @Autowired
    TestEntityManager em;

    @Test
    public void RetornaHospedagemFiltradaPorQuartoEData() {
        ClienteEntity clienteEntity = new ClienteEntity();
        em.persist(clienteEntity);
        TipoQuartoEntity tipoQuartoEntity = new TipoQuartoEntity();
        em.persist(tipoQuartoEntity);
        QuartoEntity quartoEntity = new QuartoEntity(tipoQuartoEntity, 202, 2);
        em.persist(quartoEntity);
        HospedagemEntity hospedagem1 = new HospedagemEntity(clienteEntity, HospedagemStatusEnum.INICIADA, LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-05"));
        HospedagemEntity hospedagem2 = new HospedagemEntity(clienteEntity, HospedagemStatusEnum.INICIADA, LocalDate.parse("2020-01-06"), LocalDate.parse("2020-01-10"));
        hospedagem1.incluirQuarto(quartoEntity);
        hospedagem2.incluirQuarto(quartoEntity);
        em.persist(hospedagem1);
        em.persist(hospedagem2);

        HospedagemEntity result = hospedagemRepositorio.findByQuarto(quartoEntity, LocalDate.parse("2020-01-03"));

        Assertions.assertEquals(hospedagem1, result);
    }

    @Test
    public void RetornaQuartosOcupadosNesteMomento() {
        LocalDate checkin = LocalDate.now().minusDays(5);
        LocalDate checkout = LocalDate.now().plusDays(5);
        ClienteEntity cliente = new ClienteEntity();
        em.persist(cliente);
        TipoQuartoEntity solteiro = new TipoQuartoEntity();
        em.persist(solteiro);
        TipoQuartoEntity casal = new TipoQuartoEntity();
        em.persist(casal);
        List<QuartoEntity> quartos = Arrays.asList(
                new QuartoEntity(casal, 0, 0),
                new QuartoEntity(casal, 0, 0),
                new QuartoEntity(solteiro, 0, 0),
                new QuartoEntity(solteiro, 0, 0)
        );
        quartos.forEach(quarto -> em.persist(quarto));

        HospedagemEntity h1 = new HospedagemEntity(cliente, HospedagemStatusEnum.INICIADA, checkin, checkout);
        h1.incluirQuartos(Arrays.asList(quartos.get(0), quartos.get(2)));
        em.persist(h1);
        HospedagemEntity h2 = new HospedagemEntity(cliente, HospedagemStatusEnum.INICIADA, checkin, checkout);
        h1.incluirQuarto(quartos.get(1));
        em.persist(h2);

        List<QuartoEntity> quartosOcupados = hospedagemRepositorio.encontrarQuartosOcupadosNesteMomento(LocalDate.now());
        Assertions.assertEquals(3, quartosOcupados.size());

    }

    @Test
    public void adicionaUmServicoNaHospedagem() {
        HospedagemEntity hospedagem = new HospedagemEntity();
        em.persist(hospedagem);
        ServicoEntity servico = new ServicoEntity();
        em.persist(servico);
        HospedagemServicoEntity hs = new HospedagemServicoEntity(hospedagem, servico, LocalDate.now());

        hospedagem.incluirServico(hs);

        ServicoEntity result = hospedagem.getServicos().iterator().next().getServico();
        Assertions.assertEquals(servico, result);
    }

    @Test
    public void retornaHospedagensQueForamInciadas() {
        ClienteEntity cliente = new ClienteEntity();
        em.persist(cliente);
        HospedagemEntity iniciada = new HospedagemEntity(cliente, HospedagemStatusEnum.INICIADA, LocalDate.MIN, LocalDate.MAX);
        HospedagemEntity encerrada = new HospedagemEntity(cliente, HospedagemStatusEnum.ENCERRADA, LocalDate.MIN, LocalDate.MAX);
        em.persist(iniciada);
        em.persist(encerrada);

        List<HospedagemEntity> hospedagens = hospedagemRepositorio.todasHospedagensIniciadas(cliente, HospedagemStatusEnum.INICIADA);
        Assertions.assertEquals(1, hospedagens.size());
        HospedagemEntity result = hospedagens.iterator().next();
        Assertions.assertEquals(iniciada, result);
    }

}
