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

import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.faucet_pipeline.spring.autoconfigure.FaucetPipelineAutoconfigurationTest.FAUCET_WEB_MVC_CONFIGURER_NAME;
import static org.faucet_pipeline.spring.autoconfigure.FaucetPipelineAutoconfigurationTest.REQUIRED_PROPERTIES;

/**
 * @author Michael J. Simons, 2018-03-05
 */
public class PipelineForWebMvcConfigurationTest {
    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(
            WebMvcAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class,
            FaucetPipelineAutoconfiguration.class));

    @Test
    public void shouldProvideFaucetWebMvcConfigurer() {
        contextRunner
            .withPropertyValues(REQUIRED_PROPERTIES)
            .run(ctx -> assertThat(ctx).hasBean(FAUCET_WEB_MVC_CONFIGURER_NAME));
    }
}