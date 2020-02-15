/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.controller;

import br.com.hotel.reservashospedagens.exception.ClienteNaoEncontradoException;
import br.com.hotel.reservashospedagens.exception.DataForaDoPrazoException;
import br.com.hotel.reservashospedagens.exception.HospedagemNaoExisteException;
import br.com.hotel.reservashospedagens.exception.NaoHaVagasException;
import br.com.hotel.reservashospedagens.exception.ReservaNaoExisteException;
import br.com.hotel.reservashospedagens.exception.UsuarioNaoEncontradoException;
import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoNaoExisteException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    Log log = LogFactory.getLog(ExceptionController.class);

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity usuarioNaoEnontrado() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler({ClienteNaoEncontradoException.class, HospedagemNaoExisteException.class, ReservaNaoExisteException.class})
    public ResponseEntity clienteNaoEncontrado(Exception e) {
        log.error("conteudo não encontrado", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(TipoQuartoNaoExisteException.class)
    public ResponseEntity recursoRemovido() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler({DataForaDoPrazoException.class, NaoHaVagasException.class})
    public ResponseEntity ImpossivelProsseguir() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
