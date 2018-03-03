/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.faucet_pipeline.spring.autoconfigure;

import java.util.Map;
import java.util.Optional;

/**
 * Access to all configured Faucet manifests.
 * 
 * @author Michael J. Simons, 2018-02-20
 */
class Manifest {
    private final Map<String, String> values;

    public Manifest(final Map<String, String> values) {
        this.values = values;
    }

    public String fetch(final String assetName) {
        return Optional.ofNullable(this.values.get(assetName))
            .map(a -> a.replaceFirst("^/", ""))
            .orElseGet(() -> String.format("The asset '%s' was not in the manifest", assetName));
    }
}
