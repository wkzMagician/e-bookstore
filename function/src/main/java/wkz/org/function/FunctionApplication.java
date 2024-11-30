package wkz.org.function;

import java.util.function.Function;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

@SpringBootApplication
@EnableDiscoveryClient
public class FunctionApplication {
	@Bean
	//给定单价与数量，计算总价
	public Function<Flux<Long[]>, Flux<Long>> totalPrice() {
		return flux -> flux.map(value -> value[0] * value[1]);
	}

	public static void main(String[] args) {
		SpringApplication.run(FunctionApplication.class, args);
	}

}
