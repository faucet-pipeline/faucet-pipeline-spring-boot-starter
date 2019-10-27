/*
 * Copyright 2018-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.faucet_pipeline.spring.autoconfigure;

import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.java.Log;

/**
 * @author Michael J. Simons
 *
 * @since 2018-02-19
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnEnabledResourceChain
@ConditionalOnResource(resources = "${faucet-pipeline.manifest:classpath:/manifest.json}")
@ConditionalOnClass(ObjectMapper.class)
@EnableConfigurationProperties({ResourceProperties.class, FaucetPipelineProperties.class})
@Import({PipelineForWebMvcConfiguration.class, PipelineForWebFluxConfiguration.class})
@Log
public class FaucetPipelineAutoConfiguration {
    /**
     * Creates the manifest instance.
     *
     * @param faucetPipelineProperties
     * @return
     * @throws IOException
     */
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
