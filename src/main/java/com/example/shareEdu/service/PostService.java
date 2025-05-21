package com.example.shareEdu.service;


import com.example.shareEdu.dto.request.PostCreateRequest;
import com.example.shareEdu.dto.request.UpdatePostRequest;
import com.example.shareEdu.dto.response.PostResponse;
import com.example.shareEdu.entity.Post;
import com.example.shareEdu.entity.Topic;
import com.example.shareEdu.entity.User;
import com.example.shareEdu.exception.AppException;
import com.example.shareEdu.exception.ErrorCode;
import com.example.shareEdu.mapper.PostMapper;
import com.example.shareEdu.repository.PostRepository;
import com.example.shareEdu.repository.TopicRepository;
import com.example.shareEdu.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.naming.Context;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class PostService {

    UserRepository userRepository;
    PostRepository postRepository;
    TopicRepository topicRepository;
    PostMapper postMapper;

    public PostResponse createPost(PostCreateRequest postCreateRequest) {
        // 4.4 Lấy SecurityContext để truy xuất tên người dùng hiện tại
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        // 4.5 Tìm người dùng theo tên và trả về người dùng hiện tại
        // Nếu không tìm thấy -> 4.6 Ném ngoại lệ UNAUTHENTICATED nếu không tìm thấy
        User author = userRepository.findByUserName(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        // 4.7 Ánh xạ PostCreateRequest thành thực thể Post
        Post post = postMapper.toPost(postCreateRequest);
        // 4.8 Thiết lập thời gian tạo cho bài viết
        post.setCreatedAt(LocalDateTime.now());
        // 4.9 Thiết lập tác giả của bài viết
        post.setAuthor(author);

        // 4.10 Lấy tất cả các chủ đề theo ID từ yêu cầu (bao gồm ghi log chủ đề từ yêu cầu)
        log.info("topic from request : " + postCreateRequest.getTopics().toString());
        var topicsList = topicRepository.findAllById(postCreateRequest.getTopics());

        // 4.11 Kiểm tra danh sách chủ đề có trống hoặc không đầy đủ
        // Nếu không hợp lệ -> 4.12 Ném ngoại lệ TOPIC_NOT_EXITS
        if (topicsList.isEmpty() || topicsList.size() < postCreateRequest.getTopics().size()) {
            throw new AppException(ErrorCode.TOPIC_NOT_EXITS);
        }

        // 4.13 Ghi log danh sách chủ đề đã lấy
        log.info("list toppic: " + topicsList);

        // 4.14 Tạo tập hợp các chủ đề từ danh sách đã lấy
        Set<Topic> topics = new HashSet<>(topicsList);

        // 4.15 Thiết lập các chủ đề cho bài viết
        post.setTopics(topics);
        // 4.16 Ghi log tập hợp các chủ đề cuối cùng
        log.info("topic: {}", topics);

        // 4.17 Lưu bài viết vào repository
        // 4.18 Trả về bài viết đã lưu (hoặc 4.19 ném ngoại lệ nếu thất bại)
        // 4.18 Chuyển đổi bài viết đã lưu thành PostResponse
        return postMapper.toPostResponse(postRepository.save(post));
    }
    public Post getPost(Long id){
        return postRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.POST_NOT_FOUND));
    }
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "(hasAuthority('DELETE_OWN_POST') and #post.author.id == authentication.principal.id)")
    public void softDeletePost(Post post){
        post.setDeleted(true);
        postRepository.save(post);
        log.info("Post with ID {} has been soft deleted", post.getId());


    }
    public PostResponse updatePost(UpdatePostRequest updatePostRequest) {
        Post post = getPost(updatePostRequest.getId());
        post.setUpdatedAt(LocalDateTime.now());

        postMapper.toPost(updatePostRequest);
        return postMapper.toPostResponse(postRepository.save(post));
    }
}
