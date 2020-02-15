/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.model.quarto;

import br.com.hotel.reservashospedagens.persistencia.entidade.QuartoEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.TipoQuartoEntity;
import br.com.hotel.reservashospedagens.persistencia.repositorio.QuartoRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuartoModel {

    @Autowired
    QuartoRepositorio quartoRespositorio;
    @Autowired
    TipoQuartoModel tipoQuartoModel;

    public int contarPorTipo(TipoQuartoEntity tipo) {
        int quantidadeQuartos = quartoRespositorio.countAllByTipo(tipo);
        return quantidadeQuartos;
    }

    public QuartoEntity encontrarPor(int id) {
        Optional<QuartoEntity> container = quartoRespositorio.findById(id);
        return container.get();
    }

    public List<QuartoEntity> todosOsQuartos() {
        List<QuartoEntity> lista = new ArrayList<>();
        quartoRespositorio.findAll().iterator().forEachRemaining(lista::add);
        return lista;
    }
}
