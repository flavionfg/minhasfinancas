package com.fquadros.minhasfinancas.service;

import com.fquadros.minhasfinancas.exception.RegraDeNegocioExpection;
import com.fquadros.minhasfinancas.model.repository.UsuarioRepository;
import com.fquadros.minhasfinancas.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class})
@ActiveProfiles("test")
public class UsuarioServiceTest {

    UsuarioService service;

    @MockBean
    UsuarioRepository repository;

    @BeforeEach
    public void setUP(){
        service =  new UsuarioServiceImpl(repository);
    }

    @Test()
    public void deveValidarEmail(){

        //cenario
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

        //ação
        service.validarEmail("email@email.com");
    }

    @Test()
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado(){

        //cenario
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

        //ação
        RegraDeNegocioExpection erro = Assertions.assertThrows(RegraDeNegocioExpection.class, () -> service.validarEmail("email@email.com"));

        Assertions.assertTrue(erro.getMessage().contains("Já existe um usuario cadastrado com este email"));
    }
}
