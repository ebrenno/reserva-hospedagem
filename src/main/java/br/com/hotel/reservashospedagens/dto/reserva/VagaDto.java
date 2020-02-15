/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.dto.reserva;

import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
public class VagaDto {

    private TipoQuartoEnum tipoQuarto;
    private int quantidade;
    private double preco;

    public VagaDto() {
    }

    public VagaDto(TipoQuartoEnum tipoQuarto, int quantidade, double preco) {
        this.tipoQuarto = tipoQuarto;
        this.quantidade = quantidade;
        this.preco = preco;
    }

}
