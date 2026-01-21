package com.guitarmarket.domain.post;

import com.guitarmarket.domain.product.Product;
import com.guitarmarket.domain.user.User;
import com.guitarmarket.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "post")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    // 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 상품 정보 (커뮤니티 글은 NULL)
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
    private boolean isNotice = false;

    @Column(nullable = false)
    private int viewCount = 0;

    @Column
    private java.time.LocalDateTime deletedAt;
}
