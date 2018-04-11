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

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

/**
 * Ideas that are being stored.
 *
 * @author Michael J. Simons
 */
@Document(collection = "ideas")
data class Idea(
        @Id var id: String? = null,
        val content: String,
        @CreatedDate
        val createdAt: LocalDateTime = LocalDateTime.now(),
        @LastModifiedDate
        var updatedAt: LocalDateTime = LocalDateTime.now()
)

/**
 * Storage of ideas.
 */
interface IdeaRepository : ReactiveCrudRepository<Idea, String>

/**
 * To be represented in the frontend
 */
class IdeaForm {
    @NotEmpty
    var content: String? = null
}

/**
 * 50% of the UI handling.
 */
class UIHandler(private val ideaRepository: IdeaRepository) {

    fun index(req: ServerRequest)
            = ServerResponse.ok().render("index", mapOf("ideas" to ReactiveDataDriverContextVariable(ideaRepository.findAll())))

    fun delete(req: ServerRequest) = ideaRepository
            .deleteById(req.pathVariable("id"))
            .then(ServerResponse.ok().render("redirect:/"))

}

/**
 * The actual class handing the front end.
 *
 * Handling POST data does not yet work flawless with request handlers, especially the hidden method filter breaks stuff,
 * see https://jira.spring.io/browse/SPR-16551.
 */
@Controller
@RequestMapping("/new")
class FormHandler(val ideaRepository: IdeaRepository) {
    @GetMapping
    fun newIdea(model: Model) =
            Mono.just(model.addAttribute("idea", IdeaForm())).map { _ -> "new" }

    @PostMapping
    fun saveNewIdea(
            @Valid @ModelAttribute(name = "idea") idea: Mono<IdeaForm>
    ) = idea.map { Idea(content = it.content!!) }
            .flatMap { ideaRepository.save(it) }
            .flatMap { Mono.just("redirect:/") }
            .onErrorReturn("new")
}