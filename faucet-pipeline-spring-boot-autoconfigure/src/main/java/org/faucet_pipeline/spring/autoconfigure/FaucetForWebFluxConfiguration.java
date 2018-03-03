package org.faucet_pipeline.spring.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.web.reactive.resource.ResourceResolver;
import org.springframework.web.reactive.resource.ResourceResolverChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.REACTIVE;

@ConditionalOnWebApplication(type = REACTIVE)
@AutoConfigureBefore(WebFluxAutoConfiguration.class)
class FaucetForWebFluxConfiguration {

    static class ReactiveFaucetResourceResolver implements ResourceResolver {

        @Override
        public Mono<Resource> resolveResource(ServerWebExchange serverWebExchange, String s, List<? extends Resource> list, ResourceResolverChain resourceResolverChain) {
            return null;
        }

        @Override
        public Mono<String> resolveUrlPath(String s, List<? extends Resource> list, ResourceResolverChain resourceResolverChain) {
            return null;
        }
    }
}
