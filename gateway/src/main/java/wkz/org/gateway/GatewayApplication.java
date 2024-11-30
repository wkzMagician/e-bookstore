package wkz.org.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(r -> r.path("/title2author/**")
						.filters(f -> f.rewritePath("/title2author",""))
						.uri("lb://BOOKTITLE2AUTHOR")
				)
				.route(r -> r.path("/function/**")
						.filters(f -> f.rewritePath("/function",""))
						.uri("lb://FUNCTION")
				)
				.route(r -> r.path("/**")
						.uri("lb://BACKEND")
				)
				.build();
	}
}
