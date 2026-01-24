package com.guitarmarket.domain.comment;

import com.guitarmarket.domain.post.Post;
import com.guitarmarket.domain.post.PostRepository;
import com.guitarmarket.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    /* 댓글 작성 */
    public Long createComment(Long postId, User user, CommentCreateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));

        Comment comment;

        if (request.getParentCommentId() == null) {
            comment = Comment.createRoot(post, user, request.getContent());
        } else {
            Comment parent = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 없습니다."));

            comment = Comment.createReply(post, user, parent, request.getContent());
        }

        return commentRepository.save(comment).getId();
    }

    /* 댓글 삭제 */
    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 없습니다."));

        comment.validateOwner(user);
        comment.softDelete();
    }
}
