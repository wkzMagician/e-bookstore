package wkz.org.backend.controllers;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wkz.org.backend.entity.Book;
import wkz.org.backend.entity.CartItem;
import wkz.org.backend.services.BookService;
import wkz.org.backend.services.CartService;
import wkz.org.backend.services.UserService;
import wkz.org.backend.utils.NetworkUtil;
import wkz.org.backend.utils.SessionUtils;

import java.util.Iterator;
import java.util.List;

@RestController
public class CartController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CartService cartService;

    @Autowired
    UserService userService;

    @RequestMapping("/cart/delete")
    public String delete(@RequestBody JSONObject data) {
        // 从session中获取userId
        Long userId = (Long)SessionUtils.getSession().getAttribute("userId");

        Long bookId = data.getLong("bookId");

        // 删除订单信息
        cartService.removeBookFromCart(Long.valueOf(userId), bookId);

        // 格式: {code:, message:, data:[]}
        return NetworkUtil.response("ok", "购物车删除成功");
    }

    @RequestMapping("/cart/deleteMulti")
    public String deleteMulti(@RequestBody JSONArray data) {
        // 从session中获取userId
        Long userId = (Long)SessionUtils.getSession().getAttribute("userId");

        // 删除订单信息
        cartService.removeBooksFromCart(userId, data);

        // 格式: {code:, message:, data:[]}
        return NetworkUtil.response("ok", "购物车多项删除成功");
    }

    @RequestMapping("/cart/modify")
    public String modify(@RequestBody JSONObject data) {
        Long bookId = data.getLong("bookId");
        Long bookAmount = data.getLong("bookAmount");

        // 从session中获取userId
        Long userId = (Long)SessionUtils.getSession().getAttribute("userId");

        // 修改订单信息
        cartService.updateBookAmount(userId, bookId, bookAmount);

        // 格式: {code:, message:, data:[]}
        return NetworkUtil.response("ok", "购物车修改成功");
    }

    @RequestMapping("/cart/add")
    public String add(@RequestBody JSONObject data) {
        Long bookId = data.getLong("bookId");
        Long bookAmount = data.getLong("bookAmount");

        // 从session中获取userId
        Long userId = (Long)SessionUtils.getSession().getAttribute("userId");

        // 添加订单信息
        cartService.addBookToCart(userId, bookId, bookAmount);

        // 格式: {code:, message:, data:[]}
        return NetworkUtil.response("ok", "购物车添加成功");
    }


    @RequestMapping("/cart")
//    @CrossOrigin
    public String cart() {
        // 从session中获取userId
        Long userId = (Long)SessionUtils.getSession().getAttribute("userId");

        // 获取购物车信息
        List<CartItem> cart = cartService.getCartItems(userId);

        JSONArray cartJson = cartService.toJson(cart);

        // 格式: {code:, message:, data:[]}
        return NetworkUtil.response("ok", "购物车查询成功", cartJson);
    }
}

