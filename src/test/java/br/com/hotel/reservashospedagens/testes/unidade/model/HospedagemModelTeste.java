/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.testes.unidade.model;

import br.com.hotel.reservashospedagens.model.pagamento.CobrancaModel;
import br.com.hotel.reservashospedagens.model.hospedagem.HospedagemModel;
import br.com.hotel.reservashospedagens.model.quarto.QuartoModel;
import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoModel;
import br.com.hotel.reservashospedagens.persistencia.repositorio.HospedagemRepositorio;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(classes = HospedagemModel.class)
public class HospedagemModelTeste {

    @Autowired
    HospedagemModel hospedagemModel;
    @MockitoBean
    HospedagemRepositorio hospedagemRepositorio;
    @MockitoBean
    TipoQuartoModel tipoQuartoModel;
    @MockitoBean
    QuartoModel quartoModel;
    @MockitoBean
    CobrancaModel cobrancaModel;

    @Test
    public void ComparaDatasCorretamente() {
        boolean equalsCheckin = hospedagemModel.dentroDoPrazo(LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-05"));
        boolean betweenCheckinAndCheckout = hospedagemModel.dentroDoPrazo(LocalDate.parse("2020-01-03"), LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-05"));
        boolean equalsCheckout = hospedagemModel.dentroDoPrazo(LocalDate.parse("2020-01-05"), LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-05"));
        boolean beforeCheckin = hospedagemModel.dentroDoPrazo(LocalDate.parse("2019-12-31"), LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-05"));
        boolean afterCheckout = hospedagemModel.dentroDoPrazo(LocalDate.parse("2020-01-06"), LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-05"));
        Assertions.assertTrue(equalsCheckin);
        Assertions.assertTrue(betweenCheckinAndCheckout);
        Assertions.assertTrue(equalsCheckout);
        Assertions.assertFalse(beforeCheckin);
        Assertions.assertFalse(afterCheckout);

    }

}
