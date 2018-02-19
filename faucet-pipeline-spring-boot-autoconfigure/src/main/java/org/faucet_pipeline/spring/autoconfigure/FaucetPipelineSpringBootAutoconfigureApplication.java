package org.faucet_pipeline.spring.autoconfigure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FaucetPipelineSpringBootAutoconfigureApplication {

	public static void main(String[] args) {
		SpringApplication.run(FaucetPipelineSpringBootAutoconfigureApplication.class, args);
	}
}

/*
final Map<String, String> f = objectMapper.readValue(this.getClass().getResourceAsStream("/manifest.json"),         objectMapper.getTypeFactory().constructMapType(Map.class, String.class, String.class));

        return new WebMvcConfigurerAdapter() {


            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {

                registry.addResourceHandler("/**")
                        .addResourceLocations(p.getStaticLocations())
                        .resourceChain(false).addResolver(new FaucetResourceResolver(f));
            }


        };
    }
 */