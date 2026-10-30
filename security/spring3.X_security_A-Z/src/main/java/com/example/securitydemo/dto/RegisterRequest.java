package com.example.securitydemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(

	@NotBlank(message = "Le nom d'utilisateur est obligatoire")
    	@Size(min = 3, max = 50, message = "Le nom doit contenir entre 3 et 50 caractères")
    	@Pattern(
        regexp = "^[a-zA-Z0-9_-]+$",
        message = "Le nom d'utilisateur ne peut contenir que des lettres, des chiffres, des tirets et des underscores."
    	)
        String username,

        @NotBlank
        @Size(min = 8, max = 72)
        String password

) {}
