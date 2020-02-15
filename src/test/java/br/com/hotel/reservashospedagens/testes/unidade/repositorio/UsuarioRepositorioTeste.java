/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.testes.unidade.repositorio;

import br.com.hotel.reservashospedagens.persistencia.entidade.ClienteEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.UsuarioEntity;
import br.com.hotel.reservashospedagens.persistencia.repositorio.UsuarioRepositorio;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class UsuarioRepositorioTeste {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;
    @Autowired
    TestEntityManager em;

    @Test
    public void DeveEncontrarUsuarioPorEmail() {
        ClienteEntity clienteEntity = new ClienteEntity("000", "joao", "m");
        clienteEntity = em.persist(clienteEntity);
        UsuarioEntity usuario = new UsuarioEntity("joao@email.com", "123", clienteEntity);
        usuario = em.persist(usuario);

        Optional<UsuarioEntity> container = usuarioRepositorio.findByEmail("joao@email.com");
        Assertions.assertTrue(container.isPresent());
        Assertions.assertTrue(container.get() == usuario);
    }

    @Test
    public void NaoDeveEncontrarUsuarioPorEmail() {
        ClienteEntity clienteEntity = new ClienteEntity("000", "joao", "m");
        clienteEntity = em.persist(clienteEntity);
        UsuarioEntity usuario = new UsuarioEntity("joao@email.com", "123", clienteEntity);
        em.persist(usuario);

        Optional<UsuarioEntity> testeEmail = usuarioRepositorio.findByEmail("maria@email.com");

        Assertions.assertFalse(testeEmail.isPresent());

    }

    @Test
    public void DevePersistirClienteEmModoCascade() {
        ClienteEntity clienteEntity = new ClienteEntity("000", "joao", "m");
        //antes de persistir usuario
        Assertions.assertTrue(clienteEntity.getId() == 0);

        UsuarioEntity usuario = new UsuarioEntity("joao@email.com", "123", clienteEntity);
        em.persist(usuario);
        //depois de persistir usuario
        Assertions.assertTrue(clienteEntity.getId() != 0);

    }
}
