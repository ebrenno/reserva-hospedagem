/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.persistencia.entidade;

import br.com.hotel.reservashospedagens.model.hospedagem.HospedagemStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.EqualsAndHashCode;

import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(exclude = {"quartos", "servicos"})
@Entity
@Table(name = "hospedagem")
public class HospedagemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private int id;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @Getter
    @JsonIgnore
    private ClienteEntity cliente;
    @Getter
    @Setter
    private HospedagemStatusEnum status;
    @ManyToMany
    @JoinTable(name = "hospedagem_quarto", joinColumns = {
        @JoinColumn(name = "hospedagem_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "quarto_id")})
    @Getter
    private List<QuartoEntity> quartos = new ArrayList<>();
    @JoinColumn(name = "conta_id")
    @Getter
    private LocalDate checkin;
    @Getter
    @Setter
    private LocalDate checkout;
    @Getter
    @Setter
    private double valor;
    @OneToMany(mappedBy = "hospedagem", cascade = CascadeType.ALL)
    @Getter
    @Setter
    private List<HospedagemServicoEntity> servicos = new ArrayList<>();

    public HospedagemEntity() {
    }

    public HospedagemEntity(ClienteEntity cliente, HospedagemStatusEnum status,LocalDate checkin, LocalDate checkout) {
        this.cliente = cliente;
        this.status = status;
        this.checkin = checkin;
        this.checkout = checkout;
    }
    @Version
    private int version;

    public void incluirQuarto(QuartoEntity quartoEntity) {
        quartos.add(quartoEntity);
    }

    public void incluirQuartos(Collection<QuartoEntity> quartos) {
        this.quartos.addAll(quartos);
    }

    public void incluirServico(HospedagemServicoEntity hs) {
        this.servicos.add(hs);
    }
}
