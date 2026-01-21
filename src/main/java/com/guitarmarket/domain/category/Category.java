package com.guitarmarket.domain.category;

import com.guitarmarket.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    // 부모 카테고리 (자기참조)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    // 자식 카테고리들
    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();

    @Column(name = "category_name", nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private int depth;

    /* ===== 생성 메서드 ===== */
    public static Category createRoot(String name) {
        Category category = new Category();
        category.name = name;
        category.depth = 1;
        return category;
    }

    public static Category createChild(Category parent, String name) {
        Category category = new Category();
        category.parent = parent;
        category.name = name;
        category.depth = parent.depth + 1;
        return category;
    }
}
