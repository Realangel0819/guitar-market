package com.guitarmarket.domain.product;

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
}
