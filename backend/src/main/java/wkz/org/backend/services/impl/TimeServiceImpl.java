package wkz.org.backend.services.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import wkz.org.backend.services.TimeService;

@Service
@Scope("session")
public class TimeServiceImpl implements TimeService {
		private long startTime;
		private long endTime;
		private boolean isRunning;

		@Override
		public void start() {
				if (isRunning) {
//						 throw new RuntimeException("计时器已经在运行");
				}
				startTime = System.currentTimeMillis();
				isRunning = true;
		}

		@Override
		public void stop() {
				if (!isRunning) {
						throw new RuntimeException("计时器未运行");
				}
				endTime = System.currentTimeMillis();
				isRunning = false;
		}

		@Override
		public long getTime() {
				if (isRunning) {
						return System.currentTimeMillis() - startTime;
				} else {
						return endTime - startTime;
				}
		}

}
