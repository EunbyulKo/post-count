package org.silverstar.postcount.support;

import org.junit.jupiter.api.BeforeEach;
import org.silverstar.postcount.domain.Post;
import org.silverstar.postcount.domain.PostStat;
import org.silverstar.postcount.repository.jpa.JpaPostRepository;
import org.silverstar.postcount.repository.jpa.JpaPostStatRepository;
import org.silverstar.postcount.service.interfaces.PostRepository;
import org.silverstar.postcount.service.interfaces.PostStatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class SetupTemplate {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostStatRepository postStatRepository;

    @Autowired
    private JpaPostRepository jpaPostRepository;
    @Autowired
    private JpaPostStatRepository jpaPostStatRepository;

    protected Long postId;

    @BeforeEach
    @Transactional
    void setUp() {
        jpaPostStatRepository.deleteAllInBatch();
        jpaPostRepository.deleteAllInBatch();

        Post post = Post.builder()
                .authorId(1L)
                .title("테스트 제목")
                .content("테스트 본문")
                .build();
        post = postRepository.save(post);

        PostStat stat = PostStat.builder()
                .postId(post.getId())
                .viewCount(0)
                .likeCount(0)
                .build();
        postStatRepository.create(stat);

        postId = post.getId();

    }
}