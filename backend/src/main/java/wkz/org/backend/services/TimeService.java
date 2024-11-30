package wkz.org.backend.services;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
public interface TimeService { // 计时器
		void start(); // 开始计时

		void stop(); // 停止计时

		long getTime(); // 获取计时时间

}
