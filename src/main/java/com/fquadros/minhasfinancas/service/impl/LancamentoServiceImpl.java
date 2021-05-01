package com.fquadros.minhasfinancas.service.impl;

import com.fquadros.minhasfinancas.exception.RegraDeNegocioExpection;
import com.fquadros.minhasfinancas.model.Lancamento;
import com.fquadros.minhasfinancas.model.enums.StatusLancamento;
import com.fquadros.minhasfinancas.model.repository.LancamentoRepository;
import com.fquadros.minhasfinancas.service.LancamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LancamentoServiceImpl implements LancamentoService {

    @Autowired
    private LancamentoRepository repository;

    public LancamentoServiceImpl(LancamentoRepository repository){
        this.repository = repository;
    }

    @Override
    @Transactional
    public Lancamento salvar(Lancamento lancamento) {
        validar(lancamento);
        lancamento.setStatus(StatusLancamento.PENDENTE);
        return repository.save(lancamento);
    }

    @Override
    @Transactional
    public Lancamento atualizar(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getId());
        validar(lancamento);
        return repository.save(lancamento);
    }

    @Override
    public void deletar(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getId());
        repository.delete(lancamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
        Example example = Example.of(lancamentoFiltro, ExampleMatcher.matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return repository.findAll(example);
    }

    @Override
    public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
        lancamento.setStatus(status);
        atualizar(lancamento);
    }

    @Override
    public void validar(Lancamento lancamento) {

        if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")){
            throw new RegraDeNegocioExpection("Informe uma Descricao válida");
        }

        if (lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12 ){
            throw  new RegraDeNegocioExpection("Informe um Mês valido");
        }

        if (lancamento.getAno() == null || lancamento.getAno().toString().length() != 4){
            throw  new RegraDeNegocioExpection("Informe um ano valido");
        }

        if (lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null){
            throw  new RegraDeNegocioExpection(("Informe um Usuario."));
        }

        if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1 ){
            throw  new RegraDeNegocioExpection("Informe um valor válida");
        }

        if (lancamento.getTipo() == null){
            throw new RegraDeNegocioExpection("Informe um tipo de lançamento");
        }
    }

    @Override
    public Optional<Lancamento> obterPorId(Long id) {
        return repository.findById(id);
    }
}
