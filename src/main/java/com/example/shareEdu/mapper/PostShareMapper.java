package com.example.shareEdu.mapper;

import com.example.shareEdu.dto.response.PostShareResponse;
import com.example.shareEdu.entity.PostShare;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper(componentModel = "spring")
public interface PostShareMapper {

    public PostShare toPostShare(PostShareResponse postShareResponse);


    @Mappings({
            @Mapping(source = "post", target = "post"),
            @Mapping(source = "content", target = "caption")
    })
    public PostShareResponse toPostShareResponse(PostShare postShare);
}
