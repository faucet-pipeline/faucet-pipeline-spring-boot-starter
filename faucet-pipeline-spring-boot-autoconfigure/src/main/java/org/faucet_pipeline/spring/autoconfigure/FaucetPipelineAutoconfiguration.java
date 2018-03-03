package org.faucet_pipeline.spring.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.util.Map;

/**
 * @author Michael J. Simons, 2018-02-19
 */
@Configuration
@AutoConfigureBefore(JacksonAutoConfiguration.class)
@ConditionalOnProperty(value = "spring.resources.chain.enabled", havingValue = "true")
@ConditionalOnResource(resources = "${faucet-pipeline.manifest:classpath:/manifest.json}")
@ConditionalOnClass(ObjectMapper.class)
@EnableConfigurationProperties(FaucetPipelineProperties.class)
@Import({FaucetForWebMvcConfiguration.class, FaucetForWebFluxConfiguration.class})
public class FaucetPipelineAutoconfiguration {

    @Bean
    public Manifest faucetManifestHolder(
        final ObjectMapper objectMapper,
        final FaucetPipelineProperties faucetPipelineProperties
    ) throws IOException {
        final Map<String, String> values = objectMapper.readValue(faucetPipelineProperties.getManifest().getInputStream(),
            objectMapper.getTypeFactory().constructMapType(Map.class, String.class, String.class));

        return new Manifest(values);
    }
}