/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.dto.reserva;

import java.time.LocalDate;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
public class ReservaDto {

    private LocalDate checkin;
    private LocalDate checkout;
    private List<VagaDto> vagas;

    public ReservaDto() {
    }

    public ReservaDto(LocalDate checkin, LocalDate checkout, List<VagaDto> vagas) {
        this.checkin = checkin;
        this.checkout = checkout;
        this.vagas = vagas;
    }

}
