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

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michael J. Simons
 *
 * @since 2018-03-03
 */
class ManifestTest {
   
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Test
    void fetchShouldWork() {
        final Manifest manifest = new Manifest(objectMapper, new ClassPathResource("fetchShouldWork.json"));        
        assertThat(manifest.fetch("bums")).isEmpty();
        assertThat(manifest.fetch("app.js")).contains("app-723a7d7a249d998465d650e19bdca289.js");
        assertThat(manifest.fetch("app2.js")).contains("app2.js");
    }
    
    @Test
    void fetchShouldHandleBrokenManifest() {
        final Manifest manifest = new Manifest(objectMapper, new ClassPathResource("broken.json"));        
        assertThat(manifest.fetch("app.js")).isEmpty();
    }
}
