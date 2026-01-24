package com.guitarmarket.domain.comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record CommentResponse(
        Long id,
        String writer,
        String content,
        int depth,
        LocalDateTime createdAt,
        List<CommentResponse> replies
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getUser().getNickname(),
                comment.getContent(),
                comment.getDepth(),
                comment.getCreatedAt(),
                comment.getChildren().stream()
                        .map(CommentResponse::from)
                        .collect(Collectors.toList())
        );
    }
}
