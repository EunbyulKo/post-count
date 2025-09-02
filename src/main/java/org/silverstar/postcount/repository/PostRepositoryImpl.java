package org.silverstar.postcount.repository;

import lombok.RequiredArgsConstructor;

import org.silverstar.postcount.domain.Post;
import org.silverstar.postcount.repository.entity.PostEntity;
import org.silverstar.postcount.repository.jpa.JpaPostRepository;
import org.silverstar.postcount.service.interfaces.PostRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final JpaPostRepository jpaPostRepository;

    @Override
    public Post findById(Long postId) {
        PostEntity entity = jpaPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        return entity.toPost();
    }

    @Override
    public Post save(Post post) {
        PostEntity entity = jpaPostRepository.save(new PostEntity(post));
        return entity.toPost();
    }

}
