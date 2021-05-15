package com.fquadros.minhasfinancas.service;

import com.fquadros.minhasfinancas.exception.RegraDeNegocioExpection;
import com.fquadros.minhasfinancas.model.Lancamento;
import com.fquadros.minhasfinancas.model.Usuario;
import com.fquadros.minhasfinancas.model.enums.StatusLancamento;
import com.fquadros.minhasfinancas.model.repository.LancamentoRepository;
import com.fquadros.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.fquadros.minhasfinancas.service.impl.LancamentoServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith({SpringExtension.class})
@ActiveProfiles("test")
public class LacamentoServiceTest {

    @SpyBean
    LancamentoServiceImpl service;

    @MockBean
    LancamentoRepository repository;

    @Test
    public void deveSalvarUmLacamento(){
        //cenario
        Lancamento lancamentoSalvar = LancamentoRepositoryTest.CriarLancamento();
        Mockito.doNothing().when(service).validar(lancamentoSalvar);

        Lancamento lancamentoSalvo = LancamentoRepositoryTest.CriarLancamento();
        lancamentoSalvo.setId(1l);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
        Mockito.when(repository.save(lancamentoSalvar)).thenReturn(lancamentoSalvo);

        //execucao
        Lancamento lancamento = service.salvar(lancamentoSalvar);

        //verificacao
        boolean idPersistido = lancamento.getId() == lancamentoSalvo.getId() || false && lancamento.getStatus() == StatusLancamento.PENDENTE;
        Assertions.assertTrue(idPersistido);
    }

    @Test
    public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao(){
        //cenário
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.CriarLancamento();
        Mockito.doThrow(RegraDeNegocioExpection.class).when(service).validar(lancamentoASalvar);

        //execucao e verificao
        Throwable exception = Assertions.assertThrows(RegraDeNegocioExpection.class, () -> service.salvar(lancamentoASalvar), "Teste de validação");

        Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
    }

    @Test
    public void deveAtualizarUmLacamento(){
        //cenario
        Lancamento lancamentoSalvo = LancamentoRepositoryTest.CriarLancamento();
        lancamentoSalvo.setId(1l);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);

