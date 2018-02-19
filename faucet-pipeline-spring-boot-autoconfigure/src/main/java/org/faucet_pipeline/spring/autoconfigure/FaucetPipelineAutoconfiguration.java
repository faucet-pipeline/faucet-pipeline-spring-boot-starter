package org.faucet_pipeline.spring.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Michael J. Simons, 2018-02-19
 */
@Configuration
@EnableConfigurationProperties(FaucetPipelineAutoconfiguration.class)
public class FaucetPipelineAutoconfiguration {
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