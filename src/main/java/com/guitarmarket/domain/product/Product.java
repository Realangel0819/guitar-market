package com.guitarmarket.domain.product;

import java.time.LocalDateTime;

import com.guitarmarket.domain.category.Category;
import com.guitarmarket.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(length = 50)
    private String brand;

    @Column(name = "model_name", length = 100)
    private String modelName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private LocalDateTime deletedAt;

     // ✅ 생성 메서드
    public static Product create(
            Category category,
            String brand,
            String modelName,
            String description
    ) {
        Product product = new Product();
        product.category = category;
        product.brand = brand;
        product.modelName = modelName;
        product.description = description;
        return product;
    }
}
