package org.faucet_pipeline.spring.autoconfigure;

import java.util.Arrays;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static org.faucet_pipeline.spring.autoconfigure.FaucetPipelineProperties.PROPERTIES_PREFIX;

/**
 * @author Michael J. Simons
 */
@ConfigurationProperties(prefix = PROPERTIES_PREFIX)
public class FaucetPipelineProperties {

    static final String PROPERTIES_PREFIX = "faucet-pipeline";
    /**
     * Path or resource for Faucets manifest. Defaults to
     * <pre>manifest.json</pre>.
     */
    private List<Resource> manifests = Arrays.asList(new ClassPathResource("manifest.json"));

    public List<Resource> getManifests() {
        return manifests;
    }

    public void setManifests(List<Resource> manifests) {
        this.manifests = manifests;
    }
}
