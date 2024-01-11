package com.ll.lion.community.comment.service;


import com.ll.lion.community.comment.dto.CommentDto;
import com.ll.lion.community.comment.dto.CommentSaveDto;
import com.ll.lion.community.comment.dto.CommentUpdateDto;
import com.ll.lion.community.comment.entity.Comment;
import com.ll.lion.community.comment.repository.CommentRepository;
import com.ll.lion.common.dto.ResponseDto;
import com.ll.lion.community.post.entity.Post;
import com.ll.lion.community.post.repository.PostRepository;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseDto<CommentDto> saveComment(CommentSaveDto commentSaveDto, String email) {
        Post post = postRepository.findById(commentSaveDto.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("포스트가 존재하지 않습니다. " + commentSaveDto.getPostId()));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다: " + email));

        Comment savedComment = commentRepository.save(CommentSaveDto.toEntity(commentSaveDto, user, post));
        CommentDto commentDto = CommentDto.fromEntityToDto(savedComment);

        return ResponseDto.<CommentDto>builder()
                .objData(commentDto)
                .build();
    }

    public ResponseDto<?> getAllCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);

        return ResponseDto.builder()
                .listData(comments.stream().map(CommentDto::fromEntityToDto).collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public ResponseDto<CommentDto> updateComment(Long commentId, CommentUpdateDto commentUpdateDto, String email) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다: " + commentId));

        userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다: " + email));

        comment.update(commentUpdateDto.getContent());

        return ResponseDto.<CommentDto>builder()
                .success("댓글이 수정되었습니다.")
                .objData(CommentDto.fromEntityToDto(comment))
                .build();

    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다: " + commentId));

        commentRepository.delete(comment);
    }
}
