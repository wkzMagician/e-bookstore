package wkz.org.backend.listeners;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import wkz.org.backend.messages.OrderMessage;
import wkz.org.backend.server.WebSocketServer;
import wkz.org.backend.services.BookService;
import wkz.org.backend.services.OrderService;
import wkz.org.backend.services.UserService;
import wkz.org.backend.utils.NetworkUtil;

@Component
public class OrderListener {

	@Autowired
	private OrderService orderService;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private BookService bookService;

	@Autowired
	private WebSocketServer webSocketServer;

	@Autowired
	private UserService userService;

	@KafkaListener(topics = "orderTopic", groupId = "orderGroup")
	public void listenOrderTopic(String message) {
		System.out.println("Kafka已经接收到订单消息: " + message);

		// 解析订单消息
		JSONObject orderMessage = JSON.parseObject(message);
		Long userId = orderMessage.getLong("userId");
		JSONArray orderInfo = orderMessage.getJSONArray("orderInfo");

		// 通过userId获取用户名
		String username = userService.getUsernameByUserId(userId);

		for (int i = 0; i < orderInfo.size(); i++) {
			JSONObject item = orderInfo.getJSONObject(i);
			Long bookId = item.getLong("bookId");
			Integer quantity = item.getInteger("bookAmount");

			try{
				bookService.checkBookInventory(bookId, quantity);
			} catch (Exception e) {
				// 将处理结果发送到另一个Topic
//				kafkaTemplate.send("orderResultTopic", "库存不足");
				kafkaTemplate.send("orderResultTopic", new OrderMessage(username, false, "库存不足").toString());
				return;
			}
		}

		// 处理订单
		try {
			orderService.createOrder(userId, orderInfo);
			// 将处理结果发送到另一个Topic
//			kafkaTemplate.send("orderResultTopic", "订单处理成功");
			kafkaTemplate.send("orderResultTopic", new OrderMessage(username, true, "订单处理成功").toString());
		} catch (Exception e) {
			// 将处理结果发送到另一个Topic
//			kafkaTemplate.send("orderResultTopic", "订单处理失败：" + e.getMessage());
			kafkaTemplate.send("orderResultTopic", new OrderMessage(username, false, "订单处理失败" + e.getMessage()).toString());

		}
	}

	@KafkaListener(topics = "orderResultTopic", groupId = "orderResultGroup")
	public void listenOrderResultTopic(String message) {
		System.out.println("Order Result: " + message);

		// 读取消息
		OrderMessage orderMessage = JSON.parseObject(message, OrderMessage.class);
		String username = orderMessage.getUsername();

		// 将结果通过WebSocket发送给前端
		webSocketServer.sendMessageToUser(username, message);
	}
}
