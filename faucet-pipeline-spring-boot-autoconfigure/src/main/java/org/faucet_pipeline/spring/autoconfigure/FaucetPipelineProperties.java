package org.faucet_pipeline.spring.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author Michael J. Simons
 */
@Getter 
@Setter
@ConfigurationProperties(prefix = "faucet-pipeline")
public class FaucetPipelineProperties {

    /**
     * Path or resource for Faucets manifest. Defaults to
     * <pre>manifest.json</pre>.
     */
    private Resource manifest = new ClassPathResource("manifest.json");
    
    private String[] pathPatterns = {"/**"};
    
    /**
     * Flag, wether the manifest should be cached or not. Set it to <code>false</code>
     * during development to use faucets watch task and get your assets reloaded.
     */
    private boolean cacheManifest = true;
}
