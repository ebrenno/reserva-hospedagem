/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.dto.cliente;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
public class UsuarioDTO {

    private String email;
    private String senha;

    public UsuarioDTO() {
    }

    public UsuarioDTO(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

}
