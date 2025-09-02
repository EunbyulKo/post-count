package org.silverstar.postcount.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.silverstar.postcount.domain.PostStat;

@Entity
@Table(name = "tb_post_stat")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostStatEntity {

    @Id
    private Long postId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity postEntity;

    @Column(name = "view_count", nullable = false)
    @Setter
    private long viewCount = 0L;

    @Column(name = "like_count", nullable = false)
    private long likeCount = 0L;

    public PostStatEntity(PostEntity postEntity, long viewCount, long likeCount) {
        this.postEntity = postEntity;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
    }

    public PostStat toPostStat() {
        return PostStat.builder()
                .postId(this.postId)
                .viewCount(this.viewCount)
                .likeCount(this.likeCount)
                .build();
    }

}
