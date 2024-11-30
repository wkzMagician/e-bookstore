package wkz.org.backend.services.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import wkz.org.backend.dao.UserDao;
import wkz.org.backend.entity.User;
import wkz.org.backend.entity.UserAuth;
import wkz.org.backend.services.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean login(String username, String password) {
        Long userId = userDao.findUserIdByUsername(username).longValue();
        if(userId == null) {
            throw new RuntimeException("用户名不存在");
        }

        return userDao.existsUser(userId, password);
    }

    @Override
    public void register(String username, String password, String email) {
        userDao.save(username, password, email);
    }

    @Override
    public Long getUserId(String username) {
        return userDao.findUserIdByUsername(username).longValue();
    }

    @Override
    public User getUserById(Long userId) {
        return userDao.findUserById(userId);
    }

    @Override
    public String getRoleByUserId(Long userId){
        return userDao.findRoleByUserId(userId);
    }

    @Override
    public String getUsernameByUserId(Long userId){
        return userDao.findUsernameByUserId(userId);
    }

    @Override
    public void updateUser(Long userId, JSONObject data) {
        String firstName = data.getString("firstname");
        String lastName = data.getString("lastname");
        String email = data.getString("email");
        String phone = data.getString("phone");
        String signature = data.getString("signature");

        User user = userDao.findUserById(userId);

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setSignature(signature);

        userDao.update(user);
    }

    @Override
    public List<User> getUsers(int startUserId, int userPerPage){
        Pageable pageable = PageRequest.of((startUserId-1) / userPerPage, userPerPage);
        return userDao.findAll(pageable).getContent();
    }

    @Override
    public void manageUser(Long userId, String role){
        User user = userDao.findUserById(userId);
        user.setRole(role);
        userDao.update(user);
    }
}
