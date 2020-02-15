/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.controller;

import br.com.hotel.reservashospedagens.dto.cliente.CadastroDTO;
import br.com.hotel.reservashospedagens.dto.cliente.UsuarioDTO;
import br.com.hotel.reservashospedagens.dto.cliente.ClienteDTO;
import br.com.hotel.reservashospedagens.exception.UsuarioNaoEncontradoException;
import br.com.hotel.reservashospedagens.model.cliente.UsuarioModel;
import br.com.hotel.reservashospedagens.persistencia.entidade.ClienteEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.UsuarioEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hotel/usuario")
public class UsuarioController {

    @Autowired
    ObjectMapper om;
    @Autowired
    UsuarioModel usuarioModel;

    @GetMapping(path = "/identificacao", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> identificarUsuario(@RequestBody String autenticacaoJson) throws UsuarioNaoEncontradoException, JsonProcessingException {
        UsuarioDTO autenticacao = om.readValue(autenticacaoJson, UsuarioDTO.class);
        UsuarioEntity usuario = usuarioModel.encontrarPorEmailESenha(autenticacao.getEmail(), autenticacao.getSenha());
        ClienteDTO clienteDto = ClienteDTO.toDTO(usuario.getCliente());
        String response = om.writeValueAsString(clienteDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/cadastro", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> cadastrarUsuario(@RequestBody String cadastroJson) throws JsonProcessingException {
        CadastroDTO cadastro = om.readValue(cadastroJson, CadastroDTO.class);
        ClienteEntity cliente = usuarioModel.cadastrar(cadastro.getUsuarioEntity());
        ClienteDTO clienteDTO = ClienteDTO.toDTO(cliente);
        String response = om.writeValueAsString(clienteDTO);
        return ResponseEntity.ok(response);
    }

}