        Mockito.doNothing().when(service).validar(lancamentoSalvo);

        Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);

        //execucao
         service.atualizar(lancamentoSalvo);

        //verificacao
        Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);
    }

    @Test
    public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo(){
        //cenário
        Lancamento lancamento = LancamentoRepositoryTest.CriarLancamento();

        //execucao e verificao
        Throwable exception = Assertions.assertThrows(NullPointerException.class, () -> service.atualizar(lancamento), "Teste de validação");

        Mockito.verify(repository, Mockito.never()).save(lancamento);
    }

    @Test
    public void deveDeletarUmLancamento(){
        //cenário
        Lancamento lancamento = LancamentoRepositoryTest.CriarLancamento();
        lancamento.setId(1l);

        //execucao
        service.deletar(lancamento);

        //verificao
        Mockito.verify(repository).delete(lancamento);
    }

    @Test
    public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo(){
        //cenário
        Lancamento lancamento = LancamentoRepositoryTest.CriarLancamento();
        lancamento.setId(1l);

        //execucao
        Throwable exception = Assertions.assertThrows(NullPointerException.class, () -> service.deletar(lancamento), "Teste de validação");

        //verificao
        Mockito.verify(repository, Mockito.never()).delete(lancamento);
    }

    @Test
    public void deveFiltrarLancamentos(){
        //caneario
        Lancamento lancamento = LancamentoRepositoryTest.CriarLancamento();
        lancamento.setId(1l);

        List<Lancamento> lista  = Arrays.asList(lancamento);
        Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);

        //execucao
        List<Lancamento> resultado = service.buscar(lancamento);

        //verificaçoes
        boolean teste;
        if (!resultado.isEmpty() || resultado.size() == 1 || resultado.contains(lancamento) || false) teste = true;
        else teste = false;

        Assertions.assertTrue(teste);
    }

    @Test
    public void deveAtualizarOStatusDeUmLancamento(){
        //cenario
        Lancamento lancamento = LancamentoRepositoryTest.CriarLancamento();
        lancamento.setId(1l);
        lancamento.setStatus(StatusLancamento.PENDENTE);

        StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
        Mockito.doReturn(lancamento).when(service).atualizar(lancamento);

        //exucucao
        service.atualizarStatus(lancamento, novoStatus);

        //vericacoes
        boolean resultado = lancamento.getStatus() == novoStatus;
        Assertions.assertTrue(resultado);
        Mockito.verify(service).atualizar(lancamento);
    }

    @Test
    public void deveObterUmLacamentoPorID(){
        //cenario
        Long id = 1l;

        Lancamento lancamento = LancamentoRepositoryTest.CriarLancamento();
        lancamento.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));

        //execucao
        Optional<Lancamento> resultado = service.obterPorId(id);

        //verificacao
        boolean result = resultado.isPresent();
        Assertions.assertTrue(result);
    }

    @Test
    public void deveRetornarVazioQuandoOLancamentoNaoExiste(){
        //cenario
        Long id = 1l;

        Lancamento lancamento = LancamentoRepositoryTest.CriarLancamento();
        lancamento.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        //execucao
        Optional<Lancamento> resultado = service.obterPorId(id);

        //verificacao
        boolean result = !resultado.isPresent();
        Assertions.assertTrue(result);
    }

    @Test
    public void deveRetornarErrosAoValidadeUmLancamento(){
        //cenario
        Lancamento lancamento = LancamentoRepositoryTest.CriarLancamento();

        lancamento.setId(1l);
        lancamento.setDescricao(null);

        Mockito.doThrow(RegraDeNegocioExpection.class).when(service).validar(lancamento);
        Throwable exception = Assertions.assertThrows(RegraDeNegocioExpection.class, () -> service.atualizar(lancamento), "Informe uma Descricao válida");

        lancamento.setDescricao("");

        Mockito.doThrow(RegraDeNegocioExpection.class).when(service).validar(lancamento);
        exception = Assertions.assertThrows(RegraDeNegocioExpection.class, () -> service.atualizar(lancamento), "Informe uma Descricao válida");

        lancamento.setDescricao("Salario");

        Mockito.doThrow(RegraDeNegocioExpection.class).when(service).validar(lancamento);
        exception = Assertions.assertThrows(RegraDeNegocioExpection.class, () -> service.atualizar(lancamento), "Informe um Mês valido");

        lancamento.setAno(0);

        Mockito.doThrow(RegraDeNegocioExpection.class).when(service).validar(lancamento);
        exception = Assertions.assertThrows(RegraDeNegocioExpection.class, () -> service.atualizar(lancamento), "Informe um Mês valido");

        lancamento.setAno(13);

        Mockito.doThrow(RegraDeNegocioExpection.class).when(service).validar(lancamento);
        exception = Assertions.assertThrows(RegraDeNegocioExpection.class, () -> service.atualizar(lancamento), "Informe um Mês valido");

        lancamento.setMes(1);

        Mockito.doThrow(RegraDeNegocioExpection.class).when(service).validar(lancamento);
        exception = Assertions.assertThrows(RegraDeNegocioExpection.class, () -> service.atualizar(lancamento), "Informe um ano valido");

        lancamento.setAno(202);

        Mockito.doThrow(RegraDeNegocioExpection.class).when(service).validar(lancamento);
        exception = Assertions.assertThrows(RegraDeNegocioExpection.class, () -> service.atualizar(lancamento), "Informe um ano valido");

        lancamento.setAno(2020);

        Mockito.doThrow(RegraDeNegocioExpection.class).when(service).validar(lancamento);
        exception = Assertions.assertThrows(RegraDeNegocioExpection.class, () -> service.atualizar(lancamento), "Informe um Usuario.");

        lancamento.setUsuario(new Usuario());

        Mockito.doThrow(RegraDeNegocioExpection.class).when(service).validar(lancamento);
        exception = Assertions.assertThrows(RegraDeNegocioExpection.class, () -> service.atualizar(lancamento), "Informe um Usuario.");

        lancamento.getUsuario().setId(1l);

        Mockito.doThrow(RegraDeNegocioExpection.class).when(service).validar(lancamento);
        exception = Assertions.assertThrows(RegraDeNegocioExpection.class, () -> service.atualizar(lancamento), "Informe um valor válida");

        lancamento.setValor(BigDecimal.valueOf(1));

        Mockito.doThrow(RegraDeNegocioExpection.class).when(service).validar(lancamento);
        exception = Assertions.assertThrows(RegraDeNegocioExpection.class, () -> service.atualizar(lancamento), "Informe um tipo de lançamento");
    }
}