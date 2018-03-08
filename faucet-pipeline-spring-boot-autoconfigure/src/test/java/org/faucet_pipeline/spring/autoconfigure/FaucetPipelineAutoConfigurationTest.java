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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michael J. Simons, 2018-03-05
 */
public class FaucetPipelineAutoConfigurationTest {
    static final String[] REQUIRED_PROPERTIES = {
        "spring.resources.chain.enabled = true",
        "faucet-pipeline.manifest = classpath:/fetchShouldWork.json"
    };

    static final String FAUCET_WEB_MVC_CONFIGURER_NAME = "faucetWebMvcConfigurer";
    static final String FAUCET_WEB_FLUX_CONFIGURER_NAME = "faucetWebFluxConfigurer";

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(FaucetPipelineAutoConfiguration.class));

    @Test
    public void shouldProvideManifestAndProperties() {
        contextRunner
            .withPropertyValues(REQUIRED_PROPERTIES)
            .run(ctx -> assertThat(ctx)
                .hasSingleBean(Manifest.class)
                .hasSingleBean(FaucetPipelineProperties.class)
            );
    }

    @Test
    public void shouldBeDisabledWithoutResourceChain() {
        contextRunner
            .withPropertyValues("faucet-pipeline.manifest = classpath:/fetchShouldWork.json")
            .run(ctx -> assertThat(ctx)
                .doesNotHaveBean(Manifest.class)
                .doesNotHaveBean(FaucetPipelineProperties.class)
            );
    }

    @Test
    public void shouldRequireManifest() {
        contextRunner
            .withPropertyValues("spring.resources.chain.enabled = true")
            .run(ctx -> assertThat(ctx)
                .doesNotHaveBean(Manifest.class)
                .doesNotHaveBean(FaucetPipelineProperties.class)
            );
    }

    @Test
    public void shouldRequireObjectMapperOnClasspath() {
        contextRunner
            .withClassLoader(new FilteredClassLoader(ObjectMapper.class))
            .withPropertyValues(REQUIRED_PROPERTIES)
            .run(ctx -> assertThat(ctx)
                .doesNotHaveBean(Manifest.class)
                .doesNotHaveBean(FaucetPipelineProperties.class)
            );
    }

    @Test
    public void shouldNeedWebMvcApplication() {
        contextRunner
            .withPropertyValues(REQUIRED_PROPERTIES)
            .run(ctx -> assertThat(ctx).doesNotHaveBean(FAUCET_WEB_MVC_CONFIGURER_NAME));
    }

    @Test
    public void shouldNeedWebFluxApplication() {
        contextRunner
            .withPropertyValues(REQUIRED_PROPERTIES)
            .run(ctx -> assertThat(ctx).doesNotHaveBean(FAUCET_WEB_FLUX_CONFIGURER_NAME));
    }
}