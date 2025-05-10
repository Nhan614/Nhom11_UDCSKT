package com.example.shareEdu.mapper;

import com.example.shareEdu.dto.response.PostShareResponse;
import com.example.shareEdu.entity.PostShare;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface PostShareMapper {

    public PostShare toPostShare(PostShareResponse postShareResponse);


    public PostShareResponse toPostShareResponse(PostShare postShare);
}
