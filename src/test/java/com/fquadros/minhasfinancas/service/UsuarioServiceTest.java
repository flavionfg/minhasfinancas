package com.fquadros.minhasfinancas.service;

import com.fquadros.minhasfinancas.exception.ErroAutenticacao;
import com.fquadros.minhasfinancas.exception.RegraDeNegocioExpection;
import com.fquadros.minhasfinancas.model.Usuario;
import com.fquadros.minhasfinancas.model.repository.UsuarioRepository;
import com.fquadros.minhasfinancas.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith({SpringExtension.class})
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @SpyBean
    UsuarioServiceImpl service;

    @MockBean
    UsuarioRepository repository;

    @Test
    public void deveSalvarUmUsuario(){
        //cenario
        Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
        Usuario usuario = Usuario.builder()
                .id(1l)
                .nome("nome")
                .email("email@email.com")
                .senha("senha").build();

        Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        //ação
        Usuario usuarioSalvo = service.salvarUsuario(new Usuario());

        //verificaçao
        Assertions.assertNotNull(usuarioSalvo);
        Assertions.assertEquals(1l, usuarioSalvo.getId());
        Assertions.assertEquals("nome", usuarioSalvo.getNome());
        Assertions.assertEquals("email@email.com", usuarioSalvo.getEmail());
        Assertions.assertEquals("senha",usuarioSalvo.getSenha());
    }

    @Test
    public void naoDeveSalvarUmUsuaruiComEmailJacadastrado(){
        //cenario
        String email = "email@email.com";
        Usuario usuario = Usuario.builder().email(email).build();
        Mockito.doThrow(RegraDeNegocioExpection.class).when(service).validarEmail(email);

        //ação
        try{
            service.salvarUsuario(usuario);

        }catch (RegraDeNegocioExpection ex){
            ex.equals("Já existe um usuario cadastrado com este email");
            Assertions.assertNotNull(ex);
        }

        //Refatorar este método!!!

        //verificaçao
       //Mockito.verify(repository, Mockito.never()).save(usuario);
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
        Assertions.assertNotNull(result);

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
        RegraDeNegocioExpection erro = Assertions.assertThrows(RegraDeNegocioExpection.class, () -> service.validarEmail("email@email.com"),"Já existe um usuario cadastrado com este email");
    }
}
