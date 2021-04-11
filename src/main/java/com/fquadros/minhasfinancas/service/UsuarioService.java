package com.fquadros.minhasfinancas.service;

import com.fquadros.minhasfinancas.model.Usuario;

public interface UsuarioService {

    Usuario autenticar(String email, String senha);

    Usuario salvarUsuario(Usuario usuario);

    void validarEmail(String email);

}
