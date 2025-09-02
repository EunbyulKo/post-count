package org.silverstar.postcount.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.silverstar.postcount.domain.Post;

@Entity
@Table(name = "tb_post")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    public PostEntity(Post post) {
        this.id = post.getId();
        this.authorId = post.getAuthorId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }

    public Post toPost() {
        return Post.builder()
                .id(id)
                .authorId(authorId)
                .title(title)
                .content(content)
                .build();
    }

}
