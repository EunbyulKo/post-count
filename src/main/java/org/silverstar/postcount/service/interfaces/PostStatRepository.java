package org.silverstar.postcount.service.interfaces;

import org.silverstar.postcount.domain.PostStat;

public interface PostStatRepository {
    PostStat findById(Long postId);
    PostStat create(PostStat postStat);
    PostStat updateViewCount(PostStat postStat);
    int incrementView(Long postId, long delta);
    int incrementLike(Long postId, long delta);
}
