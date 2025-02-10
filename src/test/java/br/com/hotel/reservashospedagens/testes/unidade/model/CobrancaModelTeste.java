/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.testes.unidade.model;

import br.com.hotel.reservashospedagens.model.pagamento.CobrancaModel;
import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoEnum;
import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoModel;
import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoNaoExisteException;
import br.com.hotel.reservashospedagens.persistencia.entidade.HospedagemEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.HospedagemServicoEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaTipoQuarto;
import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaTipoQuartoId;
import br.com.hotel.reservashospedagens.persistencia.entidade.ServicoEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.TipoQuartoEntity;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(classes = CobrancaModel.class)
public class CobrancaModelTeste {

    @Autowired
    CobrancaModel cobrancaModel;
    @MockitoBean
    TipoQuartoModel tipoQuartoModel;

    @Test
    public void RetornaPrecoDaReservaSobreDiasReservados() {
        LocalDate checkin = LocalDate.parse("2020-01-01");
        LocalDate checkout = LocalDate.parse("2020-01-08");
        double valor = cobrancaModel.daReserva(checkin, checkout);
        Assertions.assertTrue(valor == 210, "retornou: " + valor);
    }

    @Test
    public void RetornaValorFinalDosQuartosHospedados() throws TipoQuartoNaoExisteException {
        int quantidadeReservaSolteiro = 3;
        int quantidadeReservaCasal = 1;
        ReservaEntity reservaEntity = Mockito.mock(ReservaEntity.class);
        TipoQuartoEntity tipoSolteiro = new TipoQuartoEntity("", TipoQuartoEnum.SOLTEIRO, 100d);
        TipoQuartoEntity tipoCasal = new TipoQuartoEntity("", TipoQuartoEnum.CASAL, 300d);
        ReservaTipoQuarto quartoSolteiro = new ReservaTipoQuarto(new ReservaTipoQuartoId(reservaEntity, tipoSolteiro), quantidadeReservaSolteiro);
        ReservaTipoQuarto quartoCasal = new ReservaTipoQuarto(new ReservaTipoQuartoId(reservaEntity, tipoCasal), quantidadeReservaCasal);

        Set<ReservaTipoQuarto> quartos = new HashSet<>();
        quartos.add(quartoSolteiro);
        quartos.add(quartoCasal);

        int dias = 3;
        LocalDate checkin = LocalDate.now();
        LocalDate checkout = checkin.plusDays(dias);

        //condição interna
        Mockito.when(tipoQuartoModel.encontrarPorId(1)).thenReturn(tipoSolteiro);
        Mockito.when(tipoQuartoModel.encontrarPorId(2)).thenReturn(tipoCasal);
        //quando
        double result = cobrancaModel.todosOsQuartosReservados(quartos, checkin, checkout);
        //expect
        double quarto1 = tipoSolteiro.getPreco() * dias * quantidadeReservaSolteiro;
        double quarto2 = tipoCasal.getPreco() * dias * quantidadeReservaCasal;
        double soma = quarto1 + quarto2;

        Assertions.assertTrue(result == soma, "retornou: " + result);

    }

    @Test
    public void RetornaValorFinalDosServicosConsumidos() {
        double valor1 = 15.99;
        double valor2 = 8.99;
        double valor3 = 21.59;

        ServicoEntity servico1 = Mockito.mock(ServicoEntity.class);
        ServicoEntity servico2 = Mockito.mock(ServicoEntity.class);
        ServicoEntity servico3 = Mockito.mock(ServicoEntity.class);

        Mockito.when(servico1.getValor()).thenReturn(valor1);
        Mockito.when(servico2.getValor()).thenReturn(valor2);
        Mockito.when(servico3.getValor()).thenReturn(valor3);

        HospedagemServicoEntity h1 = new HospedagemServicoEntity(Mockito.mock(HospedagemEntity.class), servico1, LocalDate.MIN);
        HospedagemServicoEntity h2 = new HospedagemServicoEntity(Mockito.mock(HospedagemEntity.class), servico2, LocalDate.MIN);
        HospedagemServicoEntity h3 = new HospedagemServicoEntity(Mockito.mock(HospedagemEntity.class), servico3, LocalDate.MIN);

        List<HospedagemServicoEntity> servicos = Arrays.asList(h1, h2, h3);

        double expect = valor1 + valor2 + valor3;
        double total = cobrancaModel.dosServicos(servicos);
        Assertions.assertEquals(total, expect, "retornou: " + total);
    }

}
