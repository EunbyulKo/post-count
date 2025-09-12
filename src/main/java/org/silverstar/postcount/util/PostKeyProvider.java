package org.silverstar.postcount.util;

public class PostKeyProvider {
    public static final String REDIS_PATTERN = "post:*:views";
    public static final String REDIS_DUP_KEY = "dup:view:";
    public static final String KAFKA_TOPIC = "post-views";

    public static String redisKey(Long postId) {
        return "post:" + postId + ":views";
    }

    public static Long getPostId(String redisKey) {
        return Long.parseLong(redisKey.split(":")[1]);
    }

}
