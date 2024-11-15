/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.persistencia.entidade;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Entity
@Table(name = "hospedagem_servico")
public class HospedagemServicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private int id;
    @JoinColumn(name = "hospedagem_id")
    @ManyToOne
    @Getter
    private HospedagemEntity hospedagem;
    @JoinColumn(name = "servico_id")
    @OneToOne
    @Getter
    private ServicoEntity servico;
    @Getter
    private LocalDate data;

    public HospedagemServicoEntity() {
    }

    public HospedagemServicoEntity(HospedagemEntity hospedagem, ServicoEntity servico, LocalDate data) {
        this.hospedagem = hospedagem;
        this.servico = servico;
        this.data = data;
    }
    @Version
    private int version;
}
