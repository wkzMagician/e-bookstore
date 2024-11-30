package wkz.org.backend.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wkz.org.backend.entity.User;

import java.util.List;

public interface UserDao {
    User findUserById(Long userId);

    User findUserByUsername(String username);

    Number findUserIdByUsername(String username);

    String findRoleByUserId(Long userId);

    String findUsernameByUserId(Long userId);


    boolean existsUser(Long userId, String password);

    void save(String username, String password, String email);

    void update(User user);

    Page<User> findAll(Pageable pageable);
}
