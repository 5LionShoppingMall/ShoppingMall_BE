package com.ll.lion.community.service;

import com.ll.lion.community.dto.post.PostReqDto;
import com.ll.lion.community.entity.Post;
import com.ll.lion.community.repository.PostRepository;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Post postSave(PostReqDto postReqDto) {
        // TODO .get() 사용하면 안되는지 알아보기
        User findUser = userRepository.findByEmail(postReqDto.getEmail()).get();
        Post post = new Post(postReqDto, findUser);
        return postRepository.save(post);
    }
}