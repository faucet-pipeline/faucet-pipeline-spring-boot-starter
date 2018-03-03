/*
 * Copyright 2018 the original author or authors.
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

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.web.reactive.resource.ResourceResolver;
import org.springframework.web.reactive.resource.ResourceResolverChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.resource.ResourceUrlProvider;
import org.springframework.web.server.WebFilter;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.REACTIVE;

@Configuration
@ConditionalOnWebApplication(type = REACTIVE)
@AutoConfigureBefore(WebFluxAutoConfiguration.class)
class PipelineForWebFluxConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    ResourceUrlProvider resourceUrlProvider() {
        return new ResourceUrlProvider();
    }

    @Bean
    WebFilter urlTransformingFilter(final ResourceUrlProvider resourceUrlProvider) {
        return (exchange, chain) -> {
            exchange.addUrlTransformer(s -> resourceUrlProvider.getForUriString(s, exchange).block());
            return chain.filter(exchange);
        };
    }
    
    @Bean
    WebFluxConfigurer faucetWebFluxConfigurer(
            final FaucetPipelineProperties faucetPipelineProperties, 
            final ResourceProperties resourceProperties, 
            final Manifest manifest
    ) {        
        return new WebFluxConfigurer() {
            @Override
            public void addResourceHandlers(final ResourceHandlerRegistry registry) {
                registry.addResourceHandler(faucetPipelineProperties.getPathPatterns())
                        .addResourceLocations(resourceProperties.getStaticLocations())
                        .resourceChain(faucetPipelineProperties.isCacheManifest())
                            .addResolver(new ReactiveManifestBasedResourceResolver(manifest));
            }
        };       
    }

    @Log
    @RequiredArgsConstructor
    static class ReactiveManifestBasedResourceResolver implements ResourceResolver {
        private final Manifest manifest;

        @Override
        public Mono<Resource> resolveResource(ServerWebExchange serverWebExchange, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
            log.fine(() -> String.format("Resolving resource for request path '%s'", requestPath));
            return chain.resolveResource(serverWebExchange, requestPath, locations);
        }

        @Override
        public Mono<String> resolveUrlPath(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
            log.fine(() -> String.format("Resolving url path for resource '%s'", resourcePath));            
            return Mono.justOrEmpty(this.manifest.fetch(resourcePath));
        }
    }
}
