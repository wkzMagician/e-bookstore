package wkz.org.backend.controllers;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wkz.org.backend.entity.User;
import wkz.org.backend.services.UserService;
import wkz.org.backend.utils.NetworkUtil;
import wkz.org.backend.utils.SessionUtils;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public String user() {
        try {
            // 从session中获取userId
            Long userId = (Long) SessionUtils.getSession().getAttribute("userId");

            // 从数据库查找用户信息
            User user = userService.getUserById(userId);

            // 格式: {code:, message:, data:[]}
            return NetworkUtil.response("ok", "用户信息查询成功", user);
        } catch (Exception e) {
            return NetworkUtil.response("error", "用户信息查询失败");
        }
    }

    // 修改用户信息
    @PostMapping("/user/update")
    public String update(@RequestBody JSONObject data) {
        try {
            // 从session中获取userId
            Long userId = (Long) SessionUtils.getSession().getAttribute("userId");

            // 修改用户信息
            userService.updateUser(userId, data);

            // 格式: {code:, message:, data:[]}
            return NetworkUtil.response("ok", "用户信息修改成功");
        } catch (Exception e) {
            return NetworkUtil.response("error", "用户信息修改失败");
        }
    }

    @GetMapping("/admin/user")
    public String adminUser(@RequestParam int startUserId, @RequestParam int userPerPage) {
        try {
            // 从数据库查找用户信息
            List<User> users = new ArrayList<>(userService.getUsers(startUserId, userPerPage));

            // 去掉自己
            Long userId = (Long) SessionUtils.getSession().getAttribute("userId");
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getUserId().equals(userId)) {
                    users.remove(i);
                    break;
                }
            }

            // 格式: {code:, message:, data:[]}
            return NetworkUtil.response("ok", "用户信息查询成功", users);
        } catch (Exception e) {
            return NetworkUtil.response("error", "用户信息查询失败");
        }
    }

    @PostMapping("/admin/user/manage")
    public String manage(@RequestBody JSONObject data) {
        try {
            // 从data中获取userId
            Long userId = data.getLong("userId");
            String role = data.getString("role");

            // 修改用户信息
            userService.manageUser(userId, role);

            // 格式: {code:, message:, data:[]}
            return NetworkUtil.response("ok", "用户信息修改成功");
        } catch (Exception e) {
            return NetworkUtil.response("error", "用户信息修改失败");
        }
    }
}
