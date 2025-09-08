# 목표
- 조회수/좋아요 수를 주제로 **구조를 설계**하고  
- 동시성 이슈로 인한 **갱신 손실 방지 방법**을 알아보고 **직접 구현**한다.

---

# 테이블 설계
- **Post**
- **PostStat**
  - `post_id`, `view_count`, `like_count`
  - Post와 **1:1**
- **PostLike**
  - `post_id`, `user_id`, `like_yn`
  - **UNIQUE**: (`post_id`, `user_id`)
- **PostView**
  - `post_id`, `user_id`, `time`

---

# 설계 고려사항
- **PostStat 분리 이유**: 경합/부하 분산 및 **통계 집계 편의성** 확보
- **PostLike / PostView 생성 의도**: 추후 **일/시간 단위 집계**, **재집계**, **품질 보정**에 활용
  - 현재는 **동시성 제어 실험**에 집중하여 PostLike / PostView는 **미구현**
  - **PostView**는 트래픽/데이터 폭증을 대비하여 **다른 방식**도 고려

---

# 추후 개선
- 사용자당 **좋아요 1회**만 가능하도록 변경
- **PostLike**, **PostView** 테이블 및 관련 로직 추가
- **PostView** 테이블 대신 **Kafka** 등 이벤트 로그 기반 스트리밍 도입 검토
