/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.dto.cliente;

import br.com.hotel.reservashospedagens.persistencia.entidade.ClienteEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
public class ClienteDTO {

    private int id;
    private String nome;
    private String rg;
    private String sexo;

    public ClienteDTO() {

    }

    private ClienteDTO(int id, String nome, String rg, String sexo) {
        this.id = id;
        this.nome = nome;
        this.rg = rg;
        this.sexo = sexo;
    }

    public ClienteDTO(String nome, String rg, String sexo) {
        this.nome = nome;
        this.rg = rg;
        this.sexo = sexo;
    }

    public static ClienteDTO toDTO(ClienteEntity entity) {
        return new ClienteDTO(entity.getId(), entity.getNome(), entity.getRg(), entity.getSexo());

    }
}
