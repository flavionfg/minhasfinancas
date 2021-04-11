package com.fquadros.minhasfinancas.model.repository;

import com.fquadros.minhasfinancas.model.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
}
