package com.ll.lion.community.service;

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
        User user = userRepository.findById(1L).get();

        Post post = new Post(postReqDto, user);
        return postRepository.save(post);
    }

    public List<Post> postList() {
        return postRepository.findAll();
    }
}