package wkz.org.backend.controllers;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wkz.org.backend.entity.Book;
import wkz.org.backend.entity.Order;
import wkz.org.backend.entity.OrderItem;
import wkz.org.backend.services.DataService;
import wkz.org.backend.services.OrderService;
import wkz.org.backend.utils.NetworkUtil;
import wkz.org.backend.utils.SessionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
public class DataController {
    @Autowired
    OrderService orderService;
    @Autowired
    DataService dataService;

    @GetMapping("/data/purchase")
    public String purchase(@RequestParam Integer filterTime) {
        // 从session中获取userId
        Long userId = (Long) SessionUtils.getSession().getAttribute("userId");

        // 从数据库查找订单
        List<Order> orders = orderService.getOrders(userId, "", filterTime);

        // 顾客可以统计在指定时间范围内⾃⼰购买书籍的情况 ，包括每种书购
        //买了多少本，购书总本数和总⾦额
        JSONObject result = dataService.getStatistics(orders);

        return NetworkUtil.response("ok", "购买信息查询成功", result);
    }

    @GetMapping("/admin/data/sell")
    public String sell(@RequestParam Integer filterTime) {
        // 从数据库查找订单
        List<Order> orders = orderService.getOrders("", filterTime);

        // 顾客可以统计在指定时间范围内卖出书籍的情况 ，包括每种书卖出
        //了多少本，卖书总本数和总金额
        Object result = dataService.getSelling(orders);

        return NetworkUtil.response("ok", "销售信息查询成功", result);
    }

    @GetMapping("/admin/data/consume")
    public String consume(@RequestParam Integer filterTime) {
        // 从数据库查找订单
        List<Order> orders = orderService.getOrders("", filterTime);

        // 顾客可以统计在指定时间范围内消费的情况 ，包括每种书消费
        //了多少本，消费总本数和总金额
        Object result = dataService.getConsuming(orders);

        return NetworkUtil.response("ok", "消费信息查询成功", result);
    }
}
