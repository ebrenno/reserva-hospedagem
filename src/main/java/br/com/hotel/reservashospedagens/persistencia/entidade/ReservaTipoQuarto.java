/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.persistencia.entidade;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Entity
@EqualsAndHashCode
@Getter
public class ReservaTipoQuarto {

    @EmbeddedId
    ReservaTipoQuartoId id;
    private int quantidade;

    public ReservaTipoQuarto() {
    }

    public ReservaTipoQuarto(ReservaTipoQuartoId id, int quantidade) {
        this.id = id;
        this.quantidade = quantidade;
    }

}
