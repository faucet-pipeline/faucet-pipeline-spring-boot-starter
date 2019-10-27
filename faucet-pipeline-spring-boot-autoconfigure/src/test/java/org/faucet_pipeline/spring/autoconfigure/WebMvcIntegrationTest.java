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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

/**
 * @author Michael J. Simons
 *
 * @since 2018-03-05
 */
@WebMvcTest
@ImportAutoConfiguration(FaucetPipelineAutoConfiguration.class)
@TestPropertySource("/application-test.properties")
class WebMvcIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void linksShouldBeResolved() throws Exception {
        this.mvc.perform(get("/"))
            .andExpect(xpath("//head/link/@href").string("/stylesheets/app-fbbe8f7da2ec00caa4f3c6400bedfa17.css"))
            .andExpect(xpath("//head/script/@src").string("/app-723a7d7a249d998465d650e19bdca289.js"));
    }

    @Test
    void resourcesShouldBeResolvedThroughChain() throws Exception {
        this.mvc.perform(get("/example.css"))
            .andExpect(content().string("/* Empty. */"));
    }

    @Configuration(proxyBeanMethods = false)
    static class TestConfiguration {

        @Bean
        WebMvcConfigurer webMvcConfigurer() {
            return new WebMvcConfigurer() {
                @Override
                public void addViewControllers(ViewControllerRegistry registry) {
                    registry.addViewController("/").setViewName("index");
                }
            };
        }
    }
}
