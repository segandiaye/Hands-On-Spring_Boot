package com.paris13.pls.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {

    @GetMapping
    public String sayHello() {
        return "Bonjour et bienvenue sur l'application EasyNotes. Vous pouvez créer une nouvelle note en adressant une demande POST au noeud final / api / notes.";
    }
}
