package org.faucet_pipeline.demo.webmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author Michael J. Simons, 2018-02-19
 */
@SpringBootApplication
public class DemoWebmvcApplication {

    @Bean
    HandlerFunction<ServerResponse> index() {
        return request -> ServerResponse.ok().render("index");
    }
    
    @Bean
    RouterFunction<ServerResponse> routes(HandlerFunction<ServerResponse> index) {
       return RouterFunctions.route(RequestPredicates.path("/"), index);
    
    }
    
    
    
	public static void main(String[] args) {
		SpringApplication.run(DemoWebmvcApplication.class, args);
	}
}