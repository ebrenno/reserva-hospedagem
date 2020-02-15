/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.model.cliente;

import br.com.hotel.reservashospedagens.exception.ClienteNaoEncontradoException;
import br.com.hotel.reservashospedagens.persistencia.entidade.ClienteEntity;
import br.com.hotel.reservashospedagens.persistencia.repositorio.ClienteRepositorio;
import java.util.Optional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Cliente {

    Log log = LogFactory.getLog(Cliente.class);
    @Autowired
    ClienteRepositorio clienteRepositorio;

    public ClienteEntity getEntityPorId(int id) throws ClienteNaoEncontradoException {
        log.info("buscando entidade com o id: " + id);
        Optional<ClienteEntity> container = clienteRepositorio.findById(id);
        return container.orElseThrow(ClienteNaoEncontradoException::new);
    }

}
