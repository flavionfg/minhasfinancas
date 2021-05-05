package com.fquadros.minhasfinancas.api.resources;

import com.fquadros.minhasfinancas.api.dto.AtualizarStatusDTO;
import com.fquadros.minhasfinancas.api.dto.LancamentoDTO;
import com.fquadros.minhasfinancas.exception.RegraDeNegocioExpection;
import com.fquadros.minhasfinancas.model.Lancamento;
import com.fquadros.minhasfinancas.model.Usuario;
import com.fquadros.minhasfinancas.model.enums.StatusLancamento;
import com.fquadros.minhasfinancas.model.enums.TipoLançamento;
import com.fquadros.minhasfinancas.service.LancamentoService;
import com.fquadros.minhasfinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentosResource {

    private final LancamentoService service;
    private final UsuarioService usuarioService;
    private StatusLancamento statusSelecionado;

    @GetMapping
    public ResponseEntity buscar(
            @RequestParam(value = "descricao" , required = false) String descricao,
            @RequestParam(value = "mes", required = false) Integer mes,
            @RequestParam(value = "ano", required = false) Integer ano,
            @RequestParam("usuario") Long idUsuario
            //@RequestParam java.util.Map<String, String> params //Outro forma
            ){
        Lancamento lancamentoFiltro = new Lancamento();
        lancamentoFiltro.setDescricao(descricao);
        lancamentoFiltro.setMes(mes);
        lancamentoFiltro.setAno(ano);

        Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
        if (!usuario.isPresent()){
            return ResponseEntity.badRequest().body("Não foi possivel realizar a consulta. Usuario não encontrado para o id informado");
        }else {
           lancamentoFiltro.setUsuario(usuario.get());
        }

        List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
        return ResponseEntity.ok(lancamentos);
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamentoDTO dto){
       try{
           Lancamento entidade = converter(dto);
           entidade = service.salvar(entidade);
           return new ResponseEntity(entidade, HttpStatus.CREATED);

       }catch (RegraDeNegocioExpection e){
            return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto){
     return service.obterPorId(id).map( entity -> {
            try {
                Lancamento lancamento = converter(dto);
                lancamento.setId(entity.getId());
                service.atualizar(lancamento);
                return ResponseEntity.ok(lancamento);
            }catch(RegraDeNegocioExpection e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet( () ->
             new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
    }

    @PutMapping("{id}/atualiza-status")
    public ResponseEntity atulizarStatus(@PathVariable("id") Long id, @RequestBody AtualizarStatusDTO dto){
        return service.obterPorId(id).map(entity -> {
            StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus());
            if (statusSelecionado == null){
                return ResponseEntity.badRequest().body("Não foi possivel atualizar o status do lançamento, envie um status valido");
            }
            try{
                entity.setStatus(statusSelecionado);
                service.atualizar(entity);
                return ResponseEntity.ok(entity);
            }catch(RegraDeNegocioExpection e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }).orElseGet( () ->
                new ResponseEntity("Lançamento não encontrado na base de dados.", HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id){
        return service.obterPorId(id).map( entidade -> {
            service.deletar(entidade);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet( () ->
                new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
    }

    private Lancamento converter(LancamentoDTO dto){
        Lancamento lancamento = new Lancamento();
        lancamento.setId(dto.getId());
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setAno(dto.getAno());
        lancamento.setMes(dto.getMes());
        lancamento.setValor(dto.getValor());

        Usuario usuario = usuarioService
                .obterPorId(dto.getUsuario())
                .orElseThrow(() -> new RegraDeNegocioExpection("Usuario não encontrado para o id informado"));

        lancamento.setUsuario(usuario);
        if (dto.getTipo() != null){
            lancamento.setTipo(TipoLançamento.valueOf(dto.getTipo()));
        }
        if (dto.getStatus() != null){
            lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
        }
        return lancamento;
    }
}
