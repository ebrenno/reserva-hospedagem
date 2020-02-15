/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.persistencia.repositorio;

import br.com.hotel.reservashospedagens.model.hospedagem.HospedagemStatusEnum;
import br.com.hotel.reservashospedagens.persistencia.entidade.ClienteEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.HospedagemEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.QuartoEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HospedagemRepositorio extends CrudRepository<HospedagemEntity, Integer> {
    @Query("select h from HospedagemEntity h where h.cliente = :cliente and h.status = :status")
    public List<HospedagemEntity> todasHospedagensIniciadas(@Param("cliente")ClienteEntity clienteEntity,@Param("status")HospedagemStatusEnum status);

    @Query(value = "select h from HospedagemEntity h where :quarto member of h.quartos and :agora between h.checkin and h.checkout")
    public HospedagemEntity findByQuarto(@Param("quarto") QuartoEntity quartoEntity, @Param("agora") LocalDate agora);

    @Query("select quarto from HospedagemEntity h join h.quartos quarto where :agora between h.checkin and h.checkout")
    public List<QuartoEntity> encontrarQuartosOcupadosNesteMomento(@Param("agora") LocalDate agora);

}
