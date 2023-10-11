package br.com.thiagomiranda.wefluxcourse.service;

import br.com.thiagomiranda.wefluxcourse.entity.User;
import br.com.thiagomiranda.wefluxcourse.mapper.UserMapper;
import br.com.thiagomiranda.wefluxcourse.model.request.UserRequest;
import br.com.thiagomiranda.wefluxcourse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public Mono<User> save(final UserRequest request){
        return repository.save(mapper.toEntity(request));

    }

}
