package br.com.thiagomiranda.wefluxcourse.controller;

import br.com.thiagomiranda.wefluxcourse.entity.User;
import br.com.thiagomiranda.wefluxcourse.mapper.UserMapper;
import br.com.thiagomiranda.wefluxcourse.model.request.UserRequest;
import br.com.thiagomiranda.wefluxcourse.model.response.UserResponse;
import br.com.thiagomiranda.wefluxcourse.service.UserService;
import com.mongodb.reactivestreams.client.MongoClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static reactor.core.publisher.Mono.just;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerIplTest {

    public static final String ID = "12356";
    public static final String NAME = "Thiago";
    public static final String PASSWORD = "123";
    public static final String EMAIL = "thiago@email.com";
    public static final String BASE_URI = "/users";
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService service;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private MongoClient mongoClient;


    @Test
    @DisplayName("Test endpoint save with sucess")
    void testSaveWithSucess() {
        final var request = new UserRequest("Thiago", "thiago@email", "123");
        when(service.save(any(UserRequest.class))).thenReturn(just(User.builder().build()));

        webTestClient.post().uri(BASE_URI)
                .contentType(APPLICATION_JSON)
                .body(fromValue(request))
                .exchange()
                .expectStatus().isCreated();
        verify(service).save(any(UserRequest.class));
    }


    @Test
    @DisplayName("Test endpoint save with bad request")
    void testSaveWithBadRequest() {
        final var request = new UserRequest(NAME.concat(" "), EMAIL, PASSWORD);

        webTestClient.post().uri(BASE_URI)
                .contentType(APPLICATION_JSON)
                .body(fromValue(request))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo(BASE_URI)
                .jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
                .jsonPath("$.error").isEqualTo("Validation error")
                .jsonPath("$.message").isEqualTo("Error on validation attributes")
                .jsonPath("$.errors[0].fieldName").isEqualTo("name")
                .jsonPath("$.errors[0].message").isEqualTo("field cannot have blank spaces at the beginning or at end");
    }

    @Test
    @DisplayName("Test find by id endpoint with sucess")
    void testFindByIdWithSucess() {


        final var userResponse = new UserResponse(ID, NAME, PASSWORD, EMAIL);

        when(service.findById(anyString())).thenReturn(just(User.builder().build()));
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.get().uri(BASE_URI + "/" + "12356")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(ID)
                .jsonPath("$.name").isEqualTo(NAME)
                .jsonPath("$.password").isEqualTo(PASSWORD)
                .jsonPath("$.email").isEqualTo(EMAIL);
        verify(service).findById(anyString());
        verify(userMapper).toResponse(any(User.class));

    }

    @Test
    @DisplayName("Test find all endpoint with sucess")
    void testFindAllWithSucess() {

            final var userResponse = new UserResponse(ID, NAME, PASSWORD, EMAIL);

            when(service.findAll()).thenReturn(Flux.just(User.builder().build()));
            when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

            webTestClient.get().uri(BASE_URI)
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.[0].id").isEqualTo(ID)
                    .jsonPath("$.[0].name").isEqualTo(NAME)
                    .jsonPath("$.[0].password").isEqualTo(PASSWORD)
                    .jsonPath("$.[0].email").isEqualTo(EMAIL);
        verify(service).findAll();
        verify(userMapper).toResponse(any(User.class));
    }

    @Test
    @DisplayName("Test udpate endpoint with sucess")
    void testUpdateSucess() {

        final var userResponse = new UserResponse(ID, NAME, PASSWORD, EMAIL);
        final var request = new UserRequest(NAME, EMAIL, PASSWORD);

        when(service.update(anyString(),any(UserRequest.class))).thenReturn(just(User.builder().build()));
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.patch().uri(BASE_URI + "/" + ID)
                .contentType(APPLICATION_JSON)
                .body(fromValue(request))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(ID)
                .jsonPath("$.name").isEqualTo(NAME)
                .jsonPath("$.password").isEqualTo(PASSWORD)
                .jsonPath("$.email").isEqualTo(EMAIL);
        verify(service).update(anyString(), any(UserRequest.class));
        verify(userMapper).toResponse(any(User.class));

    }

    @Test
    @DisplayName("Test delete endpoint with sucess")
    void testDeleteSucess() {
        when(service.delete(anyString())).thenReturn(just(User.builder().build()));

        webTestClient.delete().uri(BASE_URI + "/" + ID)
                .exchange()
                .expectStatus().isOk();
        verify(service).delete(anyString());
    }
}