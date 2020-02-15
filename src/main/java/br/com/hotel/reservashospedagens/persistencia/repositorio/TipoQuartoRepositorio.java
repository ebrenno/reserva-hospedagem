/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.persistencia.repositorio;

import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoEnum;
import br.com.hotel.reservashospedagens.persistencia.entidade.TipoQuartoEntity;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoQuartoRepositorio extends CrudRepository<TipoQuartoEntity, Integer> {

    public Optional<TipoQuartoEntity> findByTipo(TipoQuartoEnum tipo);

}
