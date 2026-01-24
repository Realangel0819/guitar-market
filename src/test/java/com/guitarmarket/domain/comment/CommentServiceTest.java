package com.guitarmarket.domain.comment;

import com.guitarmarket.domain.post.Post;
import com.guitarmarket.domain.post.PostRepository;
import com.guitarmarket.domain.user.User;
import com.guitarmarket.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    /* ================= 댓글 작성 ================= */

    @Test
    void 댓글_작성_성공() {
        // given
        User user = createUser("test@test.com");
        Post post = createPost(user);

        CommentCreateRequest request =
                new CommentCreateRequest("댓글 내용", null);

        // when
        Long commentId =
                commentService.createComment(post.getId(), user, request);

        // then
        Comment comment = commentRepository.findById(commentId).get();

        assertThat(comment.getDepth()).isEqualTo(0);
        assertThat(comment.getParent()).isNull();
        assertThat(comment.getContent()).isEqualTo("댓글 내용");
        assertThat(comment.getPost()).isEqualTo(post);
    }

    /* ================= 대댓글 작성 ================= */

    @Test
    void 대댓글_작성_성공() {
        // given
        User user = createUser("test@test.com");
        Post post = createPost(user);

        Long parentId = commentService.createComment(
                post.getId(),
                user,
                new CommentCreateRequest("부모 댓글", null)
        );

        // when
        Long replyId = commentService.createComment(
                post.getId(),
                user,
                new CommentCreateRequest("대댓글", parentId)
        );

        // then
        Comment reply = commentRepository.findById(replyId).get();

        assertThat(reply.getDepth()).isEqualTo(1);
        assertThat(reply.getParent().getId()).isEqualTo(parentId);
        assertThat(reply.getParent().getChildren()).hasSize(1);
    }

    /* ================= 대댓글에 대댓글 불가 ================= */

    @Test
    void 대댓글에_대댓글_불가() {
        // given
        User user = createUser("test@test.com");
        Post post = createPost(user);

        Long parentId = commentService.createComment(
                post.getId(),
                user,
                new CommentCreateRequest("부모", null)
        );

        Long replyId = commentService.createComment(
                post.getId(),
                user,
                new CommentCreateRequest("대댓글", parentId)
        );

        // expect
        assertThatThrownBy(() ->
                commentService.createComment(
                        post.getId(),
                        user,
                        new CommentCreateRequest("대대댓글", replyId)
                )
        ).isInstanceOf(IllegalStateException.class);
    }

    /* ================= 댓글 삭제 ================= */

    @Test
    void 댓글_soft_delete() {
        // given
        User user = createUser("test@test.com");
        Post post = createPost(user);

        Long commentId = commentService.createComment(
                post.getId(),
                user,
                new CommentCreateRequest("삭제할 댓글", null)
        );

        // when
        commentService.deleteComment(commentId, user);

        // then
        Comment comment = commentRepository.findById(commentId).get();
        assertThat(comment.getDeletedAt()).isNotNull();
    }

    /* ================= 권한 없는 삭제 ================= */

    @Test
    void 댓글_삭제_권한없음() {
        // given
        User owner = createUser("owner@test.com");
        User other = createUser("other@test.com");
        Post post = createPost(owner);

        Long commentId = commentService.createComment(
                post.getId(),
                owner,
                new CommentCreateRequest("댓글", null)
        );

        // expect
        assertThatThrownBy(() ->
                commentService.deleteComment(commentId, other)
        ).isInstanceOf(IllegalStateException.class);
    }

    /* ================= soft delete + @Where ================= */

    @Test
    void 삭제된_댓글은_조회되지_않음() {
        // given
        User user = createUser("test@test.com");
        Post post = createPost(user);

        Long commentId = commentService.createComment(
                post.getId(),
                user,
                new CommentCreateRequest("댓글", null)
        );

        commentService.deleteComment(commentId, user);

        // when
        List<Comment> comments = commentRepository.findAll();

        // then
        assertThat(comments).isEmpty();
    }

    /* ================= 테스트 헬퍼 ================= */

    private User createUser(String email) {
        User user = User.create(email, "password", "nickname", "01012345678");
        return userRepository.save(user);
    }

    private Post createPost(User user) {
        Post post = Post.create(
                user,
                null,
                com.guitarmarket.domain.post.PostType.SALE,
                "서울",
                "gangseo-gu",
                com.guitarmarket.domain.post.SaleStatus.ON_SALE,
                false
        );
        return postRepository.save(post);
    }
}
