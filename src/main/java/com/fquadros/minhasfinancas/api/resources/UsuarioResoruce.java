package com.fquadros.minhasfinancas.api.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioResoruce {

    @GetMapping("/")
    public String helloWorld(){
        return "hello world!";
    }

}
