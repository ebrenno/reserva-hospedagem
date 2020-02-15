/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.model.quarto;

import br.com.hotel.reservashospedagens.persistencia.entidade.TipoQuartoEntity;
import br.com.hotel.reservashospedagens.persistencia.repositorio.TipoQuartoRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipoQuartoModel {

    @Autowired
    TipoQuartoRepositorio tipoQuartoRepositorio;

    public TipoQuartoEntity getEntity(TipoQuartoEnum tipo) throws TipoQuartoNaoExisteException {
        Optional<TipoQuartoEntity> container = tipoQuartoRepositorio.findByTipo(tipo);
        TipoQuartoEntity tipoQuartoEntity = container.orElseThrow(TipoQuartoNaoExisteException::new);
        return tipoQuartoEntity;
    }

    public TipoQuartoEntity encontrarPorId(int id) throws TipoQuartoNaoExisteException {
        Optional<TipoQuartoEntity> container = tipoQuartoRepositorio.findById(id);
        TipoQuartoEntity tipoQuartoEntity = container.orElseThrow(TipoQuartoNaoExisteException::new);
        return tipoQuartoEntity;
    }

    public double getValor(TipoQuartoEnum tipo) throws TipoQuartoNaoExisteException {
        return this.getEntity(tipo).getPreco();
    }
}
