        package com.guitarmarket.domain.post;

        import com.guitarmarket.domain.category.Category;
        import com.guitarmarket.domain.category.CategoryRepository;
        import com.guitarmarket.domain.post.dto.PostCreateRequest;
        import com.guitarmarket.domain.product.Product;
        import com.guitarmarket.domain.product.ProductRepository;
        import com.guitarmarket.domain.user.User;
        import com.guitarmarket.domain.user.UserRepository;
        import jakarta.persistence.EntityManager;
        import org.junit.jupiter.api.DisplayName;
        import org.junit.jupiter.api.Test;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.test.context.SpringBootTest;
        import org.springframework.transaction.annotation.Transactional;

        import static org.assertj.core.api.Assertions.assertThat;

        @SpringBootTest
        @Transactional
        class PostServiceTest {

        @Autowired
        UserRepository userRepository;

        @Autowired
        PostRepository postRepository;

        @Autowired
        ProductRepository productRepository;

        @Autowired
        CategoryRepository categoryRepository;

        @Autowired
        PostService postService;

        @Autowired
        EntityManager em;

        @Test
        @DisplayName("게시글 생성 성공")
        void createPost() {
                // given
                User user = userRepository.save(
                        User.create(
                                "test@test.com",
                                "1234",
                                "펭귄",
                                "01012345678"
                        )
                );
                
                Category category = categoryRepository.save(
                        Category.createRoot("기타")
                );
                
                Product product = productRepository.save(
                        Product.create(category, "Fender", "Stratocaster", "전설의 기타")
                );

                // when
                PostCreateRequest request = new PostCreateRequest();
                request.setProductId(product.getId());
                request.setPostType("SALE");
                request.setRegion("서울");
                request.setLocationDetail("강남구");
                request.setSaleStatus("ON_SALE");
                request.setNotice(false);
                
                Long postId = postService.createPost(user.getId(), request);

                // then
                Post findPost = postRepository.findById(postId).orElseThrow();

                assertThat(findPost.getUser().getEmail()).isEqualTo("test@test.com");
                assertThat(findPost.getPostType()).isEqualTo(PostType.SALE);
                assertThat(findPost.getSaleStatus()).isEqualTo(SaleStatus.ON_SALE);
                assertThat(findPost.getViewCount()).isEqualTo(0);
        }
        }
