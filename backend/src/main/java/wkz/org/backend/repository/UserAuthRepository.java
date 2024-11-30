package wkz.org.backend.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import wkz.org.backend.entity.UserAuth;

public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {
    @Override
    <S extends UserAuth> boolean exists(Example<S> example);
}
