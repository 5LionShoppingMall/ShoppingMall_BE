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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 게시글 등록
    @Transactional
    public Post postSave(final PostReqDto reqDto, String userEmail) {
        // 사용자 구하기
        User writer = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("로그인 해주세요."));

        // reqDto -> Post
        Post postEntity = PostDto.toEntity(new PostDto(reqDto, writer));

        // 게시글 저장하기
        return Optional.of(postRepository.save(postEntity))
                .orElseThrow(() -> new DataNotFoundException("게시글 등록에 실패했습니다."));
    }

    // 게시글 모두 조회
    public List<Post> postList() {
        return Optional.of(postRepository.findAll())
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
    public Post modifyPost(Long id, PostReqDto reqDto, String userEmail) {
        // 게시글 찾기
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("등록된 게시글이 없습니다."));

        // 수정할 회원과 작성한 회원 같은지 확인
        isAuthor(userEmail, post);

        post.setTitle(reqDto.getTitle());
        post.setContent(reqDto.getContent());

        return post;
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long id, String userEmail) {
        // 게시글 존재하는 지 검사
        Post post = getPost(id);

        // 삭제할 회원과 작성한 회원 같은지 확인
        isAuthor(userEmail, post);

        postRepository.deleteById(id);
    }

    // 접근하려는 사용자가 게시글에 대한 권한이 있는지 확인
    private static void isAuthor(String userEmail, Post post) {
        if (!userEmail.equals(post.getUser().getEmail())) {
            throw new IllegalArgumentException("해당 게시글에 대한 권한이 없습니다.");
        }
    }

    // 게시글 수 - 테스트용
    public long count() {
        return postRepository.count();
    }
}