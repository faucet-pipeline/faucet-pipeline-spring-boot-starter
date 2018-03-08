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

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;

import lombok.extern.java.Log;


/**
 * Represents a view on the faucet manifest. The manifest is read everytime a
 * resource is to be resolved, so therefore it is paramount to cache the
 * resources in the resource chain in production.
 *
 * @author Michael J. Simons, 2018-02-20
 */
@Log
class Manifest {

    private final ObjectMapper objectMapper;

    private final MapType mapType;

    private final Resource manifest;

    Manifest(final ObjectMapper objectMapper, final Resource manifest) {
        this.objectMapper = objectMapper;
        this.mapType = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, String.class);
        this.manifest = manifest;
    }

    Optional<String> fetch(final String assetName) {
        log.fine(() -> String.format("Fetching asset '%s'", assetName));

        Optional<String> rv;
        try {
            final Map<String, String> values = getValues();
            rv = Optional.ofNullable(values.get(assetName)).map(a -> a.replaceFirst("^/", ""));
        } catch (IOException e) {
            log.log(Level.WARNING, e, () -> String.format("Could not load manifest from %s", manifest));
            rv = Optional.empty();
        }

        if (!rv.isPresent()) {
            log.warning(() -> String.format("The asset '%s' was not in the manifest", assetName));
        }

        return rv;
    }

    Map<String, String> getValues() throws IOException {
        final Map<String, String> values = objectMapper.readValue(manifest.getInputStream(), mapType);
        if (log.isLoggable(Level.FINE)) {
            try {
                final String json = this.objectMapper.writeValueAsString(values);
                final LocalDateTime lastModified = LocalDateTime.ofInstant(Instant.ofEpochMilli(manifest.lastModified()), ZoneId.systemDefault());
                log.fine(() -> String.format("Using manifest from %s, last modified %s with content:%n%s", this.manifest, lastModified, json));
            } catch (IOException e) {
                log.log(Level.WARNING, e, () -> "Could not debug manifest");
            }
        }
        return values;

    }
}
