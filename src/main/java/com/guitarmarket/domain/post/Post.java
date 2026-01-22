package com.guitarmarket.domain.post;

import com.guitarmarket.domain.product.Product;
import com.guitarmarket.domain.user.User;
import com.guitarmarket.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "post")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PostType postType;

    @Column(length = 50)
    private String region;

    @Column(name = "location_detail", length = 100)
    private String locationDetail;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SaleStatus saleStatus;

    @Column(nullable = false)
    private boolean isNotice;

    @Column(nullable = false)
    private int viewCount;

    @Column
    private java.time.LocalDateTime deletedAt;

    /* ================= 생성 로직 ================= */
    public static Post create(
            User user,
            Product product,
            PostType postType,
            String region,
            String locationDetail,
            SaleStatus saleStatus,
            boolean isNotice
    ) {
        Post post = new Post();
        post.user = user;
        post.product = product;
        post.postType = postType;
        post.region = region;
        post.locationDetail = locationDetail;
        post.saleStatus = saleStatus;
        post.isNotice = isNotice;
        post.viewCount = 0;
        return post;
    }
}
