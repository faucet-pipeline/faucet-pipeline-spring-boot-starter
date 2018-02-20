/*
 * Copyright 2018 faucet-pipeline.org.
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

import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static org.faucet_pipeline.spring.autoconfigure.FaucetPipelineProperties.PROPERTIES_PREFIX;

/**
 * @author Michael J. Simons, 2018-02-20
 */
class AtLeastOneManifestExistsCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        final Environment environment = context.getEnvironment();
        ConditionOutcome rv = ConditionOutcome.noMatch("Invalid configuration.");
        try {
            // @formatter:off
            final List<Resource> configuredManifests = Binder.get(environment) // Retrieve a binder baseed on the 
                    // Bind all properties or just a subset
                    .bind(PROPERTIES_PREFIX, FaucetPipelineProperties.class)
                        // If it doesn't bind, use a default
                        .orElseGet(FaucetPipelineProperties::new)
                    // do whatever you want
                    .getManifests();
            // @formatter:on
            
            rv = configuredManifests.stream()
                    .filter(Resource::exists)
                    .findFirst()
                    .map(r -> ConditionOutcome.match(String.format("At least one manifest exists")))
                    .orElseGet(() -> ConditionOutcome.noMatch(String.format("None of the configureded manifests (%s) exists.", environment.getProperty(PROPERTIES_PREFIX))));
        } catch (BindException ex) {
        }
        return rv;
    }

}
