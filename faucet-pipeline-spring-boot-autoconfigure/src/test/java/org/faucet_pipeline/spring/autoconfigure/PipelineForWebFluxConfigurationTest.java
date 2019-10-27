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

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.resource.ResourceUrlProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.faucet_pipeline.spring.autoconfigure.FaucetPipelineAutoConfigurationTest.FAUCET_WEB_FLUX_CONFIGURER_NAME;
import static org.faucet_pipeline.spring.autoconfigure.FaucetPipelineAutoConfigurationTest.REQUIRED_PROPERTIES;

/**
 * @author Michael J. Simons,
 *
 * @since 2018-03-05
 */
class PipelineForWebFluxConfigurationTest {
    private final ReactiveWebApplicationContextRunner contextRunner = new ReactiveWebApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(FaucetPipelineAutoConfiguration.class));

    @Test
    void shouldNotReplaceExistingResourceUrlProvider() {
        contextRunner
            .withPropertyValues(REQUIRED_PROPERTIES)
            .withUserConfiguration(ExistingResourceUrlProvider.class)
            .run(ctx -> assertThat(ctx).hasSingleBean(ResourceUrlProvider.class));
    }

    @Test
    void shouldProvideNeededBeans() {
        contextRunner
            .withPropertyValues(REQUIRED_PROPERTIES)
            .run(ctx -> assertThat(ctx)
                .hasBean("resourceUrlProvider")
                .hasBean("urlTransformingFilter")
                .hasBean(FAUCET_WEB_FLUX_CONFIGURER_NAME));
    }

    @Configuration(proxyBeanMethods = false)
    static class ExistingResourceUrlProvider {
        @Bean
        public ResourceUrlProvider defaultResourceUrlProvider() {
            return new ResourceUrlProvider();
        }
    }
}