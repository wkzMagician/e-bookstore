package wkz.org.backend.services.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wkz.org.backend.entity.Book;
import wkz.org.backend.entity.Order;
import wkz.org.backend.entity.OrderItem;
import wkz.org.backend.entity.User;
import wkz.org.backend.services.DataService;
import wkz.org.backend.services.UserService;

import java.util.*;

@Service
public class DataServiceImpl implements DataService {
    @Autowired
    UserService userService;

    @Override
    public JSONObject getStatistics(List<Order> orders) {
        // 购买的书以及数量
        HashMap<Long, Pair<String, Long>> bookCount = new HashMap<>();
        Long totalBooks = 0L;
        Long totalPrice = 0L;
        for(Order order: orders){
            List<OrderItem> orderItems = order.getOrderItems();
            for(OrderItem orderItem: orderItems){
                // 获取book
                Book book = orderItem.getBook();
                Long bookId = book.getId();
                String title = book.getTitle();

                Long count = orderItem.getBookAmount();

                if(bookCount.containsKey(bookId)){
                    Pair<String, Long> pair = bookCount.get(bookId);
                    bookCount.put(bookId, new Pair<>(title, pair.b + count));
                } else {
                    bookCount.put(bookId, new Pair<>(title, count));
                }

                totalBooks += count;
                totalPrice += book.getPrice() * count;
            }
        }

        // 使用Stream API对HashMap进行排序并直接转换为JSONArray
        JSONArray bookCountJson = bookCount.entrySet().stream()
                .sorted(Map.Entry.<Long, Pair<String, Long>>comparingByValue((o1, o2) -> o2.b.compareTo(o1.b))
                        .thenComparing(Map.Entry.comparingByKey()))
                .map(entry -> {
                    JSONObject obj = new JSONObject();
                    obj.put("bookId", entry.getKey());
                    obj.put("title", entry.getValue().a);
                    obj.put("count", entry.getValue().b);
                    return obj;
                })
                .collect(JSONArray::new, JSONArray::add, JSONArray::addAll);

        // 返回结果
        // json格式
        // {
        //     "bookCount": {
        //         "title1": count1,
        //         "title2": count2,
        //         ...
        //     },
        //     "totalBooks": totalBooks,
        //     "totalPrice": totalPrice
        // }

        JSONObject result = new JSONObject();
        result.put("totalBooks", totalBooks);
        result.put("totalPrice", totalPrice);
        result.put("bookCount", bookCountJson);
        return result;
    }

    @Override
    public Object getSelling(List<Order> orders) {
        // 卖出的书以及数量
        HashMap<Long, Pair<String, Long>> sellingCount = new HashMap<>();
        for(Order order: orders){
            List<OrderItem> orderItems = order.getOrderItems();
            for(OrderItem orderItem: orderItems){
                // 获取book
                Book book = orderItem.getBook();
                Long bookId = book.getId();
                String title = book.getTitle();
                Long count = orderItem.getBookAmount();

                if(sellingCount.containsKey(bookId)){
                    Pair<String, Long> pair = sellingCount.get(bookId);
                    sellingCount.put(bookId, new Pair<>(title, pair.b + count));
                } else {
                    sellingCount.put(bookId, new Pair<>(title, count));
                }
            }
        }

        // 使用Stream API对HashMap进行排序并直接转换为JSONArray
        JSONArray sellingCountJson = sellingCount.entrySet().stream()
                .sorted(Map.Entry.<Long, Pair<String, Long>>comparingByValue((o1, o2) -> o2.b.compareTo(o1.b))
                        .thenComparing(Map.Entry.comparingByKey()))
                .map(entry -> {
                    JSONObject obj = new JSONObject();
                    obj.put("bookId", entry.getKey());
                    obj.put("title", entry.getValue().a);
                    obj.put("count", entry.getValue().b);
                    return obj;
                })
                .collect(JSONArray::new, JSONArray::add, JSONArray::addAll);

        // 返回结果
        return sellingCountJson;
    }

    @Override
    public Object getConsuming(List<Order> orders) {
        // 统计在指定时间范围内每个⽤户的累计消费情况
        // 消费的金额
        HashMap<Long, Pair<String, Long>> consuming = new HashMap<>();
        for(Order order: orders){
            Long userId = order.getUserId();
            Long price = order.getTotalPrice();
            User user = userService.getUserById(userId);
            String username = user.getUsername();
            if(consuming.containsKey(userId)){
                Pair<String, Long> pair = consuming.get(userId);
                consuming.put(userId, new Pair<>(username, pair.b + price));
            } else {
                consuming.put(userId, new Pair<>(username, price));
            }
        }

        // 使用Stream API对HashMap进行排序并直接转换为JSONArray
        JSONArray consumingList = consuming.entrySet().stream()
                .sorted(Map.Entry.<Long, Pair<String, Long>>comparingByValue((o1, o2) -> o2.b.compareTo(o1.b))
                        .thenComparing(Map.Entry.comparingByKey()))
                .map(entry -> {
                    JSONObject obj = new JSONObject();
                    obj.put("userId", entry.getKey());
                    obj.put("username", entry.getValue().a);
                    obj.put("totalPrice", entry.getValue().b);
                    return obj;
                })
                .collect(JSONArray::new, JSONArray::add, JSONArray::addAll);

        // 返回结果
        return consumingList;
    }
}
