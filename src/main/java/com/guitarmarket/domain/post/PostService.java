package com.guitarmarket.domain.post;

import com.guitarmarket.domain.post.dto.PostCreateRequest;
import com.guitarmarket.domain.product.Product;
import com.guitarmarket.domain.product.ProductRepository;
import com.guitarmarket.domain.user.User;
import com.guitarmarket.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Long createPost(Long userId, PostCreateRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Product product = null;
        if (request.getProductId() != null) {
            product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품 없음"));
        }

        Post post = Post.create(
                user,
                product,
                PostType.valueOf(request.getPostType()),
                request.getRegion(),
                request.getLocationDetail(),
                SaleStatus.valueOf(request.getSaleStatus()),
                request.isNotice()
        );

        postRepository.save(post);
        return post.getId();
    }
}
