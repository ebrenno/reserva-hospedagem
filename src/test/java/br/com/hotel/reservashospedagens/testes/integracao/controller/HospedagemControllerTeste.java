/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.testes.integracao.controller;

import br.com.hotel.reservashospedagens.dto.reserva.ReservaDto;
import br.com.hotel.reservashospedagens.dto.reserva.VagaDto;
import br.com.hotel.reservashospedagens.model.hospedagem.HospedagemModel;
import br.com.hotel.reservashospedagens.model.reserva.ReservaModel;
import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoEnum;
import br.com.hotel.reservashospedagens.persistencia.entidade.ClienteEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.HospedagemEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.QuartoEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.ServicoEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.TipoQuartoEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@AutoConfigureTestDatabase
public class HospedagemControllerTeste {

    Log log = LogFactory.getLog(ReservaControllerTeste.class);
    @Autowired
    MockMvc mvc;
    @Autowired
    TestEntityManager em;
    @Autowired
    ReservaModel reservaModel;
    @Autowired
    HospedagemModel hospedagemModel;
    @Autowired
    ObjectMapper om;

    @BeforeEach
    public void before() {
        //existem 5 quartos de solteiro previamente cadastrados
        //existem 5 quartos de casal previamente cadastrados
        log.info("metodo before iniciado");
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
    public void HospedaUmCliente() throws Exception {
        LocalDate checkin = LocalDate.MIN;
        LocalDate checkout = LocalDate.MAX;
        ClienteEntity cliente = new ClienteEntity();
        em.persist(cliente);
        Collection<VagaDto> vagas = Arrays.asList(new VagaDto(TipoQuartoEnum.SOLTEIRO, 1, 0));

        reservaModel.iniciar(cliente, vagas, checkin, checkout);

        ReservaEntity reserva = cliente.getReservas().iterator().next();

        String postURI = "/hotel/hospedagem/checkin/" + reserva.getId() + "/checkout-date/" + checkout;

        mvc.perform(post(postURI)).andExpect(status().isOk());

        HospedagemEntity result = cliente.getHospedagens().iterator().next();
        //cliente associado a hospedagem
        Assertions.assertEquals(cliente, result.getCliente());
        //quarto reservado associado a hospedagem
        Assertions.assertEquals(1, result.getQuartos().size());
    }

    @Test
    public void SolicitaUmServicoDeQuarto() throws Exception {
        LocalDate checkin = LocalDate.now().minusDays(5);
        LocalDate checkout = LocalDate.now().plusDays(5);
        ClienteEntity cliente = new ClienteEntity();
        em.persist(cliente);
        Collection<VagaDto> vagas = Arrays.asList(new VagaDto(TipoQuartoEnum.SOLTEIRO, 1, 0));

        //aplica a reserva
        reservaModel.iniciar(cliente, vagas, checkin, checkout);

        ReservaEntity reserva = cliente.getReservas().iterator().next();
        String hospedagemURI = "/hotel/hospedagem/checkin/" + reserva.getId() + "/checkout-date/" + checkout;

        //aplica a hospedagem
        mvc.perform(post(hospedagemURI)).andExpect(status().isOk());

        HospedagemEntity hospedagem = cliente.getHospedagens().iterator().next();

        ServicoEntity servico = new ServicoEntity();
        em.persist(servico);
        int quartoId = hospedagem.getQuartos().iterator().next().getId();
        String servicoURI = "/hotel/hospedagem/servico/" + servico.getId() + "/quarto/" + quartoId;

        //solicita o servico
        mvc.perform(post(servicoURI)).andExpect(status().isOk());

        ServicoEntity result = hospedagem.getServicos().iterator().next().getServico();
        Assertions.assertEquals(servico, result);
    }

    @Test
    public void RealizaCheckout() throws Exception {
        LocalDate checkin = LocalDate.now().minusDays(5);
        LocalDate checkout = LocalDate.now().plusDays(5);
        ClienteEntity cliente = new ClienteEntity();
        em.persist(cliente);
        ReservaDto reservaDto = new ReservaDto(checkin, checkout, Arrays.asList(new VagaDto(TipoQuartoEnum.SOLTEIRO, 1, 0)));
        String body = om.writeValueAsString(reservaDto);
        String reservaURI = "/hotel/reserva/aplicar/" + cliente.getId();

        //aplica a reserva
        mvc.perform(post(reservaURI).contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk());

        ReservaEntity reserva = cliente.getReservas().iterator().next();
        String hospedagemURI = "/hotel/hospedagem/checkin/" + reserva.getId() + "/checkout-date/" + reserva.getCheckout();

        //aplica a hospedagem
        mvc.perform(post(hospedagemURI)).andExpect(status().isOk());

        int hospedagemId = cliente.getHospedagens().iterator().next().getId();

        //aplica o checkout
        String checkoutURI = "/hotel/hospedagem/checkout/" + hospedagemId;
        mvc.perform(put(checkoutURI)).andExpect(status().isOk());

        LocalDate result = cliente.getHospedagens().iterator().next().getCheckout();
        Assertions.assertEquals(LocalDate.now(), result);
    }

    @Test
    public void retornaHospedagensEmVigor() throws Exception {
        LocalDate checkin = LocalDate.now().minusDays(5);
        LocalDate checkout = LocalDate.now().plusDays(5);
        ClienteEntity cliente = new ClienteEntity();
        em.persist(cliente);
        ReservaDto reservaDto = new ReservaDto(checkin, checkout, Arrays.asList(new VagaDto(TipoQuartoEnum.SOLTEIRO, 1, 0)));
        String body = om.writeValueAsString(reservaDto);
        String reservaURI = "/hotel/reserva/aplicar/" + cliente.getId();

        //aplica a reserva
        mvc.perform(post(reservaURI).contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk());

        ReservaEntity reserva = cliente.getReservas().iterator().next();
        String hospedagemURI = "/hotel/hospedagem/checkin/" + reserva.getId() + "/checkout-date/" + reserva.getCheckout();

        //aplica a hospedagem
        mvc.perform(post(hospedagemURI)).andExpect(status().isOk());

        int clienteId = cliente.getId();
        String hospedagensURI = "/hotel/hospedagem/iniciadas/" + clienteId;
        String response = mvc.perform(get(hospedagensURI).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();

        Collection<HospedagemEntity> hospedagens = om.readValue(response, new TypeReference<Collection<HospedagemEntity>>() {
        });

        int expect = cliente.getHospedagens().iterator().next().getId();
        int result = hospedagens.iterator().next().getId();
        Assertions.assertEquals(expect, result);

    }

}
