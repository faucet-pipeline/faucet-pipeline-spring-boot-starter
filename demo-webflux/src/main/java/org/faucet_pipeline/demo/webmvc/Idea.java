package org.faucet_pipeline.demo.webmvc;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Michael J. Simons, 2018-03-03
 */
@Document(collection = "ideas")
@Getter
public class Idea {
    @Id
    private Integer id;

    @Setter
    @NotNull
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Idea(final String content) {
        this.content = content;
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }
}
