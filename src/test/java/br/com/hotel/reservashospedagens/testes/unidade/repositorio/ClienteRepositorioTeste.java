/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.testes.unidade.repositorio;

import br.com.hotel.reservashospedagens.persistencia.entidade.ClienteEntity;
import br.com.hotel.reservashospedagens.persistencia.repositorio.ClienteRepositorio;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class ClienteRepositorioTeste {

    @Autowired
    TestEntityManager em;
    @Autowired
    ClienteRepositorio clienteRepositorio;

    @Test
    public void DeveCadastrarUmCliente() {
        ClienteEntity c = new ClienteEntity("000", "joao", "m");
        clienteRepositorio.save(c);
        Assertions.assertTrue(c.getId() != 0);
    }

    @Test
    public void DeveEncontrarPorRg() {
        ClienteEntity c1 = new ClienteEntity("000", "joao", "m");
        ClienteEntity c2 = new ClienteEntity("001", "maria", "f");
        c1 = em.persist(c1);
        em.persist(c2);
        Optional<ClienteEntity> result = clienteRepositorio.findByRg("000");
        Assertions.assertTrue(result.isPresent());
        Assertions.assertTrue(result.get() == c1, "retornou: " + result.get());
    }

    @Test
    public void ClienteNaoEncontrado() {
        ClienteEntity c1 = new ClienteEntity("000", "joao", "m");
        ClienteEntity c2 = new ClienteEntity("001", "maria", "f");
        em.persist(c1);
        em.persist(c2);
        Optional<ClienteEntity> result = clienteRepositorio.findByRg("002");
        Assertions.assertFalse(result.isPresent());

    }

}
