package wkz.org.backend.config;

import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.LoggingCacheErrorHandler;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CachingConfigurerImpl implements CachingConfigurer {
		@Override
		public CacheErrorHandler errorHandler() {
				return new LoggingCacheErrorHandler();
		}
}