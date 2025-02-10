/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.persistencia.entidade;

import java.io.Serializable;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Embeddable
@EqualsAndHashCode
@Getter
public class ReservaTipoQuartoId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "reserva_id")
    private ReservaEntity reservaId;
    
    @ManyToOne
    @JoinColumn(name = "tipo_quarto_id")
    private TipoQuartoEntity tipoQuartoId;

    public ReservaTipoQuartoId() {
    }

    public ReservaTipoQuartoId(ReservaEntity reservaId, TipoQuartoEntity tipoQuartoId) {
        this.reservaId = reservaId;
        this.tipoQuartoId = tipoQuartoId;
    }

}
