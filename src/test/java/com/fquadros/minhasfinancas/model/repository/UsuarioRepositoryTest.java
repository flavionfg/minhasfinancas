package com.fquadros.minhasfinancas.model.repository;

import com.fquadros.minhasfinancas.model.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class})
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveVerificarAExistenciaDeUmEmail(){
        //cenario
        Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
        entityManager.persist(usuario);

        //açao / execução
        boolean result = repository.existsByEmail("usuario@email.com");

        //verificacao
        Assertions.assertTrue(result);
    }

    @Test
    public void deveRetornarFalsoQuadnoNaoHouverUsuarioCadastradoComOEmail(){
        //cenario

        //acao
        boolean result = repository.existsByEmail("usuario@email.com");

        //verificaçao
        Assertions.assertFalse(result);
    }
}