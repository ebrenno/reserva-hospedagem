/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.testes.unidade.controller;

import br.com.hotel.reservashospedagens.controller.HospedagemController;
import br.com.hotel.reservashospedagens.exception.DataForaDoPrazoException;
import br.com.hotel.reservashospedagens.exception.HospedagemNaoExisteException;
import br.com.hotel.reservashospedagens.exception.ReservaNaoExisteException;
import br.com.hotel.reservashospedagens.model.cliente.Cliente;
import br.com.hotel.reservashospedagens.model.pagamento.CobrancaModel;
import br.com.hotel.reservashospedagens.model.hospedagem.HospedagemModel;
import br.com.hotel.reservashospedagens.model.quarto.QuartoModel;
import br.com.hotel.reservashospedagens.model.reserva.ReservaModel;
import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.ServicoEntity;
import br.com.hotel.reservashospedagens.persistencia.repositorio.ServicoRepositorio;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HospedagemController.class)
public class HospedagemControllerTeste {

    @Autowired
    MockMvc mvc;
    @MockBean
    Cliente clienteModel;
    @MockBean
    HospedagemModel hospedagemModel;
    @MockBean
    ReservaModel reservaModel;
    @MockBean
    CobrancaModel cobrancaModel;
    @MockBean
    QuartoModel quartoModel;
    @MockBean
    ServicoRepositorio servicoRepositorio;

    @Test
    public void Throws_ReservaNaoExisteException() throws Exception {
        int idNaoExistente = 2000;
        LocalDate checkout = LocalDate.now();
        Mockito.when(reservaModel.encontrarPorId(idNaoExistente)).thenThrow(ReservaNaoExisteException.class);
        mvc.perform(post("/hotel/hospedagem/checkin/" + idNaoExistente + "/checkout-date/" + checkout)).andExpect(status().isNotFound());
    }

    @Test
    public void Throws_hospedagemNaoExisteException() throws Exception {
        LocalDate checkout = LocalDate.now();
        ReservaEntity reservaEntity = Mockito.mock(ReservaEntity.class);
        Mockito.when(reservaModel.encontrarPorId(1)).thenReturn(reservaEntity);

        Mockito.doThrow(DataForaDoPrazoException.class).when(hospedagemModel).checkin(reservaEntity, checkout);
        mvc.perform(post("/hotel/hospedagem/checkin/1/checkout-date/" + checkout)).andExpect(status().isConflict());
    }

    @Test
    public void expect_isOk() throws Exception {

        mvc.perform(put("/hotel/hospedagem/checkout/1")).andExpect(status().isOk());
    }

    @Test
    public void ExpectException() throws Exception {

        Mockito.doThrow(HospedagemNaoExisteException.class).when(hospedagemModel).checkout(Mockito.any());

        mvc.perform(put("/hotel/hospedagem/checkout/1")).andExpect(status().isNotFound());

    }

    @Test
    public void incluiServicoComSucesso() throws Exception {

        ServicoEntity servicoEntity = Mockito.mock(ServicoEntity.class);
        Mockito.when(servicoRepositorio.findById(1)).thenReturn(Optional.of(servicoEntity));

        mvc.perform(post("/hotel/hospedagem/servico/1/quarto/1")).andExpect(status().isOk());
    }

}
