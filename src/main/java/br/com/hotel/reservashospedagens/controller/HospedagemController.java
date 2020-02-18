/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.controller;

import br.com.hotel.reservashospedagens.exception.ClienteNaoEncontradoException;
import br.com.hotel.reservashospedagens.exception.DataForaDoPrazoException;
import br.com.hotel.reservashospedagens.exception.HospedagemNaoExisteException;
import br.com.hotel.reservashospedagens.exception.ReservaNaoExisteException;
import br.com.hotel.reservashospedagens.model.cliente.Cliente;
import br.com.hotel.reservashospedagens.model.pagamento.CobrancaModel;
import br.com.hotel.reservashospedagens.model.hospedagem.HospedagemModel;
import br.com.hotel.reservashospedagens.model.quarto.QuartoModel;
import br.com.hotel.reservashospedagens.model.reserva.ReservaModel;
import br.com.hotel.reservashospedagens.persistencia.entidade.ClienteEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.HospedagemEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.QuartoEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.ServicoEntity;
import br.com.hotel.reservashospedagens.persistencia.repositorio.ServicoRepositorio;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotel/hospedagem")
public class HospedagemController {

    @Autowired
    Cliente clienteModel;
    @Autowired
    HospedagemModel hospedagemModel;
    @Autowired
    ReservaModel reservaModel;
    @Autowired
    CobrancaModel cobrancaModel;
    @Autowired
    QuartoModel quartoModel;
    @Autowired
    ServicoRepositorio servicoRepositorio;

    @PostMapping(path = "/checkin/{reserva_id}/checkout-date/{checkout}")
    public ResponseEntity checkin(@PathVariable int reserva_id, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkout) throws ReservaNaoExisteException, DataForaDoPrazoException {
        ReservaEntity reservaEntity = reservaModel.encontrarPorId(reserva_id);
        hospedagemModel.checkin(reservaEntity, checkout);
        /*
            continua com a geração do pagamento da hospedagem
         */
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/checkout/{hospedagem_id}")
    public ResponseEntity checkout(@PathVariable("hospedagem_id") int hospedagemId) throws HospedagemNaoExisteException {
        HospedagemEntity hospedagem = hospedagemModel.encontrarPorId(hospedagemId);
        hospedagemModel.checkout(hospedagem);
        /*
            continua com a geração do pagamento sobre os serviços consumidos
         */
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/servico/{servico_id}/quarto/{quarto_id}")
    public ResponseEntity pedirServico(@PathVariable("servico_id") int servicoId, @PathVariable("quarto_id") int quartoId) {
        QuartoEntity quartoEntity = quartoModel.encontrarPor(quartoId);
        HospedagemEntity hospedagem = hospedagemModel.encontrarPorQuarto(quartoEntity);
        ServicoEntity servico = servicoRepositorio.findById(servicoId).get();
        hospedagemModel.incluirServico(hospedagem, servico);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/iniciadas/{cliente_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<HospedagemEntity>> todasHospedagens(@PathVariable("cliente_id") int clienteId) throws HospedagemNaoExisteException, ClienteNaoEncontradoException, JsonProcessingException {
        ClienteEntity cliente = clienteModel.getEntityPorId(clienteId);
        List<HospedagemEntity> hospedagens = hospedagemModel.encontrarHospedagensIniciadas(cliente);
        return ResponseEntity.ok(hospedagens);
    }
}
