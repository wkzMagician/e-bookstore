package wkz.org.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wkz.org.backend.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(Long userId);

    User findByUsername(String username);

    @Query("SELECT u.userId FROM User u WHERE u.username = :username")
    Long findIdByUsername(@Param("username") String username);

    @Query("SELECT u.role FROM User u WHERE u.userId = :userId")
    String findRoleByUserId(Long userId);

    @Query("SELECT u.username FROM User u WHERE u.userId = :userId")
    String findUsernameByUserId(Long userId);

    Page<User> findAll(Pageable pageable);
}
