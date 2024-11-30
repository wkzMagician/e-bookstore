package wkz.org.backend.server;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@ServerEndpoint("/websocket/order/{userId}")
@Component
public class WebSocketServer {
		private static final ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>();

		public void sendMessage(Session session, String message) {
				if(session != null) {
						try {
								session.getBasicRemote().sendText(message);
						} catch (Exception e) {
								e.printStackTrace();
						}
				} else {
						System.out.println("session is null");
				}
		}

		public void sendMessageToUser(String userId, String message) {
				Session session = sessionMap.get(userId);
				sendMessage(session, message);
		}

		@OnMessage
		public void onMessage(String message, Session session) {
				System.out.println("收到消息: " + message);
		}

		@OnOpen
		public void onOpen(Session session, @PathParam("userId") String userId) {
				if(getSession(userId) == null) {
						addSession(userId, session);
						System.out.println("用户" + userId + "已连接");
				}
		}

		@OnClose
		public void onClose(Session session, @PathParam("userId") String userId) {
				removeSession(userId);
				System.out.println("用户" + userId + "已断开连接");
		}

		@OnError
		public void onError(Session session, Throwable error) {
				System.out.println("发生错误");
				error.printStackTrace();
		}


		public static void addSession(String userId, Session session) {
				sessionMap.put(userId, session);
		}

		public static void removeSession(String userId) {
				sessionMap.remove(userId);
		}

		public static Session getSession(String userId) {
				return sessionMap.get(userId);
		}
}
