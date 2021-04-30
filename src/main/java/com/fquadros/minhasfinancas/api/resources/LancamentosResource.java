package com.fquadros.minhasfinancas.api.resources;

import com.fquadros.minhasfinancas.api.dto.LancamentoDTO;
import com.fquadros.minhasfinancas.exception.RegraDeNegocioExpection;
import com.fquadros.minhasfinancas.model.Lancamento;
import com.fquadros.minhasfinancas.model.Usuario;
import com.fquadros.minhasfinancas.model.enums.StatusLancamento;
import com.fquadros.minhasfinancas.model.enums.TipoLançamento;
import com.fquadros.minhasfinancas.service.LancamentoService;
import com.fquadros.minhasfinancas.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentosResource {

    private LancamentoService service;
    private UsuarioService usuarioService;

    public LancamentosResource(LancamentoService service) {
        this.service = service;
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
                return ResponseEntity.ok(lancamento);
            }catch(RegraDeNegocioExpection e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet( () ->
             new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST);
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
        lancamento.setTipo(TipoLançamento.valueOf(dto.getTipo()));
        lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));

        return lancamento;
    }
}
