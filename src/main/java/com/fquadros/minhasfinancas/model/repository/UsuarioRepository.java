package com.fquadros.minhasfinancas.model.repository;

import com.fquadros.minhasfinancas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
