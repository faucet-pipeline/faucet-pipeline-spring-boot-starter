package org.faucet_pipeline.spring.autoconfigure;

import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Michael J. Simons, 2018-02-19
 */
public class FaucetResourceResolver implements ResourceResolver {

    private final Map<String, String> manifest;

    public FaucetResourceResolver(@NonNull final Map<String, String> manifest) {
        this.manifest = Collections.unmodifiableMap(manifest);
    }

    @Override
    public Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
        return Optional.ofNullable(chain.resolveResource(request, requestPath, locations)).orElse(null);

    }

    @Override
    public String resolveUrlPath(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
        return this.manifest.get(resourcePath);
    }
}
