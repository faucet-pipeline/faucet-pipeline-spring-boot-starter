package org.faucet_pipeline.spring.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.util.Arrays;
import lombok.extern.java.Log;

import static java.util.stream.Collectors.joining;

/**
 * @author Michael J. Simons, 2018-02-19
 */
@Configuration
@ConditionalOnProperty(value = "spring.resources.chain.enabled", havingValue = "true")
@ConditionalOnResource(resources = "${faucet-pipeline.manifest:classpath:/manifest.json}")
@ConditionalOnClass(ObjectMapper.class)
@EnableConfigurationProperties(FaucetPipelineProperties.class)
@Import({FaucetForWebMvcConfiguration.class, FaucetForWebFluxConfiguration.class})
@Log
public class FaucetPipelineAutoconfiguration {
    @Bean
    Manifest faucetManifest(final FaucetPipelineProperties faucetPipelineProperties) throws IOException {    
        log.fine(() -> String.format("Configuring faucet pipeline for manifest from %s for paths '%s'", 
                faucetPipelineProperties.getManifest(), Arrays.stream(faucetPipelineProperties.getPathPatterns()).collect(joining(", "))));
        log.fine(() -> String.format("Manifest will %sbe cached", faucetPipelineProperties.isCacheManifest() ? "" : "not "));
        
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        return new Manifest(objectMapper, faucetPipelineProperties.getManifest());
    }
}