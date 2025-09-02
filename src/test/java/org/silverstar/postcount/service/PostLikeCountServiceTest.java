package org.silverstar.postcount.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.silverstar.postcount.support.SetupTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import static org.silverstar.postcount.support.ConcurrentTestTemplate.run;

class PostLikeCountServiceTest extends SetupTemplate {

    @Autowired
    private PostLikeCountService postLikeCountService;

    @Test
    void call_once() {
        long before = postLikeCountService.getLikeCount(postId);
        long after  = postLikeCountService.incrementLike(postId, 1);
        long read   = postLikeCountService.getLikeCount(postId);

        Assertions.assertEquals(before + 1, after);
        Assertions.assertEquals(after, read);
    }

    @Test
    void given_increaseLike_when_concurrentRequests_then_success() throws Exception {
        int threads = 100;

        run(threads, 20, () -> postLikeCountService.incrementLike(postId, 1));

        long finalCount = postLikeCountService.getLikeCount(postId);
        Assertions.assertEquals(threads, finalCount);
    }


}