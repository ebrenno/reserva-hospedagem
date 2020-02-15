/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.testes.integracao.model;

import br.com.hotel.reservashospedagens.exception.UsuarioNaoEncontradoException;
import br.com.hotel.reservashospedagens.model.cliente.UsuarioModel;
import br.com.hotel.reservashospedagens.persistencia.entidade.ClienteEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.UsuarioEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase
public class UsuarioModelTeste {

    @Autowired
    UsuarioModel usuarioModel;

    @Test
    public void BuscaDeUsuarioBemSucedida() throws UsuarioNaoEncontradoException {
        String email = "email@email.com";
        String senha = "123";
        ClienteEntity cliente = new ClienteEntity();
        UsuarioEntity usuario = new UsuarioEntity(email, senha, cliente);
        usuarioModel.cadastrar(usuario);
        UsuarioEntity result = usuarioModel.encontrarPorEmailESenha(email, senha);
        Assertions.assertEquals(usuario, result);

    }
}
