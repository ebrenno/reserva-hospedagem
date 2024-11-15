/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.testes.unidade.model;

import br.com.hotel.reservashospedagens.model.cliente.UsuarioModel;
import br.com.hotel.reservashospedagens.persistencia.repositorio.UsuarioRepositorio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(classes = UsuarioModel.class)
public class UsuarioModelTeste {

    @Autowired
    UsuarioModel usuarioModel;
    @MockBean
    UsuarioRepositorio usuarioRepositorio;

    @Test
    public void EncriptarSenha() {
        String hash = usuarioModel.encriptar("senha");
        boolean result = usuarioModel.validarSenha("senha", hash);
        Assertions.assertTrue(result);
    }
}
