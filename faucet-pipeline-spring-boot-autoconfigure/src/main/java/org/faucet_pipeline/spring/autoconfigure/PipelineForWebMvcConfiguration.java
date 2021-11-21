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

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

/**
 * @author Michael J. Simons
 *
 * @since 2018-03-03
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = SERVLET)
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
class PipelineForWebMvcConfiguration {

    @Bean
    WebMvcConfigurer faucetWebMvcConfigurer(
            final FaucetPipelineProperties faucetPipelineProperties,
            final WebProperties webProperties,
            final Manifest manifest
    ) {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(final ResourceHandlerRegistry registry) {
                registry.addResourceHandler(faucetPipelineProperties.getPathPatterns())
                        .addResourceLocations(webProperties.getResources().getStaticLocations())
                        .resourceChain(faucetPipelineProperties.isCacheManifest())
                            .addResolver(new ManifestBasedResourceResolver(manifest));
            }
        };
    }

    @Log
    @RequiredArgsConstructor
    static class ManifestBasedResourceResolver implements ResourceResolver {
        private final Manifest manifest;

        @Override
        public Resource resolveResource(
            final HttpServletRequest request,
            final String requestPath,
            final List<? extends Resource> locations,
            final ResourceResolverChain chain
        ) {
            log.fine(() -> String.format("Resolving resource for request path '%s'", requestPath));
            return Optional.ofNullable(chain.resolveResource(request, requestPath, locations)).orElse(null);
        }

        @Override
        public String resolveUrlPath(
            final String resourcePath,
            final List<? extends Resource> locations,
            final ResourceResolverChain chain
        ) {
            log.fine(() -> String.format("Resolving url path for resource '%s'", resourcePath));
            return this.manifest.fetch(resourcePath).orElse(null);
        }
    }
}
