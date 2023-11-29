package com.ll.lion.community.service;

import com.ll.lion.common.exception.DataNotFoundException;
import com.ll.lion.community.dto.post.PostDto;
import com.ll.lion.community.dto.post.PostReqDto;
import com.ll.lion.community.entity.Post;
import com.ll.lion.community.repository.PostRepository;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 게시글 등록
    public Post postSave(final PostReqDto reqDto) {
        // 사용자 구하기
        User writer = userRepository.findByEmail(reqDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("로그인 해주세요."));

        // reqDto -> Post
        Post postEntity = PostDto.toEntity(new PostDto(reqDto, writer));

        // 게시글 저장하기
        return Optional.of(postRepository.save(postEntity))
                .orElseThrow(() -> new DataNotFoundException("게시글 등록에 실패했습니다."));
    }

    // 게시글 모두 조회
    public Page<Post> postList(int page) {
        // 정렬 기준
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdAt"));
        // 페이지 나누는 기준 객체 생성
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return Optional.of(postRepository.findAll(pageable))
                .orElseThrow(() -> new DataNotFoundException("등록된 게시글이 없습니다."));
    }

    // 게시글 1개 조회
    @Transactional
    public Post getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("등록된 게시글이 없습니다."));

        // 조회수
        post.setViewCount(post.getViewCount() +1);

        return post;
    }

    // 게시글 수정
    @Transactional
    public Post modifyPost(Long id, PostReqDto reqDto) {
        // 게시글 찾기
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("등록된 게시글이 없습니다."));

        post.setTitle(reqDto.getTitle());
        post.setContent(reqDto.getContent());

        return post;
    }

    // 게시글 삭제
    public void deletePost(Long id) {
        // 게시글 존재하는 지 검사
        getPost(id);
        postRepository.deleteById(id);
    }
}