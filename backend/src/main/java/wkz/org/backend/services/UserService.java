package wkz.org.backend.services;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Service;
import wkz.org.backend.entity.User;

import java.util.List;

@Service
public interface UserService {
    boolean login(String username, String password);

    void register(String username, String password, String email);

    Long getUserId(String username);

    User getUserById(Long userId);

    String getRoleByUserId(Long userId);

    String getUsernameByUserId(Long userId);

    void updateUser(Long userId, JSONObject data);

    List<User> getUsers(int startUserId, int userPerPage);

    void manageUser(Long userId, String role);
}
