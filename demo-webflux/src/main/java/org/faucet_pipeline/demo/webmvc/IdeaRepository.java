package org.faucet_pipeline.demo.webmvc;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * @author Michael J. Simons, 2018-03-03
 */
public interface IdeaRepository extends ReactiveCrudRepository<Idea, Integer> {
}
