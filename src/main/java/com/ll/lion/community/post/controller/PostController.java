package com.ll.lion.community.post.controller;

import com.ll.lion.common.dto.ResponseDto;
import com.ll.lion.community.post.dto.post.PostDto;
import com.ll.lion.community.post.dto.post.PostReqDto;
import com.ll.lion.community.post.dto.post.PostRespDto;
import com.ll.lion.community.post.entity.Post;
import com.ll.lion.community.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    // 게시글 등록
    @PostMapping("/save")
    public ResponseEntity<?> postSave(@Valid @RequestBody PostReqDto reqDto, Principal principal) {
        ResponseDto<PostRespDto> responseDto;
        try {
            Post post = postService.postSave(reqDto, principal.getName());
            responseDto = ResponseDto.<PostRespDto>builder().objData(new PostRespDto(post)).build();
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            responseDto = ResponseDto.<PostRespDto>builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    // 게시글 모두 조회
    @GetMapping("/list")
    public ResponseEntity<?> postList() {
        ResponseDto<PostRespDto> responseDto;
        try {
            List<Post> postList = postService.postList();
            List<PostRespDto> postDtos = postList.stream().map(PostRespDto::new).toList();
            responseDto = ResponseDto.<PostRespDto>builder().listData(postDtos).build();
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            responseDto = ResponseDto.<PostRespDto>builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    // 게시글 1개 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        ResponseDto<PostRespDto> responseDto;
        try {
            Post post = postService.getPost(id);
            responseDto = ResponseDto.<PostRespDto>builder().objData(new PostRespDto(post)).build();
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            responseDto = ResponseDto.<PostRespDto>builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    // 게시글 수정
    @PutMapping("/modify/{id}")
    public ResponseEntity<?> modifyPost(@PathVariable Long id, @RequestBody PostReqDto reqDto,
                                        Principal principal) {
        ResponseDto<PostRespDto> responseDto;
        try {
            Post post = postService.modifyPost(id, reqDto, principal.getName());
            responseDto = ResponseDto.<PostRespDto>builder().objData(new PostRespDto(post)).build();
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            responseDto = ResponseDto.<PostRespDto>builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    // 게시글 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, Principal principal) {
        ResponseDto<?> responseDto;
        try {
            postService.deletePost(id, principal.getName());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            // HttpStatus.NOT_FOUND (404): 클라이언트가 요청한 자원이 서버에 존재하지 않을 때 사용합니다.
            responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Post>> getPostsByKeyword(@RequestParam String keyword, @PageableDefault(size = 9) Pageable pageable) {
        Page<Post> posts = postService.findPostsByKeyword(keyword, pageable);
        return ResponseEntity.ok(posts);
    }

}