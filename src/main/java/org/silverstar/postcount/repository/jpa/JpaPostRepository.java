package org.silverstar.postcount.repository.jpa;

import org.silverstar.postcount.repository.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPostRepository extends JpaRepository<PostEntity, Long> {}
