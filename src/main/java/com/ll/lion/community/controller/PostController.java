package com.ll.lion.community.controller;

import com.ll.lion.common.dto.ResponseDto;
import com.ll.lion.community.dto.post.PostReqDto;
import com.ll.lion.community.entity.Post;
import com.ll.lion.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts") // TODO url /api로 시작해야하는지 물어보기
public class PostController {
    private final PostService postService;

    // 게시글 등록
    @GetMapping("/save")
    public ResponseEntity<Post> postSave(@RequestBody PostReqDto postReqDto) {
        // TODO 로그인된 사용자 여부 체크
        Post post = postService.postSave(postReqDto);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }
}