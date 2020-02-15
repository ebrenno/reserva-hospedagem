/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.testes.unidade.repositorio;

import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoEnum;
import br.com.hotel.reservashospedagens.persistencia.entidade.TipoQuartoEntity;
import br.com.hotel.reservashospedagens.persistencia.repositorio.TipoQuartoRepositorio;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class TipoQuartoRepositorioTeste {

    @Autowired
    TestEntityManager em;
    @Autowired
    TipoQuartoRepositorio tipoQuartoRepositorio;

    @Test
    public void RetornaNenhumQuarto() {
        TipoQuartoEntity casal = new TipoQuartoEntity("casal", TipoQuartoEnum.CASAL, 0);
        em.persist(casal);

        Optional<TipoQuartoEntity> result = tipoQuartoRepositorio.findByTipo(TipoQuartoEnum.SOLTEIRO);

        Assertions.assertFalse(result.isPresent(), "retornou: " + result.isPresent());
    }

    @Test
    public void RetornaQuartoDeSolteiro() {
        TipoQuartoEntity solteiro = new TipoQuartoEntity("solteiro", TipoQuartoEnum.SOLTEIRO, 0);
        solteiro = em.persist(solteiro);

        Optional<TipoQuartoEntity> result = tipoQuartoRepositorio.findByTipo(TipoQuartoEnum.SOLTEIRO);
        TipoQuartoEntity tipo = result.get();
        Assertions.assertTrue(solteiro == tipo, "retornou: " + tipo.getTipo());
    }
}
