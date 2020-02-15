/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.persistencia.entidade;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
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
