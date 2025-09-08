# 목표
- 동시성 이슈를 다루는 도메인을 직접 설계한다.
- 동시성으로 인한 **갱신 손실 문제**를 방지하는 방법을 구현한다.

<br/>

---

# 도메인
| 도메인 예시         | 동시성 제어 방식 | 설명                                |
|---------------------|------------------|-------------------------------------|
| 조회수, 좋아요 수   | 원자적 연산      | 단순 카운팅은 원자적 증가 연산으로 처리 |
| 선착순 쿠폰         | 낙관적 락        | 단순 카운팅으로 해야하지만, 낙관적 락 실습으로 버전 충돌을 감지해 처리   |


<br/>

---

# 테이블 설계

### 1.조회수, 좋아요수
- **Post**
- **PostStat**
  - `post_id`, `view_count`, `like_count`
  - Post와 **1:1**
- **PostLike**
  - `post_id`, `user_id`, `like_yn`
  - **UNIQUE**: (`post_id`, `user_id`)
- **PostView**
  - `post_id`, `user_id`, `time`

<br/>

### 2.선착순 쿠폰
- **Coupon**
- **CouponRemaing**
  - `coupon_id`, `remaining`
  - Coupon과 **1:1**
- **CouponIssueHistory**
  - `coupon_id`, `user_id`
  - 쿠폰 발급 내역을 기록 (insert만 진행)
 

<br/>

---

# 설계 고려사항
- **PostStat, CouponRemaing 분리 이유**: 경합/부하 분산 및 **통계 집계 편의성** 확보
- **PostLike / PostView / CouponIssueHistory 생성 의도**: 추후 **일/시간 단위 집계**, **재집계**, **품질 보정**에 활용
  - 현재는 **동시성 제어 실험**에 집중하여 PostLike / PostView는 **미구현**
  - **PostView**는 트래픽/데이터 폭증을 대비하여 **다른 방식**도 고려


<br/>

---

# 추후 개선
- 사용자당 **좋아요 1회**만 가능하도록 변경
- **PostLike**, **PostView** 테이블 및 관련 로직 추가
- **PostView** 테이블 대신 **Kafka** 등 이벤트 로그 기반 스트리밍 도입 검토


<br/>

---


# [참고] JPA 일대일 연관관계 구현 방식
1. @MapId 사용 (Post와 PostStat)

  ```
     @Id
     private Long postId;

     @MapsId
     @OneToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "post_id")
     private PostEntity postEntity;
  ```
    
2. 외부ID 사용 (Coupon와 CouponRemaining)
  
  ```
    @Id //@Id만 주고 @GeneratedValue를 사용하지 않고, 외부에서 직접 식별자를 주입하는 방식
    @Column(name = "coupon_id", nullable = false)
    private Long couponId;
  ```

