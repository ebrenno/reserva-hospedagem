/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.persistencia.entidade;

import br.com.hotel.reservashospedagens.model.reserva.ReservaStatusEnum;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(exclude = "quartosReservados")
@Entity
@Table(name = "reserva")
public class ReservaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private int id;
    @JoinColumn(name = "cliente_id")
    @ManyToOne
    @Getter
    private ClienteEntity cliente;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "reserva_id")
    @Getter
    private Set<ReservaTipoQuarto> quartosReservados = new HashSet<>();

    @Getter
    private LocalDate checkin;

    @Getter
    private LocalDate checkout;
    @Getter
    @Setter
    private ReservaStatusEnum status;
    @Getter
    @Setter
    private double valor;

    public ReservaEntity() {
    }

    public ReservaEntity(ClienteEntity cliente, LocalDate checkin, LocalDate checkout) {
        this.cliente = cliente;
        this.checkin = checkin;
        this.checkout = checkout;
    }

    public void addTipoQuarto(ReservaTipoQuarto quarto) {
        this.quartosReservados.add(quarto);
    }

    @Version
    private int version;
}
