package org.faucet_pipeline.demo.webmvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Michael J. Simons, 2018-02-19
 */
@Controller
public class IdeasController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

}
