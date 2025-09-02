package org.silverstar.postcount.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.silverstar.postcount.domain.PostStat;
import org.silverstar.postcount.repository.entity.PostEntity;
import org.silverstar.postcount.repository.entity.PostStatEntity;
import org.silverstar.postcount.repository.jpa.JpaPostStatRepository;
import org.silverstar.postcount.service.interfaces.PostStatRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class PostStatRepositoryImpl implements PostStatRepository  {

    @PersistenceContext
    private final EntityManager entityManager;

    private final JpaPostStatRepository jpaPostStatRepository;

    @Override
    public PostStat findById(Long postId) {
        PostStatEntity entity = jpaPostStatRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("PostStat not found"));
        return entity.toPostStat();
    }

    @Override
    @Transactional
    public PostStat create(PostStat postStat) {
        PostEntity postEntity = entityManager.getReference(PostEntity.class, postStat.getPostId());

        if (postEntity == null) {
            throw new IllegalArgumentException("post not found: " + postStat.getPostId());
        }

        PostStatEntity postStatEntity = new PostStatEntity(postEntity, postStat.getViewCount(), postStat.getLikeCount());
        entityManager.persist(postStatEntity);
        return postStatEntity.toPostStat();
    }

    @Override
    @Transactional
    public PostStat updateViewCount(PostStat postStat) {
        PostStatEntity postStatEntity = jpaPostStatRepository.findById(postStat.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("post_stat not found: " + postStat.getPostId()));

        postStatEntity.setViewCount(postStat.getViewCount());

        return postStatEntity.toPostStat();
    }

    @Override
    @Transactional
    public int incrementView(Long postId, long delta) {
        return jpaPostStatRepository.incrementView(postId, delta);
    }

    @Override
    @Transactional
    public int incrementLike(Long postId, long delta) {
        return jpaPostStatRepository.incrementLike(postId, delta);
    }

}
