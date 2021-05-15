package com.fquadros.minhasfinancas.model.repository;

import com.fquadros.minhasfinancas.model.Lancamento;
import com.fquadros.minhasfinancas.model.enums.StatusLancamento;
import com.fquadros.minhasfinancas.model.enums.TipoLançamento;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

    @Autowired
    LancamentoRepository repository;

    @Autowired
    TestEntityManager entityManager;
    private Lancamento lancamentoAtulizado;

    @Test
    public void deveSalvarUmLançamento(){

        Lancamento lancamento = CriarLancamento();

        lancamento = repository.save(lancamento);

        Assertions.assertNotNull(lancamento.getId());
    }

    @Test
    public void deveDeletarUmLancamento(){
        Lancamento lancamento = CriarLancamento();
        entityManager.persist(lancamento);

       lancamento = entityManager.find(Lancamento.class, lancamento.getId());

       repository.delete(lancamento);

       Lancamento lancamentoInesistente = entityManager.find(Lancamento.class, lancamento.getId());

       Assertions.assertNull(lancamentoInesistente);
    }

    @Test
    public void deveAtualizarUmLancamento(){
        Lancamento lancamento = CriarLancamento();

        lancamento.setAno(2018);
        lancamento.setDescricao("Teste Atualizado");
        lancamento.setStatus(StatusLancamento.CANCELADO);

        repository.save(lancamento);

        lancamentoAtulizado = entityManager.find(Lancamento.class, lancamento.getId());

        boolean AnoPersistido;

        if (lancamentoAtulizado.getAno() == 2018
                && lancamentoAtulizado.getDescricao() == "Teste Atualizado"
                && lancamentoAtulizado.getStatus() == StatusLancamento.CANCELADO ){
            AnoPersistido = true;
        }else{
            AnoPersistido = false;
        }
        Assertions.assertTrue(AnoPersistido);
    }

    @Test
    public void deveBuscarUmLancamentoPorID(){
        Lancamento lancamento = criarEPersistirUmLancamento();

        Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());

        Assertions.assertTrue(lancamento.getId() != null);
    }

    private Lancamento criarEPersistirUmLancamento(){
        Lancamento lancamento = CriarLancamento();
        entityManager.persist(lancamento);
        return lancamento;
    }

    public static Lancamento CriarLancamento() {
        return Lancamento.builder()
                .ano(2019)
                .mes(1)
                .descricao("Lancamento qualquer")
                .valor(BigDecimal.valueOf(10))
                .tipo(TipoLançamento.RECEITA)
                .status(StatusLancamento.PENDENTE)
                .dataCadastro(LocalDate.now())
                .build();
    }
}
