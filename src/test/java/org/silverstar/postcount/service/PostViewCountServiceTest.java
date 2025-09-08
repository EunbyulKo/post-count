package org.silverstar.postcount.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.silverstar.postcount.support.SetupPostTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import static org.silverstar.postcount.support.ConcurrentTestTemplate.run;

class PostViewCountServiceTest extends SetupPostTemplate {

    @Autowired
    private PostViewCountService postViewCountService;

    @Test
    void call_once() {
        long before = postViewCountService.getViewCount(postId);
        long after  = postViewCountService.incrementView(postId);
        long read   = postViewCountService.getViewCount(postId);

        Assertions.assertEquals(before + 1, after);
        Assertions.assertEquals(after, read);
    }

    @Test
    void given_increaseView_when_concurrentRequests_then_lose_value() throws Exception {
        int threads = 100;

        run(threads, 20, () -> postViewCountService.incrementView(postId));

        long finalCount = postViewCountService.getViewCount(postId);
        Assertions.assertNotEquals(threads, finalCount);
    }

    @Test
    void given_increaseView_when_concurrentRequests_then_success() throws Exception {
        int threads = 100;

        run(threads, 20, () -> postViewCountService.incrementView(postId, 1));

        long finalCount = postViewCountService.getViewCount(postId);
        Assertions.assertEquals(threads, finalCount);
    }


}