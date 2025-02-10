package br.com.hotel.reservashospedagens.testes.integracao.database.entidades;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoEnum;
import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaTipoQuarto;
import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaTipoQuartoId;
import br.com.hotel.reservashospedagens.persistencia.entidade.TipoQuartoEntity;

@DataJpaTest
public class ReservaTipoQuartoTeste {
    @Autowired
    TestEntityManager em;

    @Test
    public void persistirUmaReservaDeTipoDeQuarto(){
        ReservaEntity reserva = new ReservaEntity();
        em.persist(reserva);
        TipoQuartoEntity solteiro = new TipoQuartoEntity("Solteiro", TipoQuartoEnum.SOLTEIRO, 0);
        em.persist(solteiro);
        ReservaTipoQuartoId id = new ReservaTipoQuartoId(reserva, solteiro);
        ReservaTipoQuarto reservaTipoQuarto = new ReservaTipoQuarto(id,1);
        
        ReservaTipoQuarto result = em.persistFlushFind(reservaTipoQuarto);
        
        Assertions.assertEquals(reservaTipoQuarto, result);

    }
    @Test
    public void persistirUmaReservaComDoisTiposDeQuarto(){
        ReservaEntity reserva = new ReservaEntity();
        em.persist(reserva);
        TipoQuartoEntity solteiro = new TipoQuartoEntity("Solteiro", TipoQuartoEnum.SOLTEIRO, 0);
        em.persist(solteiro);
        TipoQuartoEntity casal = new TipoQuartoEntity("Casal", TipoQuartoEnum.CASAL, 0);
        em.persist(casal);
        ReservaTipoQuartoId id1 = new ReservaTipoQuartoId(reserva, solteiro);
        ReservaTipoQuarto reservaTipoQuarto1 = new ReservaTipoQuarto(id1,1);
        ReservaTipoQuartoId id2 = new ReservaTipoQuartoId(reserva, casal);
        ReservaTipoQuarto reservaTipoQuarto2 = new ReservaTipoQuarto(id2,1);

        Assertions.assertNotEquals(id1, id2);
        
        ReservaTipoQuarto result1 = em.persistFlushFind(reservaTipoQuarto1);
        ReservaTipoQuarto result2 = em.persistFlushFind(reservaTipoQuarto2);

        Assertions.assertEquals(reservaTipoQuarto1, result1);
        Assertions.assertEquals(reservaTipoQuarto2, result2);
    }
    @Test
    public void lancarExcecaoAoPersistirChaveCompostaDuplicada(){
        ReservaEntity reserva = new ReservaEntity();
        em.persist(reserva);
        TipoQuartoEntity solteiro = new TipoQuartoEntity("Solteiro", TipoQuartoEnum.SOLTEIRO, 0);
        em.persist(solteiro);
        ReservaTipoQuartoId id = new ReservaTipoQuartoId(reserva, solteiro);
        ReservaTipoQuarto reservaTipoQuarto = new ReservaTipoQuarto(id,1);
        em.persist(reservaTipoQuarto);
        em.detach(reservaTipoQuarto);
    
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            em.persistAndFlush(reservaTipoQuarto);
        });
    }
}
