/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.controller;

import br.com.hotel.reservashospedagens.dto.reserva.ReservaDto;
import br.com.hotel.reservashospedagens.dto.reserva.VagaDto;
import br.com.hotel.reservashospedagens.exception.ClienteNaoEncontradoException;
import br.com.hotel.reservashospedagens.exception.NaoHaVagasException;
import br.com.hotel.reservashospedagens.model.cliente.Cliente;
import br.com.hotel.reservashospedagens.model.pagamento.CobrancaModel;
import br.com.hotel.reservashospedagens.model.reserva.ReservaModel;
import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoNaoExisteException;
import br.com.hotel.reservashospedagens.persistencia.entidade.ClienteEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/hotel/reserva")
public class ReservaController {

    Log log = LogFactory.getLog(ReservaController.class);
    @Autowired
    ReservaModel reservaModel;
    @Autowired
    ObjectMapper om;
    @Autowired
    Cliente clienteModel;
    @Autowired
    CobrancaModel cobrancaModel;

    @GetMapping(path = "/vagas-disponiveis-entre-{checkin}-e-{checkout}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> checarDisponibilidade(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkin,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkout) throws JsonProcessingException, TipoQuartoNaoExisteException {
        log.info("requisicao para busca de vagas iniciada");
        Collection<VagaDto> vagas = reservaModel.todasVagasRestantes(checkin, checkout);

        String vagasJson = om.writeValueAsString(vagas);
        return ResponseEntity.ok(vagasJson);
    }

    @PostMapping(path = "/aplicar/{cliente_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity aplicarReserva(@PathVariable int cliente_id, @RequestBody String reservaJson) throws JsonProcessingException, ClienteNaoEncontradoException, TipoQuartoNaoExisteException, NaoHaVagasException {

        ReservaDto reservaDto = om.readValue(reservaJson, ReservaDto.class);
        log.info("requisição recebida para o cliente com o id: " + cliente_id);
        ClienteEntity clienteEntity = clienteModel.getEntityPorId(cliente_id);
        log.info("cliente com id[" + cliente_id + "] encontrado");
        reservaModel.iniciar(clienteEntity, reservaDto.getVagas(), reservaDto.getCheckin(), reservaDto.getCheckout());
        log.info("reserva concluida para o cliente com o id: " + cliente_id);
        return ResponseEntity.ok().build();
    }

}
