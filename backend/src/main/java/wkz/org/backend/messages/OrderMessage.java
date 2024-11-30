package wkz.org.backend.messages;

import lombok.Data;
import org.apache.kafka.common.protocol.types.Field.Str;

@Data
public class OrderMessage {
	String username;
	boolean success;
	String message;

	public OrderMessage(String username, boolean success, String message) {
		this.username = username;
		this.success = success;
		this.message = message;
	}

	@Override
	public String toString() {
		// JSON格式化
		return "{\"username\":\"" + username + "\",\"success\":" + success + ",\"message\":\"" + message + "\"}";
	}
}
