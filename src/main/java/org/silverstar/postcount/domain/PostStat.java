package org.silverstar.postcount.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostStat {
    private Long postId;
    private long viewCount;
    private long likeCount;

    @Builder
    public PostStat(Long postId, long viewCount, long likeCount) {
        if (postId == null) {
            throw new NullPointerException("postId is null");
        }

        this.postId = postId;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
    }

    public void view() {
        this.viewCount++;
    }

    public void like() {
        this.likeCount++;
    }

    public void unlike() {
        if (this.likeCount > 1) {
            this.likeCount--;
        }
    }
}
