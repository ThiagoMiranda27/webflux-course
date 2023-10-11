package br.com.thiagomiranda.wefluxcourse.model.request;

public record UserRequest(

        String name,
        String email,
        String password
) { }
