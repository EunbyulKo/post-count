package org.silverstar.postcount.service.interfaces;

import org.silverstar.postcount.domain.Post;

public interface PostRepository {
    Post findById(Long id);
    Post save(Post postStat);
}
