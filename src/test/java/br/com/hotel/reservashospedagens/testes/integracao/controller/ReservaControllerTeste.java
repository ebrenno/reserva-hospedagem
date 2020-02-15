/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.testes.integracao.controller;

import br.com.hotel.reservashospedagens.dto.reserva.ReservaDto;
import br.com.hotel.reservashospedagens.dto.reserva.VagaDto;
import br.com.hotel.reservashospedagens.model.cliente.Cliente;
import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoEnum;
import br.com.hotel.reservashospedagens.persistencia.entidade.ClienteEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.QuartoEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.TipoQuartoEntity;
import br.com.hotel.reservashospedagens.persistencia.repositorio.ReservaRepositorio;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@AutoConfigureTestDatabase
public class ReservaControllerTeste {

    Log log = LogFactory.getLog(ReservaControllerTeste.class);
    @Autowired
    MockMvc mvc;
    @Autowired
    TestEntityManager em;
    @Autowired
    ObjectMapper om;
    @Autowired
    ReservaRepositorio reservaRepositorio;
    @Autowired
    Cliente clienteModel;

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
    public void Retorna10VagasDisponiveis() throws Exception {
        LocalDate checkin = LocalDate.parse("2020-01-05");
        LocalDate checkout = LocalDate.parse("2020-05-08");
        String uri = "/hotel/reserva/vagas-disponiveis-entre-" + checkin + "-e-" + checkout;
        String result = mvc.perform(get(uri)).andReturn().getResponse().getContentAsString();
        Collection<VagaDto> vagas = om.readValue(result, new TypeReference< List<VagaDto>>() {
        });

        int expect = vagas.stream().mapToInt(vaga -> vaga.getQuantidade()).reduce(0, (soma, valor) -> soma + valor);
        Assertions.assertEquals(10, expect);

    }

    @Test
    public void marcaUmaReservaComSucesso() throws Exception {
        LocalDate checkin = LocalDate.parse("2020-01-05");
        LocalDate checkout = LocalDate.parse("2020-05-08");
        ClienteEntity cliente = new ClienteEntity();
        em.persist(cliente);

        VagaDto vagaSolteiro = new VagaDto(TipoQuartoEnum.SOLTEIRO, 2, 0);
        VagaDto vagaCasal = new VagaDto(TipoQuartoEnum.CASAL, 1, 0);
        List<VagaDto> vagas = Arrays.asList(vagaSolteiro, vagaCasal);
        ReservaDto reservaDto = new ReservaDto(checkin, checkout, vagas);
        String content = om.writeValueAsString(reservaDto);
        int clienteId = cliente.getId();
        String aplicar = "/hotel/reserva/aplicar/" + clienteId;

        //quando
        mvc.perform(post(aplicar).contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isOk());

        Collection<ReservaEntity> reservas = cliente.getReservas();
        //então
        int reservados = reservas.stream()
                .flatMap(reserva -> reserva.getQuartosReservados().stream())
                .map(quarto -> quarto.getQuantidade()).reduce(0, (soma, valor) -> soma + valor);
        Assertions.assertEquals(3, reservados);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void Deve_CriarCincoReservas_Quando_VariasRequisicoesAoMesmoTempo() throws Exception {
        LocalDate checkin = LocalDate.parse("2020-01-01");
        LocalDate checkout = LocalDate.parse("2020-01-05");
        VagaDto vagaSolteiro = new VagaDto(TipoQuartoEnum.SOLTEIRO, 1, 0);
        ReservaDto ultimaVaga = new ReservaDto(checkin, checkout, Arrays.asList(vagaSolteiro));
        String body = om.writeValueAsString(ultimaVaga);

        //cria vários clientes que concorrerão poucas vagas
        int totalRequisicao = 10;
        int[] ids = new int[totalRequisicao];
        for (int i = 0; i < totalRequisicao; i++) {
            ClienteEntity clienteConcorrente = new ClienteEntity(Integer.toString(i), "", "");
            ids[i] = em.persist(clienteConcorrente).getId();

        }
        //necessario para que o serviço encontre os clientes dentro da requisição
        //o estado do banco de dados precisará ser redefinido após este método
        em.getEntityManager().getTransaction().commit();

        int count = 0;
        Thread[] threads = new Thread[totalRequisicao];
        final String reservaURI = "/hotel/reserva/aplicar/";
        for (Integer id : ids) {
            threads[count++] = new Thread(() -> {
                try {
                    mvc.perform(post(reservaURI + id).contentType(MediaType.APPLICATION_JSON).content(body));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

        }
        //inicia todas as threads
        //faz a thread principal esperar todas as requisições terminarem
        for (Thread t : threads) {
            t.start();
            t.join();
        }

        Long reservasFeitas = reservaRepositorio.count();
        Assertions.assertEquals(5, reservasFeitas.intValue());
    }

}
