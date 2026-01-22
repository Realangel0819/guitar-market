package com.guitarmarket.domain.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreateRequest {

    private Long userId;
    private Long productId;
    private String postType;
    private String region;
    private String locationDetail;
    private String saleStatus;
    private boolean isNotice;
}
