package wkz.org.backend.services.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import wkz.org.backend.dao.OrderDao;
import wkz.org.backend.dao.OrderItemDao;
import wkz.org.backend.entity.Book;
import wkz.org.backend.entity.Order;
import wkz.org.backend.entity.OrderItem;
import wkz.org.backend.services.BookService;
import wkz.org.backend.services.CartService;
import wkz.org.backend.services.OrderService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderDao orderDao;
    @Autowired
    BookService bookService;
    @Autowired
    OrderItemDao orderItemDao;
    @Autowired
    CartService cartService;

    // 事务，保证订单和订单项同时保存
    @Transactional(propagation = Propagation.REQUIRED)
    public void createOrder(Long userId, JSONArray orderInfo) {
        // 使用jpa
        String bookStatus = "有货";
        // 当前时间
        Date date = new Date();
        Order order = new Order(userId, date);
        orderDao.save(order);
        // 计算总价
        Long totalPrice = 0L;

        //			int a = 0 / 0;

        for (int i = 0; i < orderInfo.size(); i++) {
            JSONObject orderItem = orderInfo.getJSONObject(i);
            Long bookId = orderItem.getLong("bookId");
            Long bookAmount = orderItem.getLong("bookAmount");

            // 移除购物车项
            cartService.removeBookFromCart(userId, bookId);

            OrderItem item = new OrderItem(bookId, bookAmount, bookStatus);
            // 获取book
            Book book = bookService.getBookById(bookId);
            item.setBook(book);
            item.setOrder(order);
//            order.addOrderItem(item);

            // 改为通过OrderItemDao保存
            orderItemDao.save(item);

            // 计算总价
//            totalPrice += book.getPrice() * bookAmount;

            // 向网关发送请求计算总价（微服务）
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8080//function/totalPrice";
            //  arg:  Function<Flux<Long[]>, Flux<Long>>
            Long price = Long.valueOf(book.getPrice());
            Long amount = Long.valueOf(bookAmount);
            Long[] arg = {price, amount};
            ResponseEntity<List<Long>> response = restTemplate.postForEntity(url, arg, (Class<List<Long>>)(Class<?>)List.class);
            List<Long> responseBody = response.getBody();
            if (responseBody != null && !responseBody.isEmpty()) {
                totalPrice += responseBody.get(0);
            }
        }

        order.setTotalPrice(totalPrice);
        orderDao.save(order);
    }

    @Override
    public List<Order> getOrders(Long userId, String filterWord, Integer filterTime) {
        // 获取当前时间
        Date endDate = new Date();
        // 计算开始时间
        Calendar calendar = Calendar.getInstance();
        if (filterTime != -1) {
            calendar.add(Calendar.DAY_OF_YEAR, -filterTime);
        } else {
            // 如果filterTime为-1，设置开始时间为很早的时间，以获取所有订单
            calendar.set(Calendar.YEAR, 1970);
        }
        Date startDate = calendar.getTime();

        // 获取订单
        return orderDao.findByUserIdAndBookTitleContainingAndOrderDateBetween(userId, filterWord, startDate, endDate);
    }

    @Override
    public List<Order> getOrders(String filterWord, Integer filterTime) {
        // 获取当前时间
        Date endDate = new Date();
        // 计算开始时间
        Calendar calendar = Calendar.getInstance();
        if (filterTime != -1) {
            calendar.add(Calendar.DAY_OF_YEAR, -filterTime);
        } else {
            // 如果filterTime为-1，设置开始时间为很早的时间，以获取所有订单
            calendar.set(Calendar.YEAR, 1970);
        }
        Date startDate = calendar.getTime();

        // 获取订单
        return orderDao.findByBookTitleContainingAndOrderDateBetween(filterWord, startDate, endDate);
    }

    public void cancelOrder(Long userId, Long orderId) {
        // 取消订单

    }

    public void payOrder(Long userId, Long orderId) {
        // 支付订单

    }

    public void deliverOrder(Long userId, Long orderId) {
        // 发货

    }

    public void confirmOrder(Long userId, Long orderId) {
        // 确认收货

    }

//    public JSONObject toJson(OrderItem orderItem) {
//        JSONObject result = new JSONObject();
//        result.put("bookId", orderItem.getBookId());
//        result.put("bookAmount", orderItem.getBookAmount());
//        result.put("bookStatus", orderItem.getBookStatus());
//        Book book = orderItem.getBook();
//        result.put("bookCover", book.getCover());
//        result.put("bookTitle", book.getTitle());
//        result.put("bookPrice", book.getPrice());
//        return result;
//    }
//
//    public JSONObject toJson(Order order) {
//        JSONObject result = new JSONObject();
//        result.put("orderId", order.getOrderId());
//        result.put("date", order.getOrderDate());
//        // 获取订单列表
//        List<OrderItem> orderItems = order.getOrderItems();
//        JSONArray orderItemsJson = new JSONArray();
//        for (OrderItem orderItem : orderItems) {
//            orderItemsJson.add(toJson(orderItem));
//        }
//        result.put("orderItems", orderItemsJson);
//        // 计算总价
//        double totalPrice = order.getTotalPrice();
//        result.put("totalPrice", totalPrice);
//
//        return result;
//    }
//
//    public JSONArray toJson(List<Order> orders) {
//        JSONArray result = new JSONArray();
//        for (Order order : orders) {
//            result.add(toJson(order));
//        }
//        return result;
//    }
}
