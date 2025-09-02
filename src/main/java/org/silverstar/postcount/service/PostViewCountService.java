package org.silverstar.postcount.service;

import lombok.RequiredArgsConstructor;
import org.silverstar.postcount.domain.PostStat;
import org.silverstar.postcount.service.interfaces.PostStatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostViewCountService {

    private final PostStatRepository postStatRepository;

    @Transactional(readOnly = true)
    public long getViewCount(Long postId) {
        return postStatRepository.findById(postId).getViewCount();
    }

    @Transactional
    public long incrementView(Long postId) {
        PostStat postStat = postStatRepository.findById(postId);
        postStat.view();
        postStatRepository.updateViewCount(postStat);
        return postStat.getViewCount();
    }

    @Transactional
    public long incrementView(Long postId, long delta) {
        postStatRepository.incrementView(postId, delta);
        return delta;
    }

}
