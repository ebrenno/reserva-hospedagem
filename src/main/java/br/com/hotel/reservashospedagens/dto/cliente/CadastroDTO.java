/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.dto.cliente;

import br.com.hotel.reservashospedagens.persistencia.entidade.ClienteEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.UsuarioEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
public class CadastroDTO {

    private UsuarioDTO usuario;
    private ClienteDTO cliente;

    public CadastroDTO() {
    }

    public CadastroDTO(UsuarioDTO usuario, ClienteDTO cliente) {
        this.usuario = usuario;
        this.cliente = cliente;
    }

    public UsuarioEntity getUsuarioEntity() {
        ClienteEntity clienteEntity = new ClienteEntity(this.cliente.getRg(), this.cliente.getNome(), this.cliente.getSexo());
        return new UsuarioEntity(usuario.getEmail(), usuario.getSenha(), clienteEntity);
    }
}
