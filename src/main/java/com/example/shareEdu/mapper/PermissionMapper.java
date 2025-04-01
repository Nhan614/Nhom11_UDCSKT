package com.example.shareEdu.mapper;

import com.example.shareEdu.dto.request.PermissionRequest;
import com.example.shareEdu.dto.response.PermissionResponse;
import com.example.shareEdu.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);


}
