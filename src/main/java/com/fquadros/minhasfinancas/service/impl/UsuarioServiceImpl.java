package com.fquadros.minhasfinancas.service.impl;

import com.fquadros.minhasfinancas.exception.RegraDeNegocioExpection;
import com.fquadros.minhasfinancas.model.Usuario;
import com.fquadros.minhasfinancas.model.repository.UsuarioRepository;
import com.fquadros.minhasfinancas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository repository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        return null;
    }

    @Override
    public Usuario salvarUsuario(Usuario usuario) {
        return null;
    }

    @Override
    public void validarEmail(String email) {

        boolean existe = repository.existsByEmail(email);
        if (existe){
            throw new RegraDeNegocioExpection("Já existe um usuario cadastrado com este email");
        }
    }
}
