package com.fquadros.minhasfinancas.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Data
@Table( name = "usuario" , schema = "financas")
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @Column(name = "id")
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "email")
    private String email;

    @Column(name = "senha")
    private String senha;

//    Testando o método Builder!
//
//    public static void main(String [] args){
//
//        Usuario.builder().nome("Flavio").email("flavionfg@gmail.com").senha("123456").build();
//    }

}
