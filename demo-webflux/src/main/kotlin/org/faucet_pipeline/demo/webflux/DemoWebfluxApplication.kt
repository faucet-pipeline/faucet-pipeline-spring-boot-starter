package org.faucet_pipeline.demo.webflux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.support.beans
import org.springframework.ui.ModelMap
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router
import org.springframework.web.reactive.resource.ResourceUrlProvider
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable

class UIHandler(val ideaRepository: IdeaRepository) {

    fun index(req: ServerRequest)
            = ok().render("index", mapOf("ideas" to ReactiveDataDriverContextVariable(ideaRepository.findAll())))

    fun delete(req: ServerRequest) = ideaRepository
            .deleteById(req.pathVariable("id"))
            .then(ServerResponse.ok().render("redirect:/"))

}

class Router(val handler: UIHandler) {
    fun routes() = router {
        GET("/", handler::index)
        DELETE("/delete/{id}", handler::delete)
    }
}

fun beans() = beans {
    bean<UIHandler>()
    bean<HiddenHttpMethodFilter>()
    bean {
        Router(ref()).routes()
    }
}

@SpringBootApplication
class DemoWebfluxApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder()
            .sources(DemoWebfluxApplication::class.java)
            .initializers(beans())
            .run(*args);
}