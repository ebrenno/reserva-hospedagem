package br.com.hotel.reservashospedagens.testes.integracao.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import br.com.hotel.reservashospedagens.model.quarto.QuartoModel;
import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoEnum;
import br.com.hotel.reservashospedagens.persistencia.entidade.QuartoEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.TipoQuartoEntity;
import jakarta.transaction.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
public class QuartoModelTeste {

    @Autowired
    QuartoModel quartoModel;
    @Autowired
    TestEntityManager em;
    @Test
    public void retornaNenhumQuarto() {
        int count = quartoModel.listarTodos().size();
        
        Assertions.assertTrue(count == 0, "retornou: " + count);
    }
    @Test
    public void retornaUmQuarto() {
        TipoQuartoEntity solteiro = new TipoQuartoEntity();
        solteiro = em.persist(solteiro);
        QuartoEntity quarto = new QuartoEntity(solteiro, 102, 1);
        quarto = em.persist(quarto);

        int count = quartoModel.listarTodos().size();

        Assertions.assertTrue(count == 1, "retornou: " + count);
    }
    @Test
    public void retornaUmQuartoDeSolteiroEUmQuartoDeCasal() {
        TipoQuartoEntity solteiro = new TipoQuartoEntity("quarto solteiro", TipoQuartoEnum.SOLTEIRO, 0);
        em.persist(solteiro);
        TipoQuartoEntity casal = new TipoQuartoEntity("quarto casal", TipoQuartoEnum.CASAL, 0);
        em.persist(casal);
        QuartoEntity quartoSolteiro = new QuartoEntity(solteiro, 102, 1);
        em.persist(quartoSolteiro);
        QuartoEntity quartoCasal = new QuartoEntity(casal, 102, 1);
        em.persist(quartoCasal);

        int count = quartoModel.listarTodos().size();

        Assertions.assertTrue(count == 2, "retornou: " + count);
    }
}
