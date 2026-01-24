package com.guitarmarket.domain.comment;

import com.guitarmarket.domain.post.Post;
import com.guitarmarket.domain.user.User;
import com.guitarmarket.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "comment")
@Where(clause = "deleted_at IS NULL")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    /* ================= 연관관계 ================= */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 부모 댓글 (null이면 일반 댓글)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parent;

    // 대댓글 목록
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    /* ================= 컬럼 ================= */

    @Column(nullable = false)
    private int depth; // 0: 댓글, 1: 대댓글

    @Column(nullable = false, length = 500)
    private String content;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /* ================= 생성 로직 ================= */

    public static Comment createRoot(Post post, User user, String content) {
        Comment comment = new Comment();
        comment.post = post;
        comment.user = user;
        comment.content = content;
        comment.depth = 0;
        return comment;
    }

    public static Comment createReply(Post post, User user, Comment parent, String content) {
        if (parent.depth >= 1) {
            throw new IllegalStateException("대댓글에는 댓글을 달 수 없습니다.");
        }

        Comment reply = new Comment();
        reply.post = post;
        reply.user = user;
        reply.parent = parent;
        reply.content = content;
        reply.depth = 1;

        parent.children.add(reply);
        return reply;
    }

    /* ================= 도메인 로직 ================= */

    public void validateOwner(User user) {
        if (!this.user.equals(user)) {
            throw new IllegalStateException("댓글 수정/삭제 권한이 없습니다.");
        }
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}
