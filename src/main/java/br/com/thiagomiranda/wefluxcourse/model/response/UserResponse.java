package br.com.thiagomiranda.wefluxcourse.model.response;

public record UserResponse(
        String id,
        String name,
        String password,
        String email
) { }