/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.persistencia.repositorio;

import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaEntity;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepositorio extends CrudRepository<ReservaEntity, Integer> {

    @Query("select r from ReservaEntity r where r.checkin between :checkin and :checkout or r.checkout between :checkin and :checkout")
    Collection<ReservaEntity> findReservasBetween(@Param("checkin") LocalDate checkin, @Param("checkout") LocalDate checkout);

    Optional<ReservaEntity> findById(int id);

}
