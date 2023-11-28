package com.ll.lion.community.service;

import com.ll.lion.common.exception.DataNotFoundException;
import com.ll.lion.community.dto.post.PostDto;
import com.ll.lion.community.dto.post.PostReqDto;
import com.ll.lion.community.entity.Post;
import com.ll.lion.community.repository.PostRepository;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Post postSave(final PostReqDto postReqDto) {
        // 사용자 구하기
        User writer = userRepository.findByEmail(postReqDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("로그인 해주세요."));

        // reqDto -> Post
        Post postEntity = PostDto.toEntity(new PostDto(postReqDto, writer));

        // 게시글 저장하기
        return Optional.of(postRepository.save(postEntity))
                .orElseThrow(() -> new DataNotFoundException("게시글 등록에 실패했습니다."));
    }

    public List<Post> postList() {
        return Optional.of(postRepository.findAll())
                .orElseThrow(() -> new DataNotFoundException("등록된 게시글이 없습니다."));
    }

    public Post getPost(Long id) {
        // TODO 조회수
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("등록된 게시글이 없습니다."));

        post.setViewCount(post.getViewCount() +1);
        postRepository.save(post);

        return post;
    }

    public void deletePost(Long id) {
        // 게시글 존재하는 지 검사
        getPost(id);
        postRepository.deleteById(id);
    }
}