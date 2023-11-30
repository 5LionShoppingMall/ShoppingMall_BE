package com.ll.lion.community.controller;

import com.ll.lion.common.dto.ResponseDto;
import com.ll.lion.community.dto.post.PostReqDto;
import com.ll.lion.community.dto.post.PostRespDto;
import com.ll.lion.community.entity.Post;
import com.ll.lion.community.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    // 게시글 등록
    @PostMapping("/save")
    public ResponseEntity<?> postSave(@Valid @RequestBody PostReqDto reqDto) {
        // TODO 로그인된 사용자 여부 체크

        ResponseDto<PostRespDto> responseDto;
        try {
            Post post = postService.postSave(reqDto);
            responseDto = ResponseDto.<PostRespDto>builder().objData(new PostRespDto(post)).build();
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            responseDto = ResponseDto.<PostRespDto>builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    // 게시글 모두 조회
    @GetMapping("/list")
    public ResponseEntity<?> postList(@RequestParam(value = "page", defaultValue = "0") int page) {
        ResponseDto<PostRespDto> responseDto;
        try {
            Page<Post> postList = postService.postList(page);
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
    public ResponseEntity<?> modifyPost(@PathVariable Long id, @RequestBody PostReqDto reqDto) {
        ResponseDto<PostRespDto> responseDto;
        try {
            Post post = postService.modifyPost(id, reqDto);
            responseDto = ResponseDto.<PostRespDto>builder().objData(new PostRespDto(post)).build();
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            responseDto = ResponseDto.<PostRespDto>builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    // 게시글 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        ResponseDto<?> responseDto;
        try {
            postService.deletePost(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            // HttpStatus.NOT_FOUND (404): 클라이언트가 요청한 자원이 서버에 존재하지 않을 때 사용합니다.
            responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }
}