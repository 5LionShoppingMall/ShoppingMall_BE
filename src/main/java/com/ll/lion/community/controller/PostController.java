package com.ll.lion.community.controller;

import com.ll.lion.common.dto.ResponseDto;
import com.ll.lion.community.dto.post.PostDto;
import com.ll.lion.community.dto.post.PostReqDto;
import com.ll.lion.community.dto.post.PostRespDto;
import com.ll.lion.community.entity.Post;
import com.ll.lion.community.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    // 게시글 등록
    @PostMapping("/save")
    public ResponseEntity<?> postSave(@Valid @RequestBody PostReqDto postReqDto) {
        // TODO 로그인된 사용자 여부 체크

        ResponseDto<PostDto> responseDto;
        try {
            Post post = postService.postSave(postReqDto);
            responseDto = ResponseDto.<PostDto>builder().objData(new PostDto(post)).build();
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            responseDto = ResponseDto.<PostDto>builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    // 게시글 모두 조회
    @GetMapping("/list")
    public ResponseEntity<?> postList() {
        ResponseDto<PostDto> responseDto;
        try {
            List<Post> postList = postService.postList();
            List<PostDto> postDtos = postList.stream().map(PostDto::new).toList();
            responseDto = ResponseDto.<PostDto>builder().listData(postDtos).build();
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            responseDto = ResponseDto.<PostDto>builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    // 게시글 1개 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        Post post = postService.getPost(id);
        PostRespDto postRespDto = new PostRespDto(post);
        return new ResponseEntity<>(postRespDto, HttpStatus.OK);
    }

    // 게시글 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}