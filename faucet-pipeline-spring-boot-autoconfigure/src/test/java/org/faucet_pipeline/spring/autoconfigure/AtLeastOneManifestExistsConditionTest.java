/*
 * Copyright 2018 faucet-pipeline.org.
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
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michael J. Simons, 2018-02-20
 */
public class AtLeastOneManifestExistsConditionTest {

    ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    public void shouldUseDefaultLocationIfExists() {
        contextRunner
                .withUserConfiguration(DefaultConfig.class)
                .run(context -> {
                    assertThat(context).hasBean("aBean");
                });
    }
    
    @Test
    public void shouldBeContentWithAtLeastOneManifest() {
        contextRunner
                .withUserConfiguration(DefaultConfig.class)
                .withPropertyValues("faucet-pipeline.manifests = foo.json, classpath:stylesheets.json, bar.json")
                .run(context -> {
                    assertThat(context).hasBean("aBean");
                });
    }
    
     @Test
    public void shouldNeedAtLeastOneManifest() {
        contextRunner
                .withUserConfiguration(DefaultConfig.class)
                .withPropertyValues("faucet-pipeline.manifests = foo.json, bar.json")
                .run(context -> {
                    assertThat(context).doesNotHaveBean("aBean");
                });
    }

    @Configuration
    static class DefaultConfig {

        @Bean
        @Conditional(AtLeastOneManifestExistsCondition.class)
        public Object aBean() {
            return new Object();
        }
    }
}