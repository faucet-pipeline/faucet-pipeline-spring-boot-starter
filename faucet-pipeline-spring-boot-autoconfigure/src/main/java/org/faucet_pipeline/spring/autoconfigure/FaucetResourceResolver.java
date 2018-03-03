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

    private final Manifest manifest;

    public FaucetResourceResolver(@NonNull final Manifest manifest) {
        this.manifest = manifest;
    }

    @Override
    public Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
        System.out.println("resolving url " + requestPath);
        return Optional.ofNullable(chain.resolveResource(request, requestPath, locations)).orElse(null);

    }

    @Override
    public String resolveUrlPath(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
        System.out.println("resolvinging " + resourcePath + " to "+ this.manifest.fetch(resourcePath));
        return this.manifest.fetch(resourcePath);
    }
}
