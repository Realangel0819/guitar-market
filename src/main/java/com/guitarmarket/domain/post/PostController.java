package com.guitarmarket.domain.post;

import com.guitarmarket.domain.post.dto.PostCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public Long create(@RequestBody PostCreateRequest request) {
        return postService.createPost(request.getUserId(), request);
    }
}
