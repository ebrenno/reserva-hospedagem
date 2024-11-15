/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.persistencia.entidade;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Entity
@Table(name = "quarto")
public class QuartoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private int id;
    @JoinColumn(name = "tipo_id", nullable = false)
    @ManyToOne(cascade = CascadeType.ALL)
    @Getter
    private TipoQuartoEntity tipo;
    @Getter
    private int numero;
    @Getter
    private int andar;

    public QuartoEntity() {
    }

    public QuartoEntity(TipoQuartoEntity tipo, int numero, int andar) {
        this.tipo = tipo;
        this.numero = numero;
        this.andar = andar;
    }
    @Version
    private int version;
}
