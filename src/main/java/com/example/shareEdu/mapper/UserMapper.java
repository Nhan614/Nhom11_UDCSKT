package com.example.shareEdu.mapper;

import com.example.shareEdu.dto.request.UserCreationRequest;
import com.example.shareEdu.dto.request.UserUpdateRequest;
import com.example.shareEdu.dto.response.UserResponse;
import com.example.shareEdu.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}