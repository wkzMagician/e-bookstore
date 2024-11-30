package wkz.org.backend.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import wkz.org.backend.dao.UserDao;
import wkz.org.backend.entity.User;
import wkz.org.backend.entity.UserAuth;
import wkz.org.backend.repository.UserAuthRepository;
import wkz.org.backend.repository.UserRepository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAuthRepository userAuthRepository;

    @Override
    @Cacheable(value = "user", key = "#userId")
    public User findUserById(Long userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Number findUserIdByUsername(String username){
        return userRepository.findIdByUsername(username);
    }

    @Override
    public String findRoleByUserId(Long userId){
        return userRepository.findRoleByUserId(userId);
    }

    @Override
    public String findUsernameByUserId(Long userId){
        return userRepository.findUsernameByUserId(userId);
    }

    @Override
    public boolean existsUser(Long userId, String password){
        Example<UserAuth> example = Example.of(new UserAuth(userId, password));
        return userAuthRepository.exists(example);
    }

    @Override
    public void save(String username, String password, String email){
        // 判断用户名是否已存在
        if(userRepository.findByUsername(username) != null){
            throw new RuntimeException("用户名已存在");
        }

        User user = new User(username, email, password);
        userRepository.save(user);
    }

    @Override
    @CacheEvict(value = "user", key = "#user.userId")
    public void update(User user){
        userRepository.save(user);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
