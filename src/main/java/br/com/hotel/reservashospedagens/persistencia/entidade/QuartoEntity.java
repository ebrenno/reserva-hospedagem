/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.persistencia.entidade;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
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
