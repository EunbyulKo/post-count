package org.silverstar.postcount.service;
import lombok.RequiredArgsConstructor;

import org.silverstar.postcount.service.interfaces.PostStatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeCountService {

    private final PostStatRepository postStatRepository;

    @Transactional(readOnly = true)
    public long getLikeCount(Long postId) {
        return postStatRepository.findById(postId).getLikeCount();
    }

    @Transactional
    public long incrementLike(Long postId, long delta) {
        postStatRepository.incrementLike(postId, delta);
        return delta;
    }
}
