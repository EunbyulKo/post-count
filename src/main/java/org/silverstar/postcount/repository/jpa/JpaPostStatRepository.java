package org.silverstar.postcount.repository.jpa;

import org.silverstar.postcount.repository.entity.PostStatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaPostStatRepository extends JpaRepository<PostStatEntity, Long> {

    @Modifying
    @Query("update PostStatEntity s set s.viewCount = s.viewCount + :delta where s.postId = :postId")
    int incrementView(@Param("postId") Long postId, @Param("delta") long delta);

    @Modifying
    @Query("update PostStatEntity s set s.likeCount = s.likeCount + :delta where s.postId = :postId")
    int incrementLike(@Param("postId") Long postId, @Param("delta") long delta);

}
