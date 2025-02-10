/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.testes.unidade.model;

import br.com.hotel.reservashospedagens.model.pagamento.CobrancaModel;
import br.com.hotel.reservashospedagens.model.quarto.QuartoModel;
import br.com.hotel.reservashospedagens.model.reserva.ReservaModel;
import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoModel;
import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoNaoExisteException;
import br.com.hotel.reservashospedagens.persistencia.entidade.TipoQuartoEntity;
import br.com.hotel.reservashospedagens.persistencia.repositorio.ReservaRepositorio;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(classes = {ReservaModel.class})
public class ReservaModelTeste {

    @MockitoBean
    ReservaRepositorio reservaRespositorio;
    @MockitoBean
    QuartoModel quartoModel;
    @MockitoBean
    CobrancaModel cobrancaModel;
    @MockitoBean
    TipoQuartoModel tipoQuartoModel;

    @Autowired
    ReservaModel reserva;

    @Test
    public void ExisteVagaDisponivel() throws TipoQuartoNaoExisteException {
        LocalDate checkin = LocalDate.MIN;
        LocalDate checkout = LocalDate.MAX;
        ReservaModel reservaSpy = Mockito.spy(reserva);
        TipoQuartoEntity solteiro = Mockito.mock(TipoQuartoEntity.class);
        Mockito.doReturn(8).when(reservaSpy).reservadosEntre(solteiro, checkin, checkout);
        Mockito.when(quartoModel.contarPorTipo(solteiro)).thenReturn(9);

        boolean flag = reservaSpy.estaDisponivel(solteiro, checkin, checkout);
        Assertions.assertTrue(flag);
    }

}
