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
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * @author Michael J. Simons, 2018-03-05
 */
@RunWith(SpringRunner.class)
@WebFluxTest
@ImportAutoConfiguration({ThymeleafAutoConfiguration.class, FaucetPipelineAutoConfiguration.class})
@TestPropertySource("/application-test.properties")
public class WebFluxIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void linksShouldBeResolved() {
        webTestClient.get().uri("/").exchange().expectBody()
            .consumeWith(bytes -> {
                final String content = new String(bytes.getResponseBodyContent());
                assertThat(content)
                    .contains("/stylesheets/app-fbbe8f7da2ec00caa4f3c6400bedfa17.css")
                    .contains("/app-723a7d7a249d998465d650e19bdca289.js");
            });
    }

    @Test
    public void resourcesShouldBeResolvedThroughChain() {
        webTestClient.get().uri("/example.css").exchange().expectBody()
            .consumeWith(bytes -> {
                final String content = new String(bytes.getResponseBodyContent());
                assertThat(content).contains("/* Empty. */");
            });
    }

    @Configuration
    static class TestConfiguration {
        @Bean
        RouterFunction<?> router() {
            return route(GET("/"), request -> ok().render("index"));
        }
    }
}
