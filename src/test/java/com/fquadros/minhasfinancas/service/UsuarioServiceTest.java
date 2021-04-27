package com.fquadros.minhasfinancas.service;

import com.fquadros.minhasfinancas.exception.ErroAutenticacao;
import com.fquadros.minhasfinancas.exception.RegraDeNegocioExpection;
import com.fquadros.minhasfinancas.model.Usuario;
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

import java.util.Optional;

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
    public void deveAutenticarUmUsuarioComSucesso(){
        //cenario
        String email = "email@email.com";
        String senha = "senha";

        Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

        //ação
        Usuario result =  service.autenticar(email, senha);

        //Verificação
        Assertions.assertTrue(result != null);

    }

    @Test
    public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado(){

        //cenario
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        //ação
        Throwable exception = Assertions.assertThrows(ErroAutenticacao.class, () ->  service.autenticar("email@email.com","senha"), "Usuario não encontrado para o email informado");
    }

    @Test
    public void deveLancarErroQuandoSenhaNaoBater(){
        //cenario
        String senha = "senha";
        Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

        //ação
        Throwable exception = Assertions.assertThrows(ErroAutenticacao.class, () ->  service.autenticar("email@email.com", "123"), "Senha invalida.");
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
