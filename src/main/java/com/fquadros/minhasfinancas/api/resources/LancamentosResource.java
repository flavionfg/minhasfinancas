package com.fquadros.minhasfinancas.api.resources;

import com.fquadros.minhasfinancas.api.dto.LancamentoDTO;
import com.fquadros.minhasfinancas.service.LancamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentosResource {

    private LancamentoService service;

    public LancamentosResource(LancamentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamentoDTO dto){

        return null;
    }
}
