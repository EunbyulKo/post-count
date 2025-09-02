package org.silverstar.postcount.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Post {
    private Long id;
    private long authorId;
    private String title;
    private String content;

    @Builder
    public Post(Long id, long authorId, String title, String content) {
        this.id = id;
        this.authorId = authorId;
        this.title = title;
        this.content = content;
    }
}
