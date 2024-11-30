package wkz.org.backend.controllers;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import wkz.org.backend.entity.Book;
import wkz.org.backend.entity.Order;
import wkz.org.backend.entity.OrderItem;
import wkz.org.backend.entity.User;
import wkz.org.backend.services.BookService;
import wkz.org.backend.services.OrderService;
import wkz.org.backend.services.UserService;
import wkz.org.backend.utils.NetworkUtil;
import wkz.org.backend.utils.SessionUtils;

import java.util.Iterator;
import java.util.List;

@RestController
public class OrderController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @Autowired
    BookService bookService;

    @RequestMapping("/order")
//    @CrossOrigin
    public String order(@RequestParam String filterWord, @RequestParam Integer filterTime) {
        // 从session中获取userId
        Long userId = (Long) SessionUtils.getSession().getAttribute("userId");

        // 从数据库查找订单
        List<Order> orders = orderService.getOrders(userId, filterWord, filterTime);

//        JSONArray ordersJson = orderService.toJson(orders);

        // 格式: {code:, message:, data:[]}
        return NetworkUtil.response("ok", "订单查询成功", orders);
    }

    @GetMapping("/admin/order")
    public String adminOrder(@RequestParam String filterWord, @RequestParam Integer filterTime) {
        // 从数据库查找订单
        List<Order> orders = orderService.getOrders(filterWord, filterTime);

        // 格式: {code:, message:, data:[]}
        return NetworkUtil.response("ok", "订单查询成功", orders);
    }


    @PostMapping("/order/add")
    public String purchase(@RequestBody JSONArray orderInfo) {
        // 从session中获取userId
        Long userId = (Long) SessionUtils.getSession().getAttribute("userId");

        // 更新库存
        for (int i = 0; i < orderInfo.size(); i++) {
            JSONObject item = orderInfo.getJSONObject(i);
            Long bookId = item.getLong("bookId");
            Integer quantity = item.getInteger("bookAmount");

            try{
                bookService.checkBookInventory(bookId, quantity);
            } catch (Exception e) {
                return NetworkUtil.response("error", "库存不足");
            }
        }

        bookService.updateInventory(orderInfo);

        // 创建订单
        orderService.createOrder(userId, orderInfo);

        // 格式: {"code": "ok", "message": "购买成功"}
        return NetworkUtil.response("ok", "购买成功");
    }

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/order/add/kafka")
    public String purchaseWithKafka(@RequestBody JSONArray orderInfo) {
        // 从session中获取userId
        Long userId = (Long) SessionUtils.getSession().getAttribute("userId");

        // 创建订单消息
        JSONObject orderMessage = new JSONObject();
        orderMessage.put("userId", userId);
        orderMessage.put("orderInfo", orderInfo);

        System.out.println("已接收下单请求，发送到Kafka");

        // 将订单消息发送到Kafka
        kafkaTemplate.send("orderTopic", orderMessage.toJSONString());

        // 返回响应
        return NetworkUtil.response("ok", "订单已发送到Kafka");
    }
}
