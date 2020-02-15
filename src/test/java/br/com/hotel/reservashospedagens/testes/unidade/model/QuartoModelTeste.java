/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.testes.unidade.model;

import br.com.hotel.reservashospedagens.model.quarto.QuartoModel;
import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoEnum;
import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoModel;
import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoNaoExisteException;
import br.com.hotel.reservashospedagens.persistencia.entidade.TipoQuartoEntity;
import br.com.hotel.reservashospedagens.persistencia.repositorio.QuartoRepositorio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {QuartoModel.class})

public class QuartoModelTeste {

    @Autowired
    QuartoModel quarto;
    @MockBean
    QuartoRepositorio quartoRepositorio;
    @MockBean
    TipoQuartoModel tipoQuartoModel;

    @Test
    public void ContagemDeQuartosComSucesso() throws TipoQuartoNaoExisteException {
        TipoQuartoEntity entidade = Mockito.mock(TipoQuartoEntity.class);
        Mockito.when(tipoQuartoModel.getEntity(TipoQuartoEnum.SOLTEIRO)).thenReturn(entidade);
        Mockito.when(quartoRepositorio.countAllByTipo(entidade)).thenReturn(6);

        int quantidade = quarto.contarPorTipo(entidade);
        Assertions.assertEquals(6, quantidade);
    }

}
