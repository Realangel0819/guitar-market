package com.guitarmarket.domain.product;

import com.guitarmarket.domain.category.Category;
import com.guitarmarket.domain.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Long createProduct(
            Long categoryId,
            String brand,
            String modelName,
            String description
    ) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리 없음"));

        Product product = Product.create(
                category,
                brand,
                modelName,
                description
        );

        productRepository.save(product);
        return product.getId();
    }
}
