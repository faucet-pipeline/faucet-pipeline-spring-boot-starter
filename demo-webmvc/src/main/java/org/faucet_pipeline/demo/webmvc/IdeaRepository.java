package org.faucet_pipeline.demo.webmvc;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Michael J. Simons, 2018-02-19
 */
public interface IdeaRepository extends CrudRepository<Idea, Integer> {
}
