/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.testes.unidade.controller;

import br.com.hotel.reservashospedagens.SpringSecurityConfig;
import br.com.hotel.reservashospedagens.controller.ReservaController;
import br.com.hotel.reservashospedagens.dto.reserva.ReservaDto;
import br.com.hotel.reservashospedagens.exception.ClienteNaoEncontradoException;
import br.com.hotel.reservashospedagens.model.cliente.Cliente;
import br.com.hotel.reservashospedagens.model.pagamento.CobrancaModel;
import br.com.hotel.reservashospedagens.model.reserva.ReservaModel;
import br.com.hotel.reservashospedagens.persistencia.entidade.ClienteEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservaController.class)
@Import(SpringSecurityConfig.class)

public class ReservaControllerTeste {

    @MockBean
    ReservaModel reservaModel;
    @MockBean
    Cliente clienteModel;
    @MockBean
    CobrancaModel cobrancaModel;
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper om;

    @Test
    public void RequisicaoDeVagasExecutadaNormalmente() throws Exception {
        LocalDate checkin = LocalDate.MIN;
        LocalDate checkout = LocalDate.MAX;
        mvc.perform(get("/hotel/reserva/vagas-disponiveis-entre-" + checkin + "-e-" + checkout)).andExpect(status().isOk());
    }

    @Test
    public void EfetuarReservaComSucesso() throws ClienteNaoEncontradoException, Exception {
        ClienteEntity clienteEntity = Mockito.mock(ClienteEntity.class);
        ReservaDto reservaDto = new ReservaDto();
        String content = om.writeValueAsString(reservaDto);

        ObjectMapper omSpy = Mockito.spy(om);
        Mockito.when(omSpy.readValue(content, ReservaDto.class)).thenReturn(reservaDto);
        Mockito.when(clienteModel.getEntityPorId(1)).thenReturn(clienteEntity);

        mvc.perform(post("/hotel/reserva/aplicar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());
    }

    @Test
    public void Throws_Erro404_Quando_ClienteNaoEncontrado() throws JsonProcessingException, Exception {
        ReservaDto reservaDto = new ReservaDto();
        String content = om.writeValueAsString(reservaDto);
        ObjectMapper omSpy = Mockito.spy(om);
        Mockito.when(omSpy.readValue(content, ReservaDto.class)).thenReturn(reservaDto);
        Mockito.when(clienteModel.getEntityPorId(1)).thenThrow(ClienteNaoEncontradoException.class);
        mvc.perform(post("/hote/reserva/aplicar/1").content(MediaType.APPLICATION_JSON_VALUE).content(content)).andExpect(status().isNotFound());
    }
        
}
