package com.ll.lion.community.comment.controller;


import com.ll.lion.community.comment.dto.CommentDto;
import com.ll.lion.community.comment.dto.CommentSaveDto;
import com.ll.lion.community.comment.dto.CommentUpdateDto;
import com.ll.lion.community.comment.service.CommentService;
import com.ll.lion.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDto<CommentDto>> saveComment(@RequestBody CommentSaveDto commentSaveDto,
                                                               @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(commentService.saveComment(commentSaveDto,
                userDetails.getUsername()));
    }

    @GetMapping("/list/{postId}")
    public ResponseEntity<?> list(@PathVariable Long postId) {
        ResponseDto<?> allCommentsByPostId = commentService.getAllCommentsByPostId(postId);
        return ResponseEntity.ok(allCommentsByPostId);
    }

    @PutMapping("/update/{commentId}")
    public ResponseEntity<ResponseDto<CommentDto>> updateComment(@PathVariable Long commentId,
                                                                 @RequestBody CommentUpdateDto commentUpdateDto,
                                                                 @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(commentService.updateComment(commentId, commentUpdateDto,
               userDetails.getUsername()));
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<ResponseDto<Void>> deleteComment(@PathVariable Long commentId,
                                                           @AuthenticationPrincipal UserDetails userDetails) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(ResponseDto.<Void>builder()
                .success("댓글이 삭제되었습니다.")
                .build());
    }

}
