/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.model.cliente;

import br.com.hotel.reservashospedagens.exception.UsuarioNaoEncontradoException;
import br.com.hotel.reservashospedagens.persistencia.entidade.ClienteEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.UsuarioEntity;
import br.com.hotel.reservashospedagens.persistencia.repositorio.UsuarioRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioModel {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    public UsuarioEntity encontrarPorEmailESenha(String email, String senha) throws UsuarioNaoEncontradoException {

        Optional<UsuarioEntity> container = usuarioRepositorio.findByEmail(email);
        UsuarioEntity usuario = container.orElseThrow(UsuarioNaoEncontradoException::new);
        boolean combina = this.validarSenha(senha, usuario.getSenha());
        if (!combina) {
            throw new UsuarioNaoEncontradoException();
        }
        return usuario;
    }

    public ClienteEntity cadastrar(UsuarioEntity usuarioEntity) {
        final String SENHA_ENCRIPTADA = this.encriptar(usuarioEntity.getSenha());
        usuarioEntity.setSenha(SENHA_ENCRIPTADA);
        usuarioEntity = usuarioRepositorio.save(usuarioEntity);
        return usuarioEntity.getCliente();
    }

    public String encriptar(String senha) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(senha);
        return hash;
    }

    public boolean validarSenha(String senhaEnviada, String senhaArmazenada) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(senhaEnviada, senhaArmazenada);
    }

}
