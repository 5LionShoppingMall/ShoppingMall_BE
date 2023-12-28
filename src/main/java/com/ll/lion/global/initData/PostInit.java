package com.ll.lion.global.initData;

import com.ll.lion.community.entity.Post;
import com.ll.lion.community.repository.PostRepository;
import com.ll.lion.community.service.PostService;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class PostInit {
    @Autowired
    @Lazy
    private PostInit self;
    private final PostService postService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Bean
    public ApplicationRunner initPost() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        if (postService.count() > 0) return;

        IntStream.rangeClosed(1,5).forEach(i -> {
            String username = "test" + i + "@test.com";
            User user = userRepository.findByEmail(username).orElse(null);

            IntStream.rangeClosed(1,5).forEach(j->{
                Post post = Post.builder()
                        .title("제목" + j)
                        .content("내용" + j)
                        .viewCount(0)
                        .user(user)
                        .build();

                postRepository.save(post);
            });
        });
    }
}
