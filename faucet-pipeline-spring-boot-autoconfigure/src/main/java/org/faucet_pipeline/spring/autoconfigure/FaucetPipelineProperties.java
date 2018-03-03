package org.faucet_pipeline.spring.autoconfigure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author Michael J. Simons
 */
@ConfigurationProperties(prefix = "faucet-pipeline")
public class FaucetPipelineProperties {

    /**
     * Path or resource for Faucets manifest. Defaults to
     * <pre>manifest.json</pre>.
     */
    private Resource manifest = new ClassPathResource("manifest.json");

    public Resource getManifest() {
        return manifest;
    }

    public void setManifest(Resource manifest) {
        this.manifest = manifest;
    }
}
