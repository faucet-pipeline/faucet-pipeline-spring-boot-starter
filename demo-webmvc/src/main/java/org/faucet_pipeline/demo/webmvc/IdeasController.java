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
package org.faucet_pipeline.demo.webmvc;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author Michael J. Simons, 2018-02-19
 */
@Controller
@RequiredArgsConstructor
public class IdeasController {

    private final IdeaRepository ideaRepository;

    @GetMapping("/")
    public ModelAndView index() {
        final List<Idea> ideas = this.ideaRepository.findAll(Sort.by("createdAt").ascending());
        return new ModelAndView("index", new ModelMap().addAttribute("ideas", ideas));
    }

    @GetMapping("/new")
    public ModelAndView newIdea() {
        return new ModelAndView("new", new ModelMap().addAttribute("idea", new IdeaForm()));
    }

    @PostMapping("/new")
    public ModelAndView newIdea(
        @Valid @ModelAttribute(name = "idea") final IdeaForm idea,
        final BindingResult bindingResult
    ) {
        final ModelAndView rv;
        if(bindingResult.hasErrors()) {
            rv = new ModelAndView("new");
        } else {
            this.ideaRepository.save(new Idea(idea.getContent()));
            rv = new ModelAndView("redirect:/");
        }
        return rv;
    }

    @DeleteMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable  final Integer id) {
        this.ideaRepository.deleteById(id);
        return new ModelAndView("redirect:/");
    }

    @Data
    static class IdeaForm {
        @NotEmpty
        private String content;
    }
}
