package wkz.org.backend.services;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Service;
import wkz.org.backend.entity.Order;
import wkz.org.backend.entity.OrderItem;

import java.util.List;

@Service
public interface OrderService {
    void createOrder(Long userId, JSONArray orderInfo);

    List<Order> getOrders(Long userId, String filterWord, Integer filterTime);

    List<Order> getOrders(String filterWord, Integer filterTime);

    void cancelOrder(Long userId, Long orderId);

    void payOrder(Long userId, Long orderId);

    void deliverOrder(Long userId, Long orderId);

    void confirmOrder(Long userId, Long orderId);

//    JSONObject toJson(OrderItem orderItem);
//
//    JSONObject toJson(Order order);
//
//    JSONArray toJson(List<Order> orders);
}
