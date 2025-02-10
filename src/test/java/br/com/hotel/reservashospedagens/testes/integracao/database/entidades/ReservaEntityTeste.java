package br.com.hotel.reservashospedagens.testes.integracao.database.entidades;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaEntity;

@DataJpaTest
public class ReservaEntityTeste {
    @Autowired
    TestEntityManager em;

    @Test
    public void minimoParaPersistirUmaReserva() {
        ReservaEntity reserva = new ReservaEntity();
        int IdGerado = em.persist(reserva).getId();
        Assertions.assertNotEquals(0, IdGerado);
    }
}
