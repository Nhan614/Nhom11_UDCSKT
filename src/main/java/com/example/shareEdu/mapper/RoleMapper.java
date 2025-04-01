package com.example.shareEdu.mapper;

import com.example.shareEdu.dto.request.RoleRequest;
import com.example.shareEdu.dto.response.RoleResponse;
import com.example.shareEdu.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

    List<RoleResponse> toRoleResponseList(List<Role> roles);
}