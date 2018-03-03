package org.faucet_pipeline.spring.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import lombok.extern.java.Log;
import org.springframework.core.io.Resource;

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

    Manifest(ObjectMapper objectMapper, Resource manifest) {
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
