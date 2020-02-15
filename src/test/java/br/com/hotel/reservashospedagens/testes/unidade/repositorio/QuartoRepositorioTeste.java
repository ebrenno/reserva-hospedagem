/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.testes.unidade.repositorio;

import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoEnum;
import br.com.hotel.reservashospedagens.persistencia.entidade.QuartoEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.TipoQuartoEntity;
import br.com.hotel.reservashospedagens.persistencia.repositorio.QuartoRepositorio;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class QuartoRepositorioTeste {

    @Autowired
    TestEntityManager em;
    @Autowired
    QuartoRepositorio quartoRepositorio;

    @Test
    public void TemDoisQuartosDeSolteiro() {
        TipoQuartoEntity solteiro = new TipoQuartoEntity("quarto solteiro", TipoQuartoEnum.SOLTEIRO, 0);
        TipoQuartoEntity casal = new TipoQuartoEntity("quarto casal", TipoQuartoEnum.CASAL, 0);
        solteiro = em.persist(solteiro);
        casal = em.persist(casal);
        List<QuartoEntity> quartos = Arrays.asList(
                new QuartoEntity(solteiro, 102, 1),
                new QuartoEntity(casal, 102, 1),
                new QuartoEntity(solteiro, 103, 1)
        );
        quartos.forEach(quarto -> em.persist(quarto));

        int count = quartoRepositorio.countAllByTipo(solteiro);

        Assertions.assertTrue(count == 2, "retornou: " + count);
    }
}
