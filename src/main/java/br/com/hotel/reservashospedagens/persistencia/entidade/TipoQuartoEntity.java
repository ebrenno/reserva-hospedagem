/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.persistencia.entidade;

import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Entity
@Table(name = "tipo_quarto")
public class TipoQuartoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private int id;
    @Getter
    private String descricao;
    @Getter
    private TipoQuartoEnum tipo;
    @Getter
    private double preco;

    public TipoQuartoEntity() {
    }

    public TipoQuartoEntity(String descricao, TipoQuartoEnum tipo, double preco) {
        this.descricao = descricao;
        this.tipo = tipo;
        this.preco = preco;

    }
    @Version
    private int version;
}
