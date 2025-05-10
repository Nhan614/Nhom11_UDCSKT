package com.example.shareEdu.mapper;

import com.example.shareEdu.dto.request.PostCreateRequest;
import com.example.shareEdu.dto.request.UpdatePostRequest;
import com.example.shareEdu.dto.response.PostResponse;
import com.example.shareEdu.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface PostMapper {

    @Mapping(target = "topics", ignore = true)
    Post toPost(PostCreateRequest post);

    Post toPost(UpdatePostRequest post);


    PostResponse toPostResponse(Post post);
}
