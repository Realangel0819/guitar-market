package com.guitarmarket.domain.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Long createRoot(String name) {
        Category category = Category.createRoot(name);
        categoryRepository.save(category);
        return category.getId();
    }

    public Long createChild(Long parentId, String name) {
        Category parent = categoryRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("부모 없음"));

        Category category = Category.createChild(parent, name);
        categoryRepository.save(category);
        return category.getId();
    }
}
