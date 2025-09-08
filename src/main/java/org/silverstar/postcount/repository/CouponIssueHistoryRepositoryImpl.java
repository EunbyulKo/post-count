package org.silverstar.postcount.repository;

import lombok.RequiredArgsConstructor;

import org.silverstar.postcount.domain.CouponIssueHistory;
import org.silverstar.postcount.repository.entity.CouponIssueHistoryEntity;
import org.silverstar.postcount.repository.jpa.JpaCouponIssueHistoryRepository;
import org.silverstar.postcount.service.interfaces.CouponIssueHistoryRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponIssueHistoryRepositoryImpl implements CouponIssueHistoryRepository {

    private final JpaCouponIssueHistoryRepository jpaCouponIssueHistoryRepository;


    @Override
    public CouponIssueHistory save(CouponIssueHistory CouponIssueHistory) {
        CouponIssueHistoryEntity entity = jpaCouponIssueHistoryRepository.save(new CouponIssueHistoryEntity(CouponIssueHistory));
        return entity.toCouponIssueHistory();
    }

}
