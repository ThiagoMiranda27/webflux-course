package br.com.thiagomiranda.wefluxcourse.service;

import br.com.thiagomiranda.wefluxcourse.entity.User;
import br.com.thiagomiranda.wefluxcourse.mapper.UserMapper;
import br.com.thiagomiranda.wefluxcourse.model.request.UserRequest;
import br.com.thiagomiranda.wefluxcourse.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService service;


    @Test
    void testSave() {
        UserRequest request = new UserRequest("thiago", "thiago@email.com", "123");
        User entity = User.builder().build();

        Mockito.when(mapper.toEntity(ArgumentMatchers.any(UserRequest.class))).thenReturn(entity);
        Mockito.when(repository.save(ArgumentMatchers.any(User.class))).thenReturn(Mono.just(User.builder().build()));

        Mono<User> result = service.save(request);

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class)
                .expectComplete()
                .verify();

        Mockito.verify(repository, times(1)).save(ArgumentMatchers.any(User.class));
    }

    @Test
    void testFindById(){
        when(repository.findById(anyString())).thenReturn(Mono.just(User.builder().build()));

        Mono<User> result = service.findById("123");

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class)
                .expectComplete()
                .verify();

        Mockito.verify(repository, times(1)).findById(ArgumentMatchers.anyString());
    }

    @Test
    void testFindAll(){
        when(repository.findAll()).thenReturn(Flux.just(User.builder().build()));

        Flux<User> result = service.findAll();

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class)
                .expectComplete()
                .verify();

        Mockito.verify(repository, times(1)).findAll();
    }

}