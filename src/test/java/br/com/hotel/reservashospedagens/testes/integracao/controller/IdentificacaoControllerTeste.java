/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.testes.integracao.controller;

import br.com.hotel.reservashospedagens.dto.cliente.CadastroDTO;
import br.com.hotel.reservashospedagens.dto.cliente.UsuarioDTO;
import br.com.hotel.reservashospedagens.dto.cliente.ClienteDTO;
import br.com.hotel.reservashospedagens.model.cliente.UsuarioModel;
import br.com.hotel.reservashospedagens.persistencia.entidade.ClienteEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.UsuarioEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureTestEntityManager
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class IdentificacaoControllerTeste {

    @Autowired
    TestEntityManager em;
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper om;
    @Autowired
    UsuarioModel usuarioModel;

    @Test
    public void RetornaUmClienteAoAutenticar() throws Exception {
        String email = "email@email.com";
        String senha = "senha";
        ClienteEntity cliente = new ClienteEntity();
        UsuarioEntity usuario = new UsuarioEntity(email, usuarioModel.encriptar(senha), cliente);
        em.persist(usuario);

        UsuarioDTO autenticacao = new UsuarioDTO(email, senha);
        String body = om.writeValueAsString(autenticacao);
        String URI = "/hotel/usuario/identificacao";
        String response = mvc.perform(get(URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn()
                .getResponse()
                .getContentAsString();
        ClienteDTO result = om.readValue(response, ClienteDTO.class);
        Assertions.assertEquals(cliente.getId(), result.getId());

    }

    @Test
    public void CadastraUmNovoUsuario() throws Exception {
        String email = "email@email.com";
        String senha = "senha";
        ClienteDTO cliente = new ClienteDTO("nome", "rg", "sexo");
        UsuarioDTO usuario = new UsuarioDTO(email, senha);
        CadastroDTO cadastro = new CadastroDTO(usuario, cliente);
        String body = om.writeValueAsString(cadastro);

        String URI = "/hotel/usuario/cadastro";
        String response = mvc.perform(post(URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ClienteDTO result = om.readValue(response, ClienteDTO.class);

        Assertions.assertEquals(cliente.getNome(), result.getNome());
    }

    @Test
    public void RetornaErro401_AoAutenticarIncorretamente() throws Exception {
        UsuarioDTO autenticacao = new UsuarioDTO();
        String body = om.writeValueAsString(autenticacao);
        String URI = "/hotel/usuario/identificacao";
        mvc.perform(get(URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isUnauthorized());
    }
}
