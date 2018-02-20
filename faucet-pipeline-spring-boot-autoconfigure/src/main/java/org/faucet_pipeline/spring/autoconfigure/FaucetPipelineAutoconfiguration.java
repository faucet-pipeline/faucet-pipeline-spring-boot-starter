package org.faucet_pipeline.spring.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * @author Michael J. Simons, 2018-02-19
 */
@Configuration
@Conditional(AtLeastOneManifestExistsCondition.class)
@ConditionalOnClass(ObjectMapper.class)
@EnableConfigurationProperties(FaucetPipelineProperties.class)
public class FaucetPipelineAutoconfiguration {

    @Bean
    public FaucetManifestHolder faucetManifestHolder(final FaucetPipelineProperties faucetPipelineProperties) {
        for(Resource r : faucetPipelineProperties.getManifests())
            System.out.println("r " + r);
        return new FaucetManifestHolder();
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