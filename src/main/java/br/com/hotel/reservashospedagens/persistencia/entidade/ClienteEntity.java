/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.persistencia.entidade;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(exclude = {"reservas", "hospedagens"})
@Entity
@Table(name = "cliente")
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private int id;
    @Column(unique = true)
    @Getter
    private String rg;
    @Getter
    private String nome;
    @Column(length = 1)
    @Getter
    private String sexo;
    @OneToMany(mappedBy = "cliente")
    @Getter
    private Set<ReservaEntity> reservas = new HashSet<>();
    @OneToMany(mappedBy = "cliente")
    @Getter
    private Set<HospedagemEntity> hospedagens = new HashSet<>();

    public ClienteEntity() {
    }

    public ClienteEntity(String rg, String nome, String sexo) {
        this.rg = rg;
        this.nome = nome;
        this.sexo = sexo;
    }
    @Version
    private int version;

    public void incluirReserva(ReservaEntity reserva) {
        reservas.add(reserva);
    }

    public void incluirHospedagem(HospedagemEntity hospedagemEntity) {
        this.hospedagens.add(hospedagemEntity);
    }
}
