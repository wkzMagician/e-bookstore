package wkz.org.backend.controllers;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import wkz.org.backend.services.TimeService;
import wkz.org.backend.services.UserService;
import wkz.org.backend.utils.NetworkUtil;
import wkz.org.backend.utils.SessionUtils;

@RestController
@Scope("session")
public class LoginController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    UserService userService;

    @Autowired
    WebApplicationContext applicationContext;

    TimeService timeService;

    @PostMapping("/login")
//    @CrossOrigin
    public String login(@RequestBody JSONObject data) {
        String username = data.getString("username");
        String password = data.getString("password");

        try {
            // 检查用户名和密码是否正确
            if(userService.login(username, password)) {
                Long userId = userService.getUserId(username);

                // 返回用户权限
                String role = userService.getRoleByUserId(userId);
                if("FORBID".equals(role)) {
                    return NetworkUtil.response("error", "您的账号已经被禁用");
                }

                // 设置session
                SessionUtils.setSession(userId);

                // 设置计时器
                timeService = applicationContext.getBean(TimeService.class);

                timeService.start();

                return NetworkUtil.response("ok", "登录成功", role);
            } else {
                return NetworkUtil.response("error_password", "密码错误");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return NetworkUtil.response("error", "用户名不存在");
        }
    }

    @PostMapping("/logout")
    public String logout() {
        SessionUtils.removeSession();

        // 返回登录时间
        timeService.stop();
        long time = timeService.getTime();

        return NetworkUtil.response("ok", "登出成功", time);
    }

    @PostMapping("/register")
    public String register(@RequestBody JSONObject data) {
        String username = data.getString("username");
        String password = data.getString("password");
        String email = data.getString("email");

        // 检查邮箱格式
        if(!email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
            return NetworkUtil.response("error", "邮箱格式错误");
        }

        try {
            // 检查用户名是否已经存在
            userService.register(username, password, email);
            return NetworkUtil.response("ok", "注册成功");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return NetworkUtil.response("error", "用户名已存在");
        }
    }
}

