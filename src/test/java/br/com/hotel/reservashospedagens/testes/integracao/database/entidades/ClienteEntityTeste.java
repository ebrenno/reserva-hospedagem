package br.com.hotel.reservashospedagens.testes.integracao.database.entidades;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import br.com.hotel.reservashospedagens.persistencia.entidade.ClienteEntity;

@DataJpaTest
public class ClienteEntityTeste {
    @Autowired
    TestEntityManager em;

    @Test
    public void minimoParaPersistirUmCliente() {
        ClienteEntity cliente = new ClienteEntity();
        int idGerado = em.persist(cliente).getId();
        Assertions.assertNotEquals(0,idGerado);
    }
    @Test
    public void buscarUmCliente() {
        ClienteEntity cliente = new ClienteEntity();
        em.persist(cliente);
        Assertions.assertNotNull(em.find(ClienteEntity.class, cliente.getId()));
    }
    

}
