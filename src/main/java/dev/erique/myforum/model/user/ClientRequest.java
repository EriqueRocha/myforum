package dev.erique.myforum.model.user;


import jakarta.validation.constraints.NotBlank;

public record ClientRequest(

        @NotBlank
        String name,

        @NotBlank
        String email,

        @NotBlank
        String password
) {
}
