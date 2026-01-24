package com.guitarmarket.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 게시글의 최상위 댓글만 조회
    List<Comment> findByPostIdAndParentIsNullOrderByCreatedAtAsc(Long postId);
}
