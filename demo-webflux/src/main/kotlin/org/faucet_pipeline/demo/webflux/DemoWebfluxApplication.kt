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
package org.faucet_pipeline.demo.webflux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.support.beans
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter
import org.springframework.web.reactive.function.server.router

/**
 * Entry point to the idea application.
 *
 * @author Michael J. Simons
 */
@SpringBootApplication
@EnableReactiveMongoRepositories
class DemoWebfluxApplication

class Router(val handler: UIHandler) {
    fun routes() = router {
        GET("/", handler::index)
        DELETE("/delete/{id}", handler::delete)
    }
}

fun beans() = beans {
    // Application specific beans
    bean<UIHandler>()

    // Infrastructure
    bean {
        Router(ref()).routes()
    }
    bean<HiddenHttpMethodFilter>()
}

fun main(args: Array<String>) {
    SpringApplicationBuilder()
            .sources(DemoWebfluxApplication::class.java)
            .initializers(beans())
            .run(*args);
}